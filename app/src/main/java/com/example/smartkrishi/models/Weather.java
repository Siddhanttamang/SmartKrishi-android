package com.example.smartkrishi.models;



public class Weather {
    private String temperature;
    private String description;
    private String date;

    public Weather(String temperature, String description, String date) {
        this.temperature = temperature;
        this.description = description;
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
