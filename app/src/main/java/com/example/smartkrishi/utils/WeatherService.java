package com.example.smartkrishi.utils;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;

public class WeatherService {
    private static final String TAG = "WeatherService";
    private RequestQueue requestQueue;

    public WeatherService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void fetchWeather(String city, TextView weatherInfo, TextView weatherLocation) {
        String url = "http://192.168.1.71:5000/api/weather?city=" + city;//if physical devce use your pc ip address or use http://10.0.2.2:5000/weather?city=

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (!response.has("city") || !response.has("temperature") || !response.has("weather")) {
                            weatherInfo.setText("Incomplete data received.");
                            return;
                        }

                        String cityName = response.getString("city");
                        int temperature = response.getInt("temperature");
                        String weatherDescription = response.getString("weather");

                        weatherLocation.setText(cityName);
                        weatherInfo.setText(String.format("%d°C - %s", temperature, weatherDescription));

                    } catch (JSONException e) {
                        Log.e(TAG, "JSON error: ", e);
                        weatherInfo.setText("Error processing data.");
                    }
                },
                error -> {
                    Log.e(TAG, "Volley error: ", error);
                    weatherInfo.setText("Failed to fetch weather.");
                }
        );

        requestQueue.add(request);
    }
}
