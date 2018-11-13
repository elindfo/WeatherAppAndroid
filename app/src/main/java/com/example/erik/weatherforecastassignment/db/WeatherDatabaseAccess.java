package com.example.erik.weatherforecastassignment.db;

import android.arch.persistence.room.Room;

import com.example.erik.weatherforecastassignment.model.ApplicationContextProvider;
import com.example.erik.weatherforecastassignment.model.DatabaseAccess;
import com.example.erik.weatherforecastassignment.model.HourlyForecastData;
import com.example.erik.weatherforecastassignment.model.HourlyForecastDataParameters;
import com.example.erik.weatherforecastassignment.model.WeatherForecast;

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
    public void addWeatherData(WeatherForecast weatherForecast) {
        System.out.println("ADDWEATHERDATA");
        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.setApprovedTime(weatherForecast.getApprovedTime());
        for(HourlyForecastData hfd : weatherForecast.getHourlyForecastData()){
            for(HourlyForecastDataParameters hfdp : hfd.getHourlyForecastDataParameters()){
                if(hfdp.getName().equals("t")){
                    weatherEntity.setValidTime(hfd.getValidTime());
                    weatherEntity.setTemperature(hfdp.getValues()[0]);
                }
                if(hfdp.getName().equals("tcc_mean")){
                    weatherEntity.setTccMean(hfdp.getValues()[0]);
                }
            }
        }
        weatherDatabase.weatherDao().insertAll(weatherEntity);
    }
}
