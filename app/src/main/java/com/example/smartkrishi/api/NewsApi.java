package com.example.smartkrishi.api;

import com.example.smartkrishi.Responses.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsApi {
    @GET("api/news")
    Call<NewsResponse> getAllNews();
}
