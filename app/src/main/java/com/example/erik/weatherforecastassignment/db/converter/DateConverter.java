package com.example.erik.weatherforecastassignment.db.converter;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Room persistence converter class used to store and retrieve Date objects
 */
public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
