package com.example.erik.weatherforecastassignment.db;

import android.arch.persistence.room.Room;
import android.util.Log;

import com.example.erik.weatherforecastassignment.model.ApplicationContextProvider;
import com.example.erik.weatherforecastassignment.model.DatabaseAccess;
import com.example.erik.weatherforecastassignment.model.StringDateTool;
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
        Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": addWeatherForecasts: Adding " + weatherForecasts.size() + " new WeatherEntities to database");
        weatherDatabase.weatherDao().insertAll(convertFromWeatherForecastList(weatherForecasts));
    }

    @Override
    public Date findLatestEntryTime() {
        return weatherDatabase.weatherDao().findLatestEntryTime();
    }

    @Override
    public List<WeatherForecast> findLatestForecastsByLongitudeAndLatitude(double longitude, double latitude) {
        List<WeatherEntity> weatherEntities = weatherDatabase.weatherDao().findLatestForecastsByLongitudeAndLatitude(longitude, latitude);
        return convertFromWeatherEntityList(weatherEntities);
    }

    @Override
    public void deleteAndInsertAll(List<WeatherForecast> weatherForecasts) {
        Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": deleteAndInsertAll: Deleting and inserting forecasts");
        weatherDatabase.weatherDao().deleteAndInsertAll(convertFromWeatherForecastList(weatherForecasts));
    }

    @Override
    public List<WeatherForecast> getAll() {
        List<WeatherEntity> weatherEntities = weatherDatabase.weatherDao().findAll();
        return convertFromWeatherEntityList(weatherEntities);
    }

    @Override
    public WeatherForecast getLast() {
        return convertFromWeatherEntity(weatherDatabase.weatherDao().getEntry());
    }

    @Override
    public Date findLatestEntryTimeByLongitudeAndLatitude(double lon, double lat) {
        return weatherDatabase.weatherDao().findLatestEntryTimeLongitudeAndLatitude(lon, lat);
    }

    private WeatherForecast convertFromWeatherEntity(WeatherEntity weatherEntity){
        WeatherForecast wf = new WeatherForecast();
        wf.setPlace(weatherEntity.getPlace());
        wf.setApprovedTime(StringDateTool.getDateFromISO8601String(weatherEntity.getApprovedTime()));
        wf.setValidTime(StringDateTool.getDateFromISO8601String(weatherEntity.getValidTime()));
        wf.setTValue(weatherEntity.getTValue());
        wf.setTccMeanValue(weatherEntity.getTccMeanValue());
        wf.setWsymb2(weatherEntity.getWsymb2());
        wf.setLongitude(weatherEntity.getLongitude());
        wf.setLatitude(weatherEntity.getLatitude());
        return wf;
    }

    private List<WeatherEntity> convertFromWeatherForecastList(List<WeatherForecast> weatherForecasts){
        List<WeatherEntity> weatherEntities = new ArrayList<>();
        Date date = new Date();
        for(WeatherForecast weatherForecast : weatherForecasts){
            WeatherEntity weatherEntity = new WeatherEntity(
                    weatherForecast.getPlace(),
                    StringDateTool.getISO8601StringFromDate(weatherForecast.getApprovedTime()),
                    StringDateTool.getISO8601StringFromDate(weatherForecast.getValidTime()),
                    weatherForecast.getTValue(),
                    weatherForecast.getTccMeanValue(),
                    weatherForecast.getWsymb2(),
                    weatherForecast.getLongitude(),
                    weatherForecast.getLatitude(),
                    date
            );
            weatherEntities.add(weatherEntity);
        }
        return weatherEntities;
    }

    private List<WeatherForecast> convertFromWeatherEntityList(List<WeatherEntity> weatherEntities){
        List<WeatherForecast> weatherForecasts = new ArrayList<>();
        for(WeatherEntity we : weatherEntities){
            weatherForecasts.add(convertFromWeatherEntity(we));
        }
        return weatherForecasts;
    }
}
