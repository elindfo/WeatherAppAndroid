package com.example.erik.weatherforecastassignment.model;

import java.util.List;

/**
 * Interface defining the methods used by the implementing WeatherProvider
 */
public interface WeatherProvider {
    List<WeatherForecast> getWeatherForecastsByCoord(double lon, double lat);
    List<Place> getPlaceData(String place);
}
