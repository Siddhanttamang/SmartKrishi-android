// WeatherService.java
package com.example.smartkrishi.Services;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.smartkrishi.Responses.WeatherResponse;
import com.example.smartkrishi.api.RetrofitClient;
import com.example.smartkrishi.api.WeatherApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherService {
    private final WeatherApi weatherApi;

    public WeatherService(Context context) {
        weatherApi = RetrofitClient.getClient().create(WeatherApi.class);
    }

    public void fetchWeather(
            String city,
            TextView temperature,
            TextView feelsLike,
            TextView humidity,
            TextView wind,
            TextView description,
            TextView weatherLocation,
            ImageView weatherIcon
    ) {
        weatherApi.getWeather(city).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse data = response.body();

                    temperature.setText("Temperature: "+ data.getTemperature() + "°C");
                    feelsLike.setText("Feels like: " + data.getFeels_like() + "°C");
                    humidity.setText("Humidity: " + data.getHumidity() + "%");
                    wind.setText("Wind: " + data.getWind_speed() + " m/s");
                    description.setText(data.getDescription());
                    weatherLocation.setText(data.getCity());

                    String iconUrl = "https://openweathermap.org/img/wn/" + data.getIcon() + "@2x.png";
                    Glide.with(weatherIcon.getContext()).load(iconUrl).into(weatherIcon);
                } else {
                    temperature.setText("Failed to load weather");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                temperature.setText("Weather fetch failed");
            }
        });
    }
}
