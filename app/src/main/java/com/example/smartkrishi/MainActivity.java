package com.example.smartkrishi;

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
        ImageButton exitButton = findViewById(R.id.exit_button);
        header = findViewById(R.id.nav_header);
//        exitButton.setOnClickListener(v -> finish());
        exitButton.setOnClickListener(v -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Exit")
                    .setMessage("Do you want to exit?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
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