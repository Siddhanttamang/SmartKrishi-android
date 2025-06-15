package com.example.smartkrishi.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartkrishi.MainActivity;
import com.example.smartkrishi.R;
import com.example.smartkrishi.fragments.HomeFragment;
import com.example.smartkrishi.fragments.MarketFragment;
import com.example.smartkrishi.fragments.NewsFragments;

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
            replaceFragment(new NewsFragments());
            header.setText("News");
//            Toast.makeText(activity, "News clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_detect) {
            replaceFragment(new ImagePickerFragment());
//            activity.getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(android.R.id.content, new ImagePickerFragment())
//                    .addToBackStack(null)
//                    .commit();
//            return true;
        } else if (itemId == R.id.nav_market) {
            header.setText("Market Place");
            replaceFragment(new MarketFragment());
//            Toast.makeText(activity, "Marketplace clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_settings) {
            // Replace with SettingsFragment (create if needed)
            header.setText("Settings");
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