package com.example.smartkrishi.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
    private TextView reportLoginRequired;
    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        return token != null && !token.isEmpty();
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reports, container, false);

        reportRecyclerView = view.findViewById(R.id.reportsRecyclerView);
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reportLoginRequired =view.findViewById(R.id.reportLoginRequired);


        ReportDAO reportDAO = new ReportDAO(requireContext());
        if (!isLoggedIn()) {
            reportDAO.clearReports();
            reportRecyclerView.setVisibility(View.GONE);
            reportLoginRequired.setText("Please log in to view reports.");
            reportLoginRequired.setVisibility(View.VISIBLE);


            return view;
        }


        // Load cached news first
        List<Reports> cachedReport = reportDAO.getReports();
        Collections.reverse(cachedReport);

        if (!cachedReport.isEmpty()) {
            reportAdapter = new ReportAdapter(cachedReport);
            reportRecyclerView.setAdapter(reportAdapter);
            reportAdapter.setOnItemClickListener(report -> {
                ReportDetailsDialogFragment dialog = ReportDetailsDialogFragment.newInstance(report);
                dialog.show(getParentFragmentManager(), "ProductDetailsDialog");
            });
            reportRecyclerView.setVisibility(View.VISIBLE);
        }
        else {
            reportRecyclerView.setVisibility(View.GONE);
            reportLoginRequired.setText("No reports available.");
            reportLoginRequired.setVisibility(View.VISIBLE);

        }

        SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("auth_token", null);
        ReportService reportService = new ReportService();
        reportService.getAllReports(token,new ReportService.ReportCallback() {
            @Override
            public void onSuccess(List<Reports> reportsList) {
                if (isAdded() && getContext() != null) {
                    reportDAO.clearReports();

                    if (reportsList == null || reportsList.isEmpty()) {

                        reportRecyclerView.setVisibility(View.GONE);
                        reportLoginRequired.setText("No reports available.");
                        reportLoginRequired.setVisibility(View.VISIBLE);
                        return;
                    }


                    for (Reports report : reportsList) {
                        if (report != null && report.getCrop_name() != null) {
                            reportDAO.insertReport(report);
                        }
                    }

                    reportAdapter = new ReportAdapter(reportsList);
                    reportRecyclerView.setAdapter(reportAdapter);
                    reportAdapter.setOnItemClickListener(report -> {
                        ReportDetailsDialogFragment dialog = ReportDetailsDialogFragment.newInstance(report);
                        dialog.show(getParentFragmentManager(), "ProductDetailsDialog");
                    });

                    reportRecyclerView.setVisibility(View.VISIBLE);
                    reportLoginRequired.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(String errorMessage) {
                if (isAdded() && getContext() != null) {
                    Log.d("ReportFragment",errorMessage);


                    if (cachedReport.isEmpty()) {
                        reportRecyclerView.setVisibility(View.GONE);
                    }
                }
            }

        });

        return view;
    }

}
