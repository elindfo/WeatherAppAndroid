package com.example.erik.weatherforecastassignment.db;

import android.arch.persistence.room.Room;

import com.example.erik.weatherforecastassignment.model.ApplicationContextProvider;
import com.example.erik.weatherforecastassignment.model.DatabaseAccess;
import com.example.erik.weatherforecastassignment.model.WeatherForecast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherDatabaseAccess implements DatabaseAccess {

    private static WeatherDatabaseAccess weatherDatabaseAccess;
    private WeatherDatabase weatherDatabase;

    private WeatherDatabaseAccess(){
        weatherDatabase = Room.databaseBuilder(
                ApplicationContextProvider.getContext(),
                WeatherDatabase.class,
                "weather_database").build();
    }

    public static WeatherDatabaseAccess getInstance(){
        if(weatherDatabaseAccess == null){
            weatherDatabaseAccess = new WeatherDatabaseAccess();
        }
        return weatherDatabaseAccess;
    }

    @Override
    public void addWeatherForecasts(List<WeatherForecast> weatherForecasts) {
        List<WeatherEntity> weatherEntities = new ArrayList<>();
        Date timestamp = new Date();
        for(WeatherForecast weatherForecast : weatherForecasts){
            WeatherEntity weatherEntity = new WeatherEntity(
                    weatherForecast.getApprovedTime(),
                    weatherForecast.getValidTime(),
                    weatherForecast.getName(),
                    weatherForecast.getValue(),
                    weatherForecast.getLongitude(),
                    weatherForecast.getLatitude(),
                    timestamp
            );
            weatherEntities.add(weatherEntity);
        }

        weatherDatabase.weatherDao().insertAll(weatherEntities);
    }
}
