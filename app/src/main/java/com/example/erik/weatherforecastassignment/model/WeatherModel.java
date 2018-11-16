package com.example.erik.weatherforecastassignment.model;

import android.util.Log;

import com.example.erik.weatherforecastassignment.db.WeatherDatabaseAccess;
import com.example.erik.weatherforecastassignment.smhi.Smhi;

import java.util.ArrayList;
import java.util.Date;
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
        //TODO Get from database if time < 1h, else get from SMHI api
        Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getWeatherForecasts: Fetching forecasts for lon " + lon + ", lat " + lat);
        List<WeatherForecast> weatherForecasts;
        Date lastApprovedTime = weatherDatabaseAccess.findLatestEntryTimeByLongitudeAndLatitude(lon, lat);
        lastApprovedTime = lastApprovedTime == null ? new Date(0) : lastApprovedTime;
        Date currentTime = new Date();
        Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getWeatherForecasts: time passed " + (currentTime.getTime() - lastApprovedTime.getTime()));
        if(currentTime.getTime() - lastApprovedTime.getTime() > 3600000){
            //If more than an hour has passed
            Log.d("WeatherForecastAssignment", this.getClass()
                            .getSimpleName() + ": getWeatherForecasts: More than an hour passed since last call to location. Fetching data from API");
            weatherForecasts = weatherProvider.getWeatherForecastsByCoord(lon, lat);

            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getWeatherForecasts: Attempting to add API fetched data to database");
            weatherDatabaseAccess.addWeatherForecasts(weatherForecasts);
        }
        else{
            Log.d("WeatherForecastAssignment", this.getClass()
                    .getSimpleName() + ": getWeatherForecasts: Less than an hour passed since last call to location. Fetching data from database");
            weatherForecasts = weatherDatabaseAccess.findLatestForecastsByLongitudeAndLatitude(lon, lat);
        }
        return weatherForecasts;
    }
}
