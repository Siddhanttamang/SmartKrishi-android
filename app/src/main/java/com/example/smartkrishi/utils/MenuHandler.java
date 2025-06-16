package com.example.smartkrishi.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.widget.TextView;

import com.example.smartkrishi.MainActivity;
import com.example.smartkrishi.R;
import com.example.smartkrishi.fragments.HomeFragment;
import com.example.smartkrishi.fragments.ImagePickerFragment;
import com.example.smartkrishi.fragments.MarketFragment;
import com.example.smartkrishi.fragments.NewsFragment;
import com.example.smartkrishi.fragments.SettingFragment;

public class MenuHandler {
    private final MainActivity activity;

    public MenuHandler(MainActivity activity) {
        this.activity = activity;
    }

    public boolean onNavigationItemSelected(MenuItem item, TextView header) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            replaceFragment(new HomeFragment());
            header.setText("Home");
//            Toast.makeText(activity, "Home clicked", Toast.LENGTH_SHORT).show();

        } else if (itemId == R.id.nav_news) {
            replaceFragment(new NewsFragment());
            header.setText("News");
//            Toast.makeText(activity, "News clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_detect) {
            replaceFragment(new ImagePickerFragment());

        } else if (itemId == R.id.nav_market) {
            header.setText("Market Place");
            replaceFragment(new MarketFragment());
//            Toast.makeText(activity, "Marketplace clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_settings) {
            // Replace with SettingsFragment (create if needed)
            header.setText("Settings");
            replaceFragment(new SettingFragment());
//            Toast.makeText(activity, "Settings clicked", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}