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
                return weatherDatabaseAccess.getAllForecasts();
            }
        }
        Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getLastUpdatedWeatherForecasts: Time difference: " + (currentTimeInMillis - latestEntryTimeInMillis));
        if(currentTimeInMillis - latestEntryTimeInMillis > timeLimit){
            Log.d("WeatherForecastAssignment", this.getClass()
                    .getSimpleName() + ": getLastUpdatedWeatherForecasts: Timelimit " + timeLimit / 60000 + " minutes exceeded, fetching data from API");
            WeatherForecast weatherForecast = weatherDatabaseAccess.getLast();
            List<WeatherForecast> weatherForecasts = weatherProvider.getWeatherForecastsByCoord(weatherForecast.getLongitude(), weatherForecast.getLatitude());
            Log.d("WeatherForecastAssignment", this.getClass()
                    .getSimpleName() + ": getLastUpdatedWeatherForecasts: Fetched " + weatherForecasts.size() + " new forecasts for location " + weatherForecast.getPlace());
            for(WeatherForecast wf : weatherForecasts){
                wf.setPlace(weatherForecast.getPlace());
            }
            weatherDatabaseAccess.deleteAndInsertAll(weatherForecasts);
            return weatherForecasts;
        }
        else{
            Log.d("WeatherForecastAssignment", this.getClass()
                    .getSimpleName() + ": getLastUpdatedWeatherForecasts: Timelimit " + timeLimit / 60000 + " minutes NOT exceeded, fetching data from database");
            List<WeatherForecast> weatherForecasts = weatherDatabaseAccess.getAllForecasts();
            for(WeatherForecast wf : weatherForecasts){
                Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getWeatherForecastsByCoordinates: Place fetched: " + wf.getPlace());
            }
            return weatherDatabaseAccess.getAllForecasts();
        }
    }

    public List<WeatherForecast> getWeatherForecastsByPlace(String place) {
        Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getWeatherForecastsByPlace: Fetching forecasts for " + place);
        List<Place> places = weatherProvider.getPlaceData(place);
        if(places != null && places.size() > 0){
            List<WeatherForecast> weatherForecasts = weatherProvider.getWeatherForecastsByCoord(places.get(0).getLongitude(), places.get(0).getLatitude());
            if(weatherForecasts != null && weatherForecasts.size() > 0){
                Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getWeatherForecastsByCoordinates: Storing " + weatherForecasts.size() + " new forecasts.");
                //TODO ADD PLACE IN FORECAST
                for(WeatherForecast wf : weatherForecasts){
                    wf.setPlace(places.get(0).getPlace());
                }
                weatherDatabaseAccess.deleteAndInsertAll(weatherForecasts);
            }
            else{
                Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getWeatherForecastsByCoordinates: No forecasts found for location");
            }
            return weatherForecasts;
        }
        return new ArrayList<>();
    }

    public void setWeatherForecastsByPlace(Place place) {
        Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": setWeatherForecastsByPlace: Fetching forecasts for " + place.getPlace());
        List<WeatherForecast> weatherForecasts = weatherProvider.getWeatherForecastsByCoord(place.getLongitude(), place.getLatitude());
        if(weatherForecasts != null && weatherForecasts.size() > 0){
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getWeatherForecastsByPlace: Storing " + weatherForecasts.size() + " new forecasts.");
            for(WeatherForecast wf : weatherForecasts){
                wf.setPlace(place.getPlace());
            }
            weatherDatabaseAccess.deleteAndInsertAll(weatherForecasts);
        }
        else{
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": getWeatherForecastsByPlace: No forecasts found for location");
        }
    }

    public List<Place> getPlaces(String place) {
        return weatherProvider.getPlaceData(place);
    }

    public boolean isFavourite(Place place){
        return weatherDatabaseAccess.isFavourite(place);
    }

    public boolean addFavourite(Place place){
        return weatherDatabaseAccess.addFavourite(place);
    }

    public void removeFavourite(Place place){
        weatherDatabaseAccess.removeFavourite(place);
    }

    public List<Place> getFavourites() {
        return weatherDatabaseAccess.getFavourites();
    }
}
