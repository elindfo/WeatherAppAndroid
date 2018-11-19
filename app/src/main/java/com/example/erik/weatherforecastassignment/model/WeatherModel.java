package com.example.erik.weatherforecastassignment.model;

import android.util.Log;

import com.example.erik.weatherforecastassignment.db.WeatherDatabaseAccess;
import com.example.erik.weatherforecastassignment.smhi.Smhi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is the main singleton model class that is used for correspondence between the view, API and
 * database.
 */
public class WeatherModel {

    public static final String TAG = "WeatherForecastAssignment";
    private static final long MILLIS_PER_HOUR = 3600000;
    private static final long MILLIS_PER_10_MINUTES = MILLIS_PER_HOUR / 6;

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

    /**
     * This method has logic that either checks the database or makes a new request to the API
     * depending on current network status and time since last weather data was fetched and from
     * what place. If weather data is accessible from the database, it may use those coordinates
     * for a new API request.
     * @param status Current network status
     * @return Empty list of WeatherForecast if no previous weather data can be found in database.
     *         List of cached WeatherForecast if the time limit for this connection type has not
     *         been reached.
     *         List of API received WeatherForecast if the time limit has been exceeded.
     */
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
                timeLimit = MILLIS_PER_10_MINUTES;
                Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": getLastUpdatedWeatherForecasts: on WIFI, timelimit set to " + timeLimit);
                break;
            }
            case MOBILE:{ //Older than 60 minutes
                timeLimit = MILLIS_PER_HOUR;
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

    /**
     * This method will fetch new weather forecast data from the API and update the database.
     * @param place The Place used in the weather forecast search
     */
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

    /**
     * This method will fetch place data form the API.
     * @param place The Place name used in the search
     * @return List of found Place
     */
    public List<Place> getPlaces(String place) {
        Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": getPlaces: " + place);
        return weatherProvider.getPlaceData(place);
    }

    /**
     * This method will check if a Place is currently in the favorite list
     * @param place The Place used in the check
     * @return True if the Place is in the favorite list, false if not
     */
    public boolean isFavorite(Place place){
        Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": isFavorite: " + place.getPlace());
        return weatherDatabaseAccess.isFavorite(place);
    }

    /**
     * This method will add a Place to the favorite list.
     * @param place The Place to be added
     * @return True if the Place was added to the favorite list, false if not
     */
    public boolean addFavorite(Place place){
        Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": addFavorite: " + place.getPlace());
        return weatherDatabaseAccess.addFavorite(place);
    }

    /**
     * This method will remove a Place from the favorite list.
     * @param place The Place to be removed
     */
    public void removeFavorite(Place place){
        Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": removeFavorite: " + place.getPlace());
        weatherDatabaseAccess.removeFavorite(place);
    }

    /**
     * This method will find all Places currently in the favorite list.
     * @return List of Place
     */
    public List<Place> getFavorites() {
        Log.d(WeatherModel.TAG, this.getClass().getSimpleName() + ": getFavorites");
        return weatherDatabaseAccess.getFavorites();
    }
}
