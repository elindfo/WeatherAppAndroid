package com.example.erik.weatherforecastassignment.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.Date;
import java.util.List;

@Dao
public abstract class WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(WeatherEntity weatherEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertAll(List<WeatherEntity> weatherEntities);

    @Delete
    abstract void delete(WeatherEntity weatherEntity);

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

    @Query("SELECT * FROM WeatherEntity WHERE longitude = :longitude AND latitude = :latitude AND timestamp = (SELECT MAX(timestamp) FROM WeatherEntity WHERE longitude = :longitude AND latitude = :latitude);")
    abstract List<WeatherEntity> findLatestForecastsByLongitudeAndLatitude(double longitude, double latitude);

    @Query("SELECT * FROM WeatherEntity;")
    abstract List<WeatherEntity> findAll();

    @Query("SELECT MAX(timestamp) FROM WeatherEntity WHERE longitude = :lon AND latitude = :lat;")
    public abstract Date findLatestEntryTimeLongitudeAndLatitude(double lon, double lat);
}
