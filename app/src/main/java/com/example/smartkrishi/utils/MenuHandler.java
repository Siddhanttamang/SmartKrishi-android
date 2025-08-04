package com.example.smartkrishi.utils;

import android.view.MenuItem;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

        } else if (itemId == R.id.nav_news) {
            replaceFragment(new NewsFragment());
            header.setText("News");

        } else if (itemId == R.id.nav_detect) {
            // Show dialog overlay instead of replacing fragment
            new ImagePickerFragment().show(activity.getSupportFragmentManager(), "ImagePicker");
            return true;

        } else if (itemId == R.id.nav_market) {
            header.setText("Market Place");
            replaceFragment(new MarketFragment());

        } else if (itemId == R.id.nav_settings) {
            header.setText("Settings");
            replaceFragment(new SettingFragment());
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
