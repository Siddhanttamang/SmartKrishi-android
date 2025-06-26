package com.example.smartkrishi.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartkrishi.LoginActivity;
import com.example.smartkrishi.MainActivity;
import com.example.smartkrishi.R;
import com.example.smartkrishi.Services.UserService;

public class SettingFragment extends Fragment {
    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        return token != null && !token.isEmpty();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting, container, false);

        LinearLayout reportHistoryButton = view.findViewById(R.id.reportHistoryBtn);
        LinearLayout profileBtn = view.findViewById(R.id.profileBtn);
        LinearLayout deleteBtn = view.findViewById(R.id.deleteAccountBtn);

        reportHistoryButton.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.fragment_container, new ReportFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        profileBtn.setOnClickListener(v -> {
            if(!isLoggedIn()){
                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();

            }else {
                FragmentTransaction transaction = requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.fragment_container, new ProfileFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        deleteBtn.setOnClickListener(v -> {
            if(!isLoggedIn()){
                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();

            }else {

                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete Account")
                        .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            deleteAccount();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        return view;
    }

    private void deleteAccount() {
        SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("auth_token", null);

        if (token == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        UserService userService = new UserService();
        userService.deleteAccount(token, new UserService.DeletionCallback() {
            @Override
            public void onSuccess() {
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();

                Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(requireContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(requireContext(), "Failed: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
