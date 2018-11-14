package com.example.erik.weatherforecastassignment.model;

import com.example.erik.weatherforecastassignment.db.WeatherDatabaseAccess;
import com.example.erik.weatherforecastassignment.smhi.Smhi;

import java.util.List;

public class WeatherModel {

    private static WeatherModel weatherModel;

    private WeatherProvider weatherProvider;
    private WeatherDatabaseAccess weatherDatabaseAccess;

    private WeatherModel(){
        weatherProvider = Smhi.getInstance();
        weatherDatabaseAccess = WeatherDatabaseAccess.getInstance();
    }

    public static WeatherModel getInstance(){
        if(weatherModel == null){
            weatherModel = new WeatherModel();
        }
        return weatherModel;
    }

    public List<WeatherForecast> getWeatherForecasts(double lon, double lat){
        List<WeatherForecast> weatherForecasts = weatherProvider.getWeatherForecastsByCoord(lon, lat);
        if(weatherForecasts.size() > 0){
            weatherDatabaseAccess.addWeatherForecasts(weatherForecasts);
        }
        return weatherForecasts;
    }
}
