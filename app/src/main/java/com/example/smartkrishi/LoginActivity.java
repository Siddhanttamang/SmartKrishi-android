package com.example.smartkrishi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.smartkrishi.Services.LoginService;
import com.example.smartkrishi.Services.UserService;
import com.example.smartkrishi.Responses.UserLoginResponse;
import com.google.gson.Gson;

public class LoginActivity extends Activity {

    private EditText emailInput, passwordInput;
    private Button loginBtn;
    private Button newAccountBtn;
    private TextView loginError;

    private LoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);  // Your layout file

        emailInput = findViewById(R.id.login_email);
        passwordInput = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        loginError = findViewById(R.id.login_error);
        newAccountBtn =findViewById(R.id.newuser_btn);

        loginService = new LoginService(this);
        newAccountBtn.setOnClickListener(e->{

            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);

        });

        loginBtn.setOnClickListener(e -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                loginError.setText("Email and password are required");
                return;
            }

            // Use LoginService
            loginService.login(email, password, new LoginService.LoginCallback() {
                @Override
                public void onSuccess(String token) {
                    // Save token first
                    SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
                    prefs.edit().putString("auth_token", token).apply();

                    // Now fetch user info
                    UserService userService = new UserService();
                    userService.getCurrentUser(token, new UserService.UserCallback() {
                        @Override
                        public void onSuccess(UserLoginResponse.UserData user) {
                            // Save user info as JSON
                            Gson gson = new Gson();
                            String userJson = gson.toJson(user);
                            prefs.edit().putString("user_data", userJson).apply();

                            // Go to MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(String message) {
                            loginError.setText("Login success, but failed to get user info.");
                        }
                    });
                }

                @Override
                public void onError(String message) {
                    loginError.setText(message);
                }
            });
        });

    }
}
