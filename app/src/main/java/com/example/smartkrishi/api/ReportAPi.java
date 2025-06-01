package com.example.smartkrishi.api;

import com.example.smartkrishi.models.Recommendation;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ReportAPi {
    @POST("api/report")
    Call<Void> createReport(@Header("Authorization") String token, @Body Recommendation recommendation
    );
}
