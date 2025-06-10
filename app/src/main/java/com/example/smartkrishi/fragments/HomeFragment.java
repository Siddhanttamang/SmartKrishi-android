package com.example.smartkrishi.fragments;

import android.content.SharedPreferences;
import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.smartkrishi.R;
import com.example.smartkrishi.Responses.UserLoginResponse;
import com.example.smartkrishi.Services.WeatherService;
import com.google.gson.Gson;

public class HomeFragment extends Fragment {
    private TextView weatherInfo, weatherLocation;
    private WeatherService weatherService;
    private TextView userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        // Initialize views
        userName= view.findViewById(R.id.user_name);
        weatherInfo = view.findViewById(R.id.weatherInfo);
        weatherLocation = view.findViewById(R.id.weather_location);

        SharedPreferences prefs = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String userJson = prefs.getString("user_data", null);




        // Initialize WeatherService
        weatherService = new WeatherService(requireContext());
        if (userJson != null) {
            Gson gson = new Gson();
            UserLoginResponse.UserData user = gson.fromJson(userJson, UserLoginResponse.UserData.class);
            String name = user.getName(); // Use safely now
            String address= user.getAddress();
            userName.setText("Welcome, "+name);
            weatherService.fetchWeather(address, weatherInfo, weatherLocation);
        }

        return view;
    }
}