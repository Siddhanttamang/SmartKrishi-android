package com.example.smartkrishi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartkrishi.Services.RegisterService;
import com.example.smartkrishi.models.UserRegister;

public class RegisterActivity extends AppCompatActivity {

    private EditText userName, userEmail, userAddress, userContact, userPassword;
    private Button userCreateBtn;
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

        registerService = new RegisterService(this);

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
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();

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
