package com.example.erik.weatherforecastassignment.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Room persistence Dao interface used to define database operations
 */
@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(FavoriteEntity favoriteEntity);

    @Delete
    void delete(FavoriteEntity favoriteEntity);

    @Query("SELECT COUNT(*) FROM FavoriteEntity WHERE id = :id")
    int exists(long id);

    @Query("SELECT * FROM FavoriteEntity")
    List<FavoriteEntity> findAll();
}
