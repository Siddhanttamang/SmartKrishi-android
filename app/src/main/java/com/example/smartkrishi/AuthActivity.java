package com.example.smartkrishi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartkrishi.models.UserRegister;
import com.example.smartkrishi.Services.RegisterService;

public class AuthActivity extends Activity {

    private TextView user_error;
    private EditText user_name, user_password, user_email, user_address, user_contact;
    private Button user_create_btn;
    private RegisterService registerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Initialize UI components
        user_error = findViewById(R.id.user_error);
        user_name = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        user_contact = findViewById(R.id.user_contact);
        user_address = findViewById(R.id.user_address);
        user_create_btn = findViewById(R.id.user_create_btn);

        // Initialize RegisterService
        registerService = new RegisterService(this);

        // Button click listener
        user_create_btn.setOnClickListener(view -> {
            if (validateForm()) {
                String name = user_name.getText().toString().trim();
                String email = user_email.getText().toString().trim();
                String address = user_address.getText().toString().trim();
                String phone = user_contact.getText().toString().trim();
                String password = user_password.getText().toString().trim();

                UserRegister user = new UserRegister(name, phone, password, address, email);

                registerService.registerUser(user, new RegisterService.AuthCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(AuthActivity.this, message, Toast.LENGTH_LONG).show();
                        clearForm();
                    }

                    @Override
                    public void onError(String error) {
                        user_error.setText(error);
                        user_error.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    // Validation
    private boolean validateForm() {
        String name = user_name.getText().toString().trim();
        String email = user_email.getText().toString().trim();
        String address = user_address.getText().toString().trim();
        String phone = user_contact.getText().toString().trim();
        String password = user_password.getText().toString().trim();

        user_error.setVisibility(View.GONE); // Hide previous error

        boolean isValid = true;

        if (name.isEmpty()) {
            user_name.setError("Name is required");
            isValid = false;
        }

        if (email.isEmpty()) {
            user_email.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            user_email.setError("Invalid email format");
            isValid = false;
        }

        if (address.isEmpty()) {
            user_address.setError("Address is required");
            isValid = false;
        }

        if (phone.isEmpty()) {
            user_contact.setError("Phone number is required");
            isValid = false;
        } else if (!phone.matches("\\d{10}")) {
            user_contact.setError("Enter valid 10-digit phone number");
            isValid = false;
        }

        if (password.isEmpty()) {
            user_password.setError("Password is required");
            isValid = false;
        } else if (password.length() < 6) {
            user_password.setError("Password must be at least 6 characters");
            isValid = false;
        }

        return isValid;
    }

    // Reset fields
    private void clearForm() {
        user_name.setText("");
        user_email.setText("");
        user_address.setText("");
        user_contact.setText("");
        user_password.setText("");
        user_error.setVisibility(View.GONE);
    }
}
