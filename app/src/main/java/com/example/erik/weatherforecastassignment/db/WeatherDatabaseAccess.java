package com.example.erik.weatherforecastassignment.db;

import android.arch.persistence.room.Room;
import android.util.Log;

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
        for(WeatherForecast weatherForecast : weatherForecasts){
            WeatherEntity weatherEntity = new WeatherEntity(
                    weatherForecast.getApprovedTime(),
                    weatherForecast.getValidTime(),
                    weatherForecast.getTValue(),
                    weatherForecast.getTccMeanValue(),
                    weatherForecast.getWsymb2(),
                    weatherForecast.getLongitude(),
                    weatherForecast.getLatitude()
            );
            weatherEntities.add(weatherEntity);
        }
        Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getWeatherForecasts: Adding " + weatherEntities.size() + " new WeatherEntities to database");
        weatherDatabase.weatherDao().insertAll(weatherEntities);
    }
}
