package com.example.smartkrishi.Responses;

public class WeatherResponse {
    private String city;
    private int temperature;
    private int feels_like;
    private int humidity;
    private double wind_speed;
    private String description;
    private String icon;

    public String getCity() { return city; }
    public int getTemperature() { return temperature; }
    public int getFeels_like() { return feels_like; }
    public int getHumidity() { return humidity; }
    public double getWind_speed() { return wind_speed; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }
}
