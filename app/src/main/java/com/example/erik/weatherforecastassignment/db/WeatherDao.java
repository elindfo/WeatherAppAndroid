package com.example.erik.weatherforecastassignment.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeatherEntity weatherEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WeatherEntity> weatherEntities);

    @Delete
    void delete(WeatherEntity weatherEntity);

    @Query("SELECT MAX(timestamp) FROM WeatherEntity WHERE longitude = :longitude AND latitude = :latitude;")
    Date findLatestEntryTimeByLongitudeAndLatitude(double longitude, double latitude);

    @Query("SELECT * FROM WeatherEntity WHERE longitude = :longitude AND latitude = :latitude AND timestamp = (SELECT MAX(timestamp) FROM WeatherEntity WHERE longitude = :longitude AND latitude = :latitude);")
    List<WeatherEntity> findLatestForecastsByLongitudeAndLatitude(double longitude, double latitude);
}
