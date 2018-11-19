package com.example.erik.weatherforecastassignment.model;

import android.util.Log;

import com.example.erik.weatherforecastassignment.db.WeatherDatabaseAccess;
import com.example.erik.weatherforecastassignment.smhi.Smhi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherModel {

    public static final String TAG = "WeatherForecastAssignment";

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

    public List<WeatherForecast> getLastUpdatedWeatherForecasts(NetworkStatus.Status status) {
        Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": getLastUpdatedWeatherForecasts: fetching forecasts");

        Date latestEntryTime = weatherDatabaseAccess.findLastEntryTime();
        latestEntryTime = latestEntryTime == null ? new Date(0) : latestEntryTime;
        long latestEntryTimeInMillis = latestEntryTime.getTime();

        if(latestEntryTimeInMillis <= 0){ //No earlier search made so wont be able to update from API or DB
            Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": getLastUpdatedWeatherForecasts: no earlier search made, returning 0 forecasts");
            return new ArrayList<>();
        }

        long currentTimeInMillis = new Date().getTime();
        long timeLimit = 0;
        switch(status){
            case WIFI:{ //Older than 10 minues
                timeLimit = 600000;
                Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": getLastUpdatedWeatherForecasts: on WIFI, timelimit set to " + timeLimit);
                break;
            }
            case MOBILE:{ //Older than 60 minutes
                timeLimit = 3600000;
                Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": getLastUpdatedWeatherForecasts: on MOBILE, timelimit set to " + timeLimit);
                break;
            }
            case NO_CONNECTION:{ //Unable to fetch, if no stored data, return empty array
                return weatherDatabaseAccess.getAllForecasts();
            }
        }
        Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": getLastUpdatedWeatherForecasts: time difference: " + (currentTimeInMillis - latestEntryTimeInMillis));
        if(currentTimeInMillis - latestEntryTimeInMillis > timeLimit){
            Log.d(WeatherModel.TAG, this.getClass()
                    .getSimpleName() + ": getLastUpdatedWeatherForecasts: timelimit " + timeLimit / 60000 + " minutes exceeded, fetching data from API");
            WeatherForecast weatherForecast = weatherDatabaseAccess.getLast();
            List<WeatherForecast> weatherForecasts = weatherProvider.getWeatherForecastsByCoord(weatherForecast.getLongitude(), weatherForecast.getLatitude());
            Log.d(WeatherModel.TAG, this.getClass()
                    .getSimpleName() + ": getLastUpdatedWeatherForecasts: fetched " + weatherForecasts.size() + " new forecasts for location " + weatherForecast.getPlace());
            for(WeatherForecast wf : weatherForecasts){
                wf.setPlace(weatherForecast.getPlace());
            }
            weatherDatabaseAccess.deleteAndInsertAll(weatherForecasts);
            return weatherForecasts;
        }
        else{
            Log.d(WeatherModel.TAG, this.getClass()
                    .getSimpleName() + ": getLastUpdatedWeatherForecasts: timelimit " + timeLimit / 60000 + " minutes NOT exceeded, fetching data from database");
            return weatherDatabaseAccess.getAllForecasts();
        }
    }

    public void setWeatherForecastsByPlace(Place place) {
        Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": setWeatherForecastsByPlace: fetching forecasts for " + place.getPlace());
        List<WeatherForecast> weatherForecasts = weatherProvider.getWeatherForecastsByCoord(place.getLongitude(), place.getLatitude());
        if(weatherForecasts != null && weatherForecasts.size() > 0){
            Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": getWeatherForecastsByPlace: storing " + weatherForecasts.size() + " new forecasts.");
            for(WeatherForecast wf : weatherForecasts){
                wf.setPlace(place.getPlace());
            }
            weatherDatabaseAccess.deleteAndInsertAll(weatherForecasts);
        }
        else{
            Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": getWeatherForecastsByPlace: no forecasts found for location");
        }
    }

    public List<Place> getPlaces(String place) {
        Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": getPlaces: " + place);
        return weatherProvider.getPlaceData(place);
    }

    public boolean isFavorite(Place place){
        Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": isFavorite: " + place.getPlace());
        return weatherDatabaseAccess.isFavorite(place);
    }

    public boolean addFavorite(Place place){
        Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": addFavorite: " + place.getPlace());
        return weatherDatabaseAccess.addFavorite(place);
    }

    public void removeFavorite(Place place){
        Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": removeFavorite: " + place.getPlace());
        weatherDatabaseAccess.removeFavorite(place);
    }

    public List<Place> getFavorites() {
        Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": getFavorites");
        return weatherDatabaseAccess.getFavorites();
    }
}
