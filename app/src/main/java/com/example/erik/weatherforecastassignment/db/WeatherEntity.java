package com.example.erik.weatherforecastassignment.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class WeatherEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "approved_time")
    private String approvedTime;

    @ColumnInfo(name = "valid_time")
    private String validTime;

    @ColumnInfo(name = "t")
    private double temperature;

    @ColumnInfo(name = "tcc_mean")
    private double tccMean;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(String approvedTime) {
        this.approvedTime = approvedTime;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTccMean() {
        return tccMean;
    }

    public void setTccMean(double tccMean) {
        this.tccMean = tccMean;
    }
}
