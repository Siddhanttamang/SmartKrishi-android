package com.example.smartkrishi;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartkrishi.Services.RegisterService;
import com.example.smartkrishi.models.UserRegister;
import com.example.smartkrishi.utils.PestDetectionActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText userName, userEmail, userAddress, userContact, userPassword;
    private Button userCreateBtn;
    private ScrollView scrollView;
    private EditText passwordEditText;

    private TextView userError;

    private RegisterService registerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Bind views
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userAddress = findViewById(R.id.user_address);
        userContact = findViewById(R.id.user_contact);
        userPassword = findViewById(R.id.user_password);
        userCreateBtn = findViewById(R.id.user_create_btn);
        userError = findViewById(R.id.user_error);
        scrollView=findViewById(R.id.RegisterScrollView);

        registerService = new RegisterService(this);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            scrollView.getWindowVisibleDisplayFrame(r);
            int screenHeight = scrollView.getRootView().getHeight();
            int keypadHeight = screenHeight - r.bottom;

            if (keypadHeight > screenHeight * 0.15) {
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    scrollView.post(() -> {
                        scrollView.smoothScrollTo(0, focusedView.getTop());
                    });

                }
            }
        });




        userCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    String name = userName.getText().toString().trim();
                    String email = userEmail.getText().toString().trim();
                    String address = userAddress.getText().toString().trim();
                    String phone = userContact.getText().toString().trim();
                    String password = userPassword.getText().toString().trim();

                    UserRegister user = new UserRegister(name, phone, password, address, email);

                    registerService.registerUser(user, new RegisterService.AuthCallback() {
                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
                            finish(); // Optional: close this activity

                        }

                        @Override
                        public void onError(String error) {
                            userError.setText(error);
                            userError.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }

    private boolean validateForm() {
        String name = userName.getText().toString().trim();
        String email = userEmail.getText().toString().trim();
        String address = userAddress.getText().toString().trim();
        String phone = userContact.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        userError.setVisibility(View.GONE); // Hide error on new submit

        if (name.isEmpty()) {
            userName.setError("Name is required");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("Invalid email format");
            return false;
        }
        if (address.isEmpty()) {
            userAddress.setError("Address is required");
            return false;
        }
        if (phone.length() != 10 || !phone.matches("\\d+")) {
            userContact.setError("Enter valid 10-digit phone number");
            return false;
        }
        if (password.length() < 6) {
            userPassword.setError("Password must be at least 6 characters");
            return false;
        }

        return true;
    }


}
