package com.example.erik.weatherforecastassignment.model;

import com.example.erik.weatherforecastassignment.smhi.Smhi;

public class WeatherForecast {

    private String approvedTime;

    public WeatherForecast(String approvedTime) {
        this.approvedTime = approvedTime;
    }

    public String getApprovedTime() {
        return approvedTime;
    }
}
