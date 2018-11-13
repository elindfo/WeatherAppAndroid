package com.example.erik.weatherforecastassignment.model;

import java.util.List;

public class WeatherForecast {

    private String approvedTime;
    private List<HourlyForecastData> hourlyForecastData;

    public WeatherForecast() {
    }

    public String getApprovedTime() {
        return approvedTime;
    }

    public List<HourlyForecastData> getHourlyForecastData() {
        return hourlyForecastData;
    }

    public void setApprovedTime(String approvedTime) {
        this.approvedTime = approvedTime;
    }

    public void setHourlyForecastData(List<HourlyForecastData> hourlyForecastData) {
        this.hourlyForecastData = hourlyForecastData;
    }
}
