package com.example.erik.weatherforecastassignment.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity()
public class WeatherEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "approved_time")
    private Date approvedTime;

    @ColumnInfo(name = "valid_time")
    private Date validTime;

    @ColumnInfo(name = "t_value")
    private double tValue;

    @ColumnInfo(name = "tcc_mean_value")
    private double tccMeanValue;

    @ColumnInfo(name = "wsymb2")
    private int wsymb2;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "timestamp")
    private Date timestamp;

    public WeatherEntity(Date approvedTime, Date validTime, double tValue, double tccMeanValue, int wsymb2, double longitude, double latitude, Date timestamp) {
        this.approvedTime = approvedTime;
        this.validTime = validTime;
        this.tValue = tValue;
        this.tccMeanValue = tccMeanValue;
        this.wsymb2 = wsymb2;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(Date approvedTime) {
        this.approvedTime = approvedTime;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    public double getTValue() {
        return tValue;
    }

    public void setTValue(double tValue) {
        this.tValue = tValue;
    }

    public double getTccMeanValue() {
        return tccMeanValue;
    }

    public void setTccMeanValue(double tccMeanValue) {
        this.tccMeanValue = tccMeanValue;
    }

    public int getWsymb2() {
        return wsymb2;
    }

    public void setWsymb2(int wsymb2) {
        this.wsymb2 = wsymb2;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
