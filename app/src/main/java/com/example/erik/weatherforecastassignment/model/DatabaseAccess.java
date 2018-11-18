package com.example.erik.weatherforecastassignment.model;

import java.util.Date;
import java.util.List;

public interface DatabaseAccess {
    Date findLatestEntryTime();
    void deleteAndInsertAll(List<WeatherForecast> weatherForecasts);
    List<WeatherForecast> getAll();
    WeatherForecast getLast();
    boolean isFavourite(Place place);
    boolean addFavourite(Place place);
    void removeFavourite(Place place);
}
