package com.example.smartkrishi.Services;

import android.util.Log;

import com.example.smartkrishi.Responses.ReportResponse;
import com.example.smartkrishi.api.ReportsAPi;
import com.example.smartkrishi.api.RetrofitClient;
import com.example.smartkrishi.models.Reports;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportService {

    private final ReportsAPi reportsApi;

    public ReportService() {
        this.reportsApi = RetrofitClient.getClient().create(ReportsAPi.class);
    }

    public void getAllReports(String token, ReportCallback callback) {
        reportsApi.getReports("Bearer " + token).enqueue(new Callback<List<Reports>>() {
            @Override
            public void onResponse(Call<List<Reports>> call, Response<List<Reports>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to fetch reports");

                }
            }

            @Override

            public void onFailure(Call<List<Reports>> call, Throwable t) {
                callback.onFailure("Network error: " + t.getMessage());

            }

        });
    }

    public interface ReportCallback {
        void onSuccess(List<Reports> reportsList);
        void onFailure(String message);


    }
}
