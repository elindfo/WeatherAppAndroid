package com.example.erik.weatherforecastassignment.model;

import java.util.List;

public interface WeatherProvider {

    List<WeatherForecast> getWeatherForecastsByCoord(double lon, double lat);
    List<Place> getPlaceData(String place);
}
