package com.example.erik.weatherforecastassignment.model;

import java.util.Date;
import java.util.List;

/**
 * Interface defining the methods used by the implementing Database provider
 */
public interface DatabaseAccess {
    Date findLastEntryTime();
    void deleteAndInsertAll(List<WeatherForecast> weatherForecasts);
    List<WeatherForecast> getAllForecasts();
    WeatherForecast getLast();
    boolean isFavorite(Place place);
    boolean addFavorite(Place place);
    void removeFavorite(Place place);
    List<Place> getFavorites();
}
