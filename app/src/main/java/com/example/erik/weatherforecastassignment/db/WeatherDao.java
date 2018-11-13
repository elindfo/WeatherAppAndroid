package com.example.erik.weatherforecastassignment.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface WeatherDao {

    @Insert
    void insertAll(WeatherEntity... weatherEntities);
}
