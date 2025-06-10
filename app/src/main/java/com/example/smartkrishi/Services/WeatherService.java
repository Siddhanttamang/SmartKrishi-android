package com.example.smartkrishi.Services;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.example.smartkrishi.api.RetrofitClient;
import com.example.smartkrishi.api.WeatherApi;
import com.example.smartkrishi.Responses.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherService {
    private static final String TAG = "WeatherService";

    private final WeatherApi weatherApi;

    public WeatherService(Context context) {
        weatherApi = RetrofitClient.getClient().create(WeatherApi.class);
    }

    public void fetchWeather(String city, TextView weatherInfo, TextView weatherLocation) {
        weatherApi.getWeather(city).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse data = response.body();
                    weatherLocation.setText(data.getCity());
                    weatherInfo.setText(String.format("%d°C - %s", data.getTemperature(), data.getWeather()));
                } else {
                    weatherInfo.setText("Failed to get valid response.");
                    Log.e(TAG, "Invalid response: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                weatherInfo.setText("Error fetching weather.");
                Log.e(TAG, "Retrofit error: ", t);
            }
        });
    }
}
