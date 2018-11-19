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

    @Override
    public Date findLastEntryTime() {
        Log.d(WeatherDatabaseAccess.TAG, this.getClass().getSimpleName() + ": findLastEntryTime");
        return weatherDatabase.weatherDao().findLatestEntryTime();
    }

    @Override
    public void deleteAndInsertAll(List<WeatherForecast> weatherForecasts) {
        Log.d(WeatherDatabaseAccess.TAG, this.getClass().getSimpleName() + ": deleteAndInsertAll");
        weatherDatabase.weatherDao().deleteAndInsertAll(convertFromWeatherForecastList(weatherForecasts));
    }

    @Override
    public List<WeatherForecast> getAllForecasts() {
        Log.d(WeatherDatabaseAccess.TAG, this.getClass().getSimpleName() + ": getAllForecasts");
        List<WeatherEntity> weatherEntities = weatherDatabase.weatherDao().findAll();
        return convertFromWeatherEntityList(weatherEntities);
    }

    @Override
    public WeatherForecast getLast() {
        Log.d(WeatherDatabaseAccess.TAG, this.getClass().getSimpleName() + ": getLast");
        return convertFromWeatherEntity(weatherDatabase.weatherDao().getEntry());
    }

    @Override
    public boolean isFavorite(Place place) {
        boolean isFavourite = weatherDatabase.favouriteDao().exists(place.getGeonameId()) > 0;
        Log.d(WeatherDatabaseAccess.TAG, this.getClass().getSimpleName() + ": isFavourite: " + isFavourite);
        return isFavourite;
    }

    @Override
    public boolean addFavorite(Place place) {
        boolean isAdded = weatherDatabase.favouriteDao().insert(convertFromPlace(place)) > 0;
        Log.d(WeatherDatabaseAccess.TAG, this.getClass().getSimpleName() + ": addFavourite: " + isAdded);
        return isAdded;
    }

    @Override
    public void removeFavorite(Place place) {
        Log.d(WeatherDatabaseAccess.TAG, this.getClass().getSimpleName() + ": removeFavorite");
        weatherDatabase.favouriteDao().delete(convertFromPlace(place));
    }

    @Override
    public List<Place> getFavorites() {
        Log.d(WeatherDatabaseAccess.TAG, this.getClass().getSimpleName() + ": getFavorites");
        return convertFromFavouriteEntityList(weatherDatabase.favouriteDao().findAll());
    }

    private List<Place> convertFromFavouriteEntityList(List<FavouriteEntity> favouriteEntities){
        List<Place> places = new ArrayList<>();
        for(FavouriteEntity favouriteEntity : favouriteEntities){
            places.add(convertFromFavouriteEntity(favouriteEntity));
        }
        return places;
    }

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
