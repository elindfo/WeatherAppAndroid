package com.example.erik.weatherforecastassignment.db;

import android.arch.persistence.room.Room;
import android.util.Log;

import com.example.erik.weatherforecastassignment.model.ApplicationContextProvider;
import com.example.erik.weatherforecastassignment.model.DatabaseAccess;
import com.example.erik.weatherforecastassignment.model.Place;
import com.example.erik.weatherforecastassignment.model.StringDateTool;
import com.example.erik.weatherforecastassignment.model.WeatherForecast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This singleton class handles database operations and has methods used
 * to fetch, update, delete and add data to the database. The methods are provided
 * by the implemented DatabaseAccess interface.
 */
public class WeatherDatabaseAccess implements DatabaseAccess {

    public static final String TAG = "WeatherForecastAssignment";

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

    /**
     * Used to fetch the time of the last database entry.
     * @return Date object with the time
     */
    @Override
    public Date findLastEntryTime() {
        Log.d(WeatherDatabaseAccess.TAG, this.getClass().getSimpleName() + ": findLastEntryTime");
        return weatherDatabase.weatherDao().findLatestEntryTime();
    }

    /**
     * Used when weather data is to be replaced in the database.
     * @param weatherForecasts List of new WeatherForecasts to be stored
     */
    @Override
    public void deleteAndInsertAll(List<WeatherForecast> weatherForecasts) {
        Log.d(WeatherDatabaseAccess.TAG, this.getClass().getSimpleName() + ": deleteAndInsertAll");
        weatherDatabase.weatherDao().deleteAndInsertAll(convertFromWeatherForecastList(weatherForecasts));
    }

    /**
     * Used to fetch all currently stored WeatherForecast objects.
     * @return List of WeatherForecast
     */
    @Override
    public List<WeatherForecast> getAllForecasts() {
        Log.d(WeatherDatabaseAccess.TAG, this.getClass().getSimpleName() + ": getAllForecasts");
        List<WeatherEntity> weatherEntities = weatherDatabase.weatherDao().findAll();
        return convertFromWeatherEntityList(weatherEntities);
    }

    /**
     * Fetches the last WeatherForecast object to be inserted. Is used only to get the coordinates
     * of the previous stored data.
     * @return
     */
    @Override
    public WeatherForecast getLast() {
        Log.d(WeatherDatabaseAccess.TAG, this.getClass().getSimpleName() + ": getLast");
        return convertFromWeatherEntity(weatherDatabase.weatherDao().getEntry());
    }

    /**
     * Checks whether a Place is in the favorites list.
     * @param place The Place to be checked
     * @return true if the place is in the favorites list, false if not
     */
    @Override
    public boolean isFavorite(Place place) {
        boolean isFavourite = weatherDatabase.favouriteDao().exists(place.getGeonameId()) > 0;
        Log.d(WeatherDatabaseAccess.TAG, this.getClass().getSimpleName() + ": isFavourite: " + isFavourite);
        return isFavourite;
    }

    /**
     * Attempts to add a Place to the favorites list.
     * @param place The Place to be added
     * @return true if the place was added to the favorites list, false if not
     */
    @Override
    public boolean addFavorite(Place place) {
        boolean isAdded = weatherDatabase.favouriteDao().insert(convertFromPlace(place)) > 0;
        Log.d(WeatherDatabaseAccess.TAG, this.getClass().getSimpleName() + ": addFavourite: " + isAdded);
        return isAdded;
    }

    /**
     * Attempts to remove a Place from the favorites list.
     * @param place The Place to be removed
     */
    @Override
    public void removeFavorite(Place place) {
        Log.d(WeatherDatabaseAccess.TAG, this.getClass().getSimpleName() + ": removeFavorite");
        weatherDatabase.favouriteDao().delete(convertFromPlace(place));
    }

    /**
     * Fetches all Places stored in the favorites list.
     * @return List of Place
     */
    @Override
    public List<Place> getFavorites() {
        Log.d(WeatherDatabaseAccess.TAG, this.getClass().getSimpleName() + ": getFavorites");
        return convertFromFavouriteEntityList(weatherDatabase.favouriteDao().findAll());
    }

    /**
     * Private method used to convert List of FavoriteEntity to List of Place.
     * @param favouriteEntities The List to be converted
     * @return List of Place
     */
    private List<Place> convertFromFavouriteEntityList(List<FavouriteEntity> favouriteEntities){
        List<Place> places = new ArrayList<>();
        for(FavouriteEntity favouriteEntity : favouriteEntities){
            places.add(convertFromFavouriteEntity(favouriteEntity));
        }
        return places;
    }

    /**
     * Private method used to convert List of WeatherForecast to List of WeatherEntity.
     * @param weatherForecasts The List to be converted
     * @return List of WeatherEntity
     */
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


    /**
     * Private method used to convert List of WeatherEntity to List of WeatherForecast.
     * @param weatherEntities The List to be converted
     * @return List of WeatherForecast
     */
    private List<WeatherForecast> convertFromWeatherEntityList(List<WeatherEntity> weatherEntities){
        List<WeatherForecast> weatherForecasts = new ArrayList<>();
        for(WeatherEntity we : weatherEntities){
            weatherForecasts.add(convertFromWeatherEntity(we));
        }
        return weatherForecasts;
    }

    /**
     * Private method used to convert from FavoriteEntity to Place.
     * @param favouriteEntity The object to be converted
     * @return Place
     */
    private Place convertFromFavouriteEntity(FavouriteEntity favouriteEntity){
        Place place = new Place(
                favouriteEntity.getId(),
                favouriteEntity.getPlace(),
                favouriteEntity.getMunicipality(),
                favouriteEntity.getCounty(),
                favouriteEntity.getLongitude(),
                favouriteEntity.getLatitude()
        );
        return place;
    }

    /**
     * Private method used to convert from Place to FavoriteEntity.
     * @param place The object to be converted
     * @return FavoriteEntity
     */
    private FavouriteEntity convertFromPlace(Place place){
        FavouriteEntity favouriteEntity = new FavouriteEntity(
                place.getGeonameId(),
                place.getPlace(),
                place.getMunicipality(),
                place.getCounty(),
                place.getLongitude(),
                place.getLatitude()
        );
        return favouriteEntity;
    }

    /**
     * Private method used to convert from WeatherEntity to WeatherForecast.
     * @param weatherEntity The object to be converted
     * @return WeatherForecast
     */
    private WeatherForecast convertFromWeatherEntity(WeatherEntity weatherEntity){
        WeatherForecast wf = new WeatherForecast(
                StringDateTool.getDateFromISO8601String(weatherEntity.getApprovedTime()),
                StringDateTool.getDateFromISO8601String(weatherEntity.getValidTime()),
                weatherEntity.getTValue(),
                weatherEntity.getTccMeanValue(),
                weatherEntity.getWsymb2(),
                weatherEntity.getLongitude(),
                weatherEntity.getLatitude(),
                weatherEntity.getPlace()
        );
        return wf;
    }




}
