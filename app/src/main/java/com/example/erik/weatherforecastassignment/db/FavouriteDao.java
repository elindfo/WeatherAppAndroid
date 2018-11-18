package com.example.erik.weatherforecastassignment.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(FavouriteEntity favouriteEntity);

    @Delete
    void delete(FavouriteEntity favouriteEntity);

    @Query("SELECT COUNT(*) FROM FavouriteEntity WHERE id = :id")
    int exists(long id);
}
