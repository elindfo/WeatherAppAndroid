package com.example.erik.weatherforecastassignment.model;

import java.util.Date;
import java.util.List;

public interface DatabaseAccess {

    void addWeatherForecasts(List<WeatherForecast> weatherForecasts);
    Date findLatestEntryTimeByLongitudeAndLatitude(double longitude, double latitude);
    List<WeatherForecast> findLatestForecastsByLongitudeAndLatitude(double longitude, double latitude);
    void deleteAndInsertAll(List<WeatherForecast> weatherForecasts);
}
