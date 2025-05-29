package com.example.smartkrishi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartkrishi.models.UserRegister;
import com.example.smartkrishi.utils.RegisterService;

public class AuthActivity extends Activity {
    private TextView user_error;
    private EditText user_name;
    private EditText user_password;
    private EditText user_email;
    private EditText user_address;
    private EditText user_contact;
    private Button user_create_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        user_error=findViewById(R.id.user_error);
        user_name=findViewById(R.id.user_name);
        user_email=findViewById(R.id.user_email);
        user_password=findViewById(R.id.user_password);
        user_contact=findViewById(R.id.user_contact);
        user_address=findViewById(R.id.user_address);
        user_create_btn= findViewById(R.id.user_create_btn);

        user_create_btn.setOnClickListener(e -> {
            String name = user_name.getText().toString().trim();
            String email = user_email.getText().toString().trim();
            String password = user_password.getText().toString().trim();
            String phone = user_contact.getText().toString().trim();
            String address = user_address.getText().toString().trim();

            if (name.isEmpty()) {
                user_error.setText("Name is required.");
            } else if (email.isEmpty()) {
                user_error.setText("Email is required.");
            } else if (password.isEmpty()) {
                user_error.setText("Password is required.");
            } else if (phone.isEmpty()) {
                user_error.setText("Contact number is required.");
            } else if (address.isEmpty()) {
                user_error.setText("Address is required.");
            } else {
                user_error.setText(""); // clear any previous errors
                // Proceed with further operations (e.g., API call, storing data)
                UserRegister user = new UserRegister(name,phone,password,address,email);
                RegisterService registerService = new RegisterService(this);
                registerService.registerUser(user, new RegisterService.AuthCallback() {
                    @Override
                    public void onSuccess(String message) {
                        runOnUiThread(() -> {
                            Toast.makeText(AuthActivity.this, message, Toast.LENGTH_SHORT).show();
                            // Optionally navigate to next activity
                            Intent intent = new Intent(AuthActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> user_error.setText(error));
                    }

                });
            }
        });


    }

}
