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

    public List<WeatherForecast> getWeatherForecastsByCoordinates(double lon, double lat){
        //TODO Get from database if time < 1h, else get from SMHI api
        Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getWeatherForecastsByCoordinates: Fetching forecasts for lon " + lon + ", lat " + lat);
        List<WeatherForecast> weatherForecasts = weatherProvider.getWeatherForecastsByCoord(lon, lat);
        weatherDatabaseAccess.deleteAndInsertAll(weatherForecasts);
        return weatherForecasts;
    }

    public List<WeatherForecast> getLastUpdatedWeatherForecasts(NetworkStatus.Status status) {

        Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getLastUpdatedWeatherForecasts: Fetching forecasts");

        Date latestEntryTime = weatherDatabaseAccess.findLatestEntryTime();
        latestEntryTime = latestEntryTime == null ? new Date(0) : latestEntryTime;
        long latestEntryTimeInMillis = latestEntryTime.getTime();

        if(latestEntryTimeInMillis <= 0){ //No earlier search made so wont be able to update from API or DB
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getLastUpdatedWeatherForecasts: No earlier search made, returning 0 forecasts");
            return new ArrayList<>();
        }

        long currentTimeInMillis = new Date().getTime();
        long timeLimit = 0;
        switch(status){
            case WIFI:{ //Older than 10 minues
                timeLimit = 600000;
                Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getLastUpdatedWeatherForecasts: On WIFI, timelimit set to " + timeLimit);
                break;
            }
            case MOBILE:{ //Older than 60 minutes
                timeLimit = 3600000;
                Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getLastUpdatedWeatherForecasts: On MOBILE, timelimit set to " + timeLimit);
                break;
            }
            case NO_CONNECTION:{ //Unable to fetch, if no stored data, return empty array
                return weatherDatabaseAccess.getAll();
            }
        }
        Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getLastUpdatedWeatherForecasts: Time difference: " + (currentTimeInMillis - latestEntryTimeInMillis));
        if(currentTimeInMillis - latestEntryTimeInMillis > timeLimit){
            Log.d("WeatherForecastAssignment", this.getClass()
                    .getSimpleName() + ": getLastUpdatedWeatherForecasts: Timelimit " + timeLimit / 60000 + " minutes exceeded, fetching data from API");
            WeatherForecast weatherForecast = weatherDatabaseAccess.getLast();
            List<WeatherForecast> weatherForecasts = weatherProvider.getWeatherForecastsByCoord(weatherForecast.getLongitude(), weatherForecast.getLatitude());
            weatherDatabaseAccess.deleteAndInsertAll(weatherForecasts);
            return weatherForecasts;
        }
        else{
            Log.d("WeatherForecastAssignment", this.getClass()
                    .getSimpleName() + ": getWeatherForecastsByCoordinates: Timelimit " + timeLimit / 60000 + " minutes NOT exceeded, fetching data from database");
            return weatherDatabaseAccess.getAll();
        }
    }
}
