package com.example.erik.weatherforecastassignment.model;

import java.util.List;

public interface WeatherProvider {

    WeatherForecast getWeatherForecastByCoord(double lon, double lat);
}
