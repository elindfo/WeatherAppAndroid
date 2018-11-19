package com.example.erik.weatherforecastassignment.model;

import java.util.Date;
import java.util.List;

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
