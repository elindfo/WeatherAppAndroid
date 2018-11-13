package com.example.erik.weatherforecastassignment.model;

import com.example.erik.weatherforecastassignment.smhi.Smhi;

import java.util.List;

public class WeatherHandler {

    private static WeatherHandler weatherHandler;

    private WeatherProvider weatherProvider;

    private WeatherHandler(){
        weatherProvider = Smhi.getInstance();
    }

    public static WeatherHandler getInstance(){
        if(weatherHandler == null){
            weatherHandler = new WeatherHandler();
        }
        return weatherHandler;
    }

    public List<WeatherForecast> getWeatherForecasts(double lon, double lat){
        return weatherProvider.getWeatherForecastsByCoord(lon, lat);
    }
}
