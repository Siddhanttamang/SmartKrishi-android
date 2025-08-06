package com.example.smartkrishi.api;

import com.example.smartkrishi.Responses.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("api/weather")  // Make sure this matches your backend route
    Call<WeatherResponse> getWeather(@Query("city") String city);
}

