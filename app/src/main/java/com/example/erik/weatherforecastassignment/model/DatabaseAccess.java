package com.example.erik.weatherforecastassignment.model;

import java.util.List;

public interface DatabaseAccess {

    void addWeatherForecasts(List<WeatherForecast> weatherForecasts);
}
