package com.example.smartkrishi.api;

import com.example.smartkrishi.Responses.ReportResponse;
import com.example.smartkrishi.models.Recommendation;
import com.example.smartkrishi.models.Reports;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ReportsAPi {
    @Multipart
    @POST("api/report")
    Call<Void> createReport(
            @Header("Authorization") String token,
            @Part("crop") RequestBody crop,
            @Part("disease") RequestBody disease,
            @Part("recommendation") RequestBody recommendation,
            @Part MultipartBody.Part image
    );

    @GET("api/report")
    Call<List<Reports>> getReports(@Header("Authorization") String token);

}
