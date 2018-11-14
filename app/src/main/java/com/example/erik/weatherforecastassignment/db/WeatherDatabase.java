package com.example.erik.weatherforecastassignment.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.erik.weatherforecastassignment.db.converter.DateConverter;

@Database(entities = {WeatherEntity.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract WeatherDao weatherDao();
}
