package com.example.smartkrishi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.smartkrishi.R;
import com.example.smartkrishi.utils.WeatherService;

public class HomeFragment extends Fragment {
    private TextView weatherInfo, weatherLocation;
    private WeatherService weatherService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        // Initialize views
        weatherInfo = view.findViewById(R.id.weatherInfo);
        weatherLocation = view.findViewById(R.id.weather_location);

        // Initialize WeatherService
        weatherService = new WeatherService(requireContext());
        weatherService.fetchWeather("Kathmandu", weatherInfo, weatherLocation);

        return view;
    }
}