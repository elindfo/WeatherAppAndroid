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
                    weatherForecast.getLatitude(),
                    new Date()
            );
            weatherEntities.add(weatherEntity);
        }
        Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getWeatherForecasts: Adding " + weatherEntities.size() + " new WeatherEntities to database");
        weatherDatabase.weatherDao().insertAll(weatherEntities);
    }

    @Override
    public Date findLatestEntryTimeByLongitudeAndLatitude(double longitude, double latitude) {
        return weatherDatabase.weatherDao().findLatestEntryTimeByLongitudeAndLatitude(longitude, latitude);
    }

    @Override
    public List<WeatherForecast> findLatestForecastsByLongitudeAndLatitude(double longitude, double latitude) {
        List<WeatherEntity> weatherEntities = weatherDatabase.weatherDao().findLatestForecastsByLongitudeAndLatitude(longitude, latitude);
        return convertFromWeatherEnityList(weatherEntities);
    }

    private List<WeatherForecast> convertFromWeatherEnityList(List<WeatherEntity> weatherEntities){
        List<WeatherForecast> weatherForecasts = new ArrayList<>();
        for(WeatherEntity we : weatherEntities){
            WeatherForecast wf = new WeatherForecast();
            wf.setApprovedTime(we.getApprovedTime());
            wf.setValidTime(we.getValidTime());
            wf.setTValue(we.getTValue());
            wf.setTccMeanValue(we.getTccMeanValue());
            wf.setWsymb2(we.getWsymb2());
            wf.setLongitude(we.getLongitude());
            wf.setLatitude(we.getLatitude());
            weatherForecasts.add(wf);
        }
        return weatherForecasts;
    }
}
