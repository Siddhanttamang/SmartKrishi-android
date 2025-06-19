package com.example.smartkrishi.api;

import com.example.smartkrishi.Responses.ReportResponse;
import com.example.smartkrishi.models.Recommendation;
import com.example.smartkrishi.models.Reports;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ReportsAPi {
    @POST("api/report")
    Call<Void> createReport(@Header("Authorization") String token, @Body Recommendation recommendation
    );
    @GET("api/report")
    Call<List<Reports>> getReports(@Header("Authorization") String token);

}
