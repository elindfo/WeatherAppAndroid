package com.example.erik.weatherforecastassignment.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.erik.weatherforecastassignment.db.converter.DateConverter;

/**
 * Room persistence Database class used to configure the database
 */
@Database(entities = {WeatherEntity.class, FavoriteEntity.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract WeatherDao weatherDao();
    public abstract FavoriteDao favoriteDao();
}
