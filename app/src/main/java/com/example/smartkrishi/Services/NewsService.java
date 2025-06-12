package com.example.smartkrishi.Services;

import android.util.Log;

import com.example.smartkrishi.api.NewsApi;
import com.example.smartkrishi.api.RetrofitClient;
import com.example.smartkrishi.models.News;
import com.example.smartkrishi.Responses.NewsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsService {
    private static final String TAG = "NewsService";
    private final NewsApi newsApi;

    public interface NewsCallback {
        void onSuccess(List<News> newsList);
        void onFailure(String errorMessage);
    }

    public NewsService() {
        newsApi = RetrofitClient.getClient().create(NewsApi.class);
    }

    public void fetchNews(NewsCallback callback) {
        newsApi.getAllNews().enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    callback.onSuccess(response.body().getData());
                } else {
                    callback.onFailure("Failed: Invalid response");
                    Log.e(TAG, "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                callback.onFailure("Error: " + t.getMessage());
                Log.e(TAG, "Retrofit error: ", t);
            }
        });
    }
}
