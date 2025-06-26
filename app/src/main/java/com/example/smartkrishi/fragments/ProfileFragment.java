package com.example.smartkrishi.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.smartkrishi.R;
import com.example.smartkrishi.Services.UserService;
import com.example.smartkrishi.Responses.UserLoginResponse;
import com.example.smartkrishi.models.UserUpdateRequest;

public class ProfileFragment extends Fragment {

    private EditText editName, editEmail, editContact, editAddress, editPassword;
    private Button btnUpdate;
    private SharedPreferences prefs;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        // Initialize UI elements
        editName = view.findViewById(R.id.editName);
        editEmail = view.findViewById(R.id.editEmail);
        editContact = view.findViewById(R.id.editContact);
        editAddress = view.findViewById(R.id.editAddress);
        editPassword = view.findViewById(R.id.editPassword);
        btnUpdate = view.findViewById(R.id.btnUpdateProfile);

        // Get access token from SharedPreferences
        prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = prefs.getString("auth_token", null);

        if (token != null) {
            fetchUserProfile();
        }

        btnUpdate.setOnClickListener(v -> {
            updateUserProfile();
        });

        return view;
    }

    private void fetchUserProfile() {
        new UserService().getCurrentUser(token, new UserService.UserCallback() {
            @Override
            public void onSuccess(UserLoginResponse.UserData user) {
                editName.setText(user.getName());
                editEmail.setText(user.getEmail());
                editContact.setText(user.getContact());
                editAddress.setText(user.getAddress());
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "Failed to load profile: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfile() {
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        String contact = editContact.getText().toString();
        String address = editAddress.getText().toString();
        String password = editPassword.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Name and email are required", Toast.LENGTH_SHORT).show();
            return;
        }

        UserUpdateRequest request = new UserUpdateRequest(name, email, address, contact, password);

        new UserService().updateUser(token, request, new UserService.UserCallback() {
            @Override
            public void onSuccess(UserLoginResponse.UserData user) {
                Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                editPassword.setText(""); // Clear password field after update
                // Update SharedPreferences
                SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("user_id", user.getId());
                editor.putString("user_name", user.getName());
                editor.putString("user_email", user.getEmail());
                editor.putString("user_contact", user.getContact());
                editor.putString("user_address", user.getAddress());
                editor.apply();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "Update failed: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
