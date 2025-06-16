package com.example.smartkrishi.Services;

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
        reportsApi.getReports("Bearer " + token).enqueue(new Callback<ReportResponse>() {
            @Override
            public void onResponse(Call<ReportResponse> call, Response<ReportResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onError("Failed to fetch reports");
                }
            }

            @Override

            public void onFailure(Call<ReportResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }

        });
    }

    public interface ReportCallback {
        void onSuccess(List<Reports> reportsList);
        void onError(String message);


    }
}
