package com.example.erik.weatherforecastassignment.model;

import java.util.List;

public class HourlyForecastData {

    private String validTime;
    private List<HourlyForecastDataParameters> hourlyForecastDataParameters;

    public HourlyForecastData() {}

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public List<HourlyForecastDataParameters> getHourlyForecastDataParameters() {
        return hourlyForecastDataParameters;
    }

    public void setHourlyForecastDataParameters(List<HourlyForecastDataParameters> hourlyForecastDataParameters) {
        this.hourlyForecastDataParameters = hourlyForecastDataParameters;
    }
}