package com.example.smartkrishi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartkrishi.R;

public class SettingFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting, container, false);

        // Find the "Report History" button
       LinearLayout reportHistoryButton = view.findViewById(R.id.reportHistoryBtn);

        // Set click listener to navigate to ReportFragment
        reportHistoryButton.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.fragment_container, new ReportFragment());
            transaction.addToBackStack(null);  // Allows back navigation
            transaction.commit();
        });

        return view;
    }
}
