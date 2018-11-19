package com.example.erik.weatherforecastassignment.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Room persistence Entity class used to store Place data in the database
 */
@Entity
public class FavouriteEntity {

    @PrimaryKey
    private long id;

    @ColumnInfo(name = "place")
    private String place;

    @ColumnInfo(name = "municipality")
    private String municipality;

    @ColumnInfo(name = "county")
    private String county;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "latitude")
    private double latitude;

    public FavouriteEntity(long id, String place, String municipality, String county, double longitude, double latitude) {
        this.id = id;
        this.place = place;
        this.municipality = municipality;
        this.county = county;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
