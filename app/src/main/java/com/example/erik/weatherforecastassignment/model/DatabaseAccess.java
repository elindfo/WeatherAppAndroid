package com.example.erik.weatherforecastassignment.model;

import java.util.List;

public interface DatabaseAccess {

    void addWeatherForecasts(List<WeatherForecast> weatherForecasts);
    long findMaxApprovedTimeByLongitudeAndLatitude(double longitude, double latitude);
    List<WeatherForecast> findLatestForecastsByLongitudeAndLatitude(double longitude, double latitude);
}
