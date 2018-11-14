package com.example.erik.weatherforecastassignment.model;

import java.util.List;

public class WeatherForecast {

    private String approvedTime;
    private String validTime;
    private double tValue;
    private double tccMeanValue;
    private int wsymb2;
    private double longitude;
    private double latitude;

    public WeatherForecast() {
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
}
