package com.example.smartkrishi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.smartkrishi.Database.ReportDAO;
import com.example.smartkrishi.utils.MenuHandler;
import com.example.smartkrishi.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.app.AlertDialog;

public class MainActivity extends FragmentActivity {
    private MenuHandler menuHandler;
    private TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton logoutBtn = findViewById(R.id.logout_button);
        ImageButton appLogo = findViewById(R.id.app_logo);
        Button loginBtn = findViewById(R.id.login_button);
        header = findViewById(R.id.nav_header);

        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (token != null && !token.isEmpty()) {
            // User logged in - show logout, hide login
            loginBtn.setVisibility(View.GONE);
            logoutBtn.setVisibility(View.VISIBLE);

            logoutBtn.setOnClickListener(v -> {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Logout")
                        .setMessage("Do you want to Logout?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("auth_token");
                            editor.remove("user_data");  // optional clear user info
                            editor.apply();
                            ReportDAO reportDAO= new ReportDAO(this);
                            reportDAO.clearReports();



                            // Update UI
                            loginBtn.setVisibility(View.VISIBLE);
                            logoutBtn.setVisibility(View.GONE);

                            // Redirect to LoginActivity
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // prevent back navigation
                        })
                        .setNegativeButton("No", null)
                        .show();
            });

        } else {
            // User not logged in - show login, hide logout
            loginBtn.setVisibility(View.VISIBLE);
            logoutBtn.setVisibility(View.GONE);

            loginBtn.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            });
        }

        appLogo.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            header.setText("Home");
        });

        // Setup bottom navigation and header update
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        menuHandler = new MenuHandler(MainActivity.this);
        bottomNav.setOnItemSelectedListener(item -> menuHandler.onNavigationItemSelected(item, header));

        // Load initial fragment (Home)
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }
}
