package com.example.smartkrishi.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartkrishi.Database.ReportDAO;
import com.example.smartkrishi.R;
import com.example.smartkrishi.Services.ReportService;
import com.example.smartkrishi.adapters.ReportAdapter;
import com.example.smartkrishi.models.Reports;

import java.util.Collections;
import java.util.List;

public class ReportFragment extends Fragment {

    private RecyclerView reportRecyclerView;
    private ReportAdapter reportAdapter;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reports, container, false);

        reportRecyclerView = view.findViewById(R.id.reportsRecyclerView);
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        ReportDAO reportDAO = new ReportDAO(requireContext());

        // Load cached news first
        List<Reports> cachedReport = reportDAO.getReports();
        Collections.reverse(cachedReport);

        if (!cachedReport.isEmpty()) {

            reportAdapter = new ReportAdapter(cachedReport);
            reportRecyclerView.setAdapter(reportAdapter);
            reportRecyclerView.setVisibility(View.VISIBLE);
        } else {
            // If no cached data, show loading
            reportRecyclerView.setVisibility(View.GONE);

        }

        SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("auth_token", null);

        if (token == null) {
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return view;
        }

        ReportService reportService = new ReportService();
        reportService.getAllReports(token,new ReportService.ReportCallback() {
            @Override

            public void onSuccess(List<Reports> reportsList) {
                if (isAdded() && getContext() != null) {
                    // Clear previous local data before inserting new
                    reportDAO.clearReports();

                    // Insert fresh reports from API
                    for (Reports report : reportsList) {
                        reportDAO.insertReport(report);
                    }

                    // Update UI
                    reportAdapter = new ReportAdapter(reportsList);
                    reportRecyclerView.setAdapter(reportAdapter);
                    reportRecyclerView.setVisibility(View.VISIBLE);
                    reportAdapter.setOnItemClickListener(report -> {
                        ProductDetailsDialogFragment dialog = ProductDetailsDialogFragment.newInstance(report);
                        dialog.show(getParentFragmentManager(), "ProductDetailsDialog");
                    });

                }
            }

            @Override
            public void onFailure(String errorMessage) {
                if (isAdded() && getContext() != null) {
                    Log.d("ReportFragment",errorMessage);
                    Toast.makeText(getContext(), "Failed to get Report: " + errorMessage, Toast.LENGTH_SHORT).show();

                    if (cachedReport.isEmpty()) {
                        reportRecyclerView.setVisibility(View.GONE);
                    }
                }
            }

        });

        return view;
    }

}
