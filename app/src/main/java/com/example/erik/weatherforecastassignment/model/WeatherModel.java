package com.example.erik.weatherforecastassignment.model;

import com.example.erik.weatherforecastassignment.db.WeatherDatabaseAccess;
import com.example.erik.weatherforecastassignment.smhi.Smhi;

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

    public WeatherForecast getWeatherForecast(double lon, double lat){
        WeatherForecast weatherForecast = weatherProvider.getWeatherForecastByCoord(lon, lat);
        weatherDatabaseAccess.addWeatherData(weatherForecast);
        return weatherForecast;
    }
}
