package com.example.erik.weatherforecastassignment.model;

import android.util.Log;

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
        Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getWeatherForecasts: Fetching forecasts for lon " + lon + ", lat " + lat);
        List<WeatherForecast> weatherForecasts = weatherProvider.getWeatherForecastsByCoord(lon, lat);
        if(weatherForecasts != null){
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getWeatherForecasts: Attempting to add to database");
            weatherDatabaseAccess.addWeatherForecasts(weatherForecasts);
        }
        return weatherForecasts;
    }
}
