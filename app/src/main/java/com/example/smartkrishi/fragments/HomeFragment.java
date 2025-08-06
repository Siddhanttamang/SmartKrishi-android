// HomeFragment.java
package com.example.smartkrishi.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.smartkrishi.R;
import com.example.smartkrishi.Services.WeatherService;

public class HomeFragment extends Fragment {
    private TextView userName, weatherLocation, temperature, feelsLike, humidity, wind, description;
    private ImageView weatherIcon;
    private WeatherService weatherService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        // Initialize views
        userName = view.findViewById(R.id.user_name);
        weatherLocation = view.findViewById(R.id.weather_location);
        temperature = view.findViewById(R.id.temperature);
        feelsLike = view.findViewById(R.id.feels_like);
        humidity = view.findViewById(R.id.humidity);
        wind = view.findViewById(R.id.wind);
        description = view.findViewById(R.id.description);
        weatherIcon = view.findViewById(R.id.weather_icon);

        if (!isLoggedIn()) {
            return view;
        }

        SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String name = prefs.getString("user_name", "User");
        String address = prefs.getString("user_address", "Kathmandu");

        userName.setText("Welcome, " + name);

        weatherService = new WeatherService(requireContext());
        weatherService.fetchWeather(
                address,
                temperature,
                feelsLike,
                humidity,
                wind,
                description,
                weatherLocation,
                weatherIcon
        );

        return view;
    }

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        return token != null && !token.isEmpty();
    }
}