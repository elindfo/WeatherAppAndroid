package com.example.erik.weatherforecastassignment.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.Date;
import java.util.List;

/**
 * Room persistence abstract Dao class used to define database operations
 */
@Dao
public abstract class WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertAll(List<WeatherEntity> weatherEntities);

    @Query("DELETE FROM WeatherEntity")
    abstract void deleteAll();

    @Query("SELECT MAX(timestamp) FROM WeatherEntity;")
    abstract Date findLatestEntryTime();

    @Transaction
    void deleteAndInsertAll(List<WeatherEntity> weatherEntities){
        deleteAll();
        insertAll(weatherEntities);
    }

    @Query("SELECT * FROM WeatherEntity LIMIT 1")
    abstract WeatherEntity getEntry();

    @Query("SELECT * FROM WeatherEntity;")
    abstract List<WeatherEntity> findAll();
}
