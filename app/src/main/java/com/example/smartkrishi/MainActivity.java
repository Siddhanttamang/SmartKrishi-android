package com.example.smartkrishi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import com.example.smartkrishi.utils.MenuHandler;
import com.example.smartkrishi.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class MainActivity extends FragmentActivity {
    private MenuHandler menuHandler;
    private TextView header;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize exit button
        ImageButton logoutButton = findViewById(R.id.logout_button);
        header = findViewById(R.id.nav_header);
//        exitButton.setOnClickListener(v -> finish());
        logoutButton.setOnClickListener(v -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Logout")
                    .setMessage("Do you want to Logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });


        // Setup navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        menuHandler = new MenuHandler(MainActivity.this);
        bottomNav.setOnItemSelectedListener(item -> menuHandler.onNavigationItemSelected(item,header));

        // Load initial fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }
}