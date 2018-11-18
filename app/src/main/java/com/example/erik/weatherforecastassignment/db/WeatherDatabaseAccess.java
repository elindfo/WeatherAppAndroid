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
    public Date findLatestEntryTime() {
        return weatherDatabase.weatherDao().findLatestEntryTime();
    }

    @Override
    public void deleteAndInsertAll(List<WeatherForecast> weatherForecasts) {
        Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": deleteAndInsertAll: Deleting and inserting forecasts");
        weatherDatabase.weatherDao().deleteAndInsertAll(convertFromWeatherForecastList(weatherForecasts));
    }

    @Override
    public List<WeatherForecast> getAllForecasts() {
        List<WeatherEntity> weatherEntities = weatherDatabase.weatherDao().findAll();
        return convertFromWeatherEntityList(weatherEntities);
    }

    @Override
    public WeatherForecast getLast() {
        return convertFromWeatherEntity(weatherDatabase.weatherDao().getEntry());
    }

    @Override
    public boolean isFavourite(Place place) {
        return weatherDatabase.favouriteDao().exists(place.getGeonameId()) > 0;
    }

    @Override
    public boolean addFavourite(Place place) {
        return weatherDatabase.favouriteDao().insert(convertFromPlace(place)) > 0;
    }

    @Override
    public void removeFavourite(Place place) {
        weatherDatabase.favouriteDao().delete(convertFromPlace(place));
    }

    @Override
    public List<Place> getFavourites() {
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
