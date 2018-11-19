package com.example.erik.weatherforecastassignment.model;

import java.util.Date;

public class WeatherForecast {

    private Date approvedTime;
    private Date validTime;
    private double tValue;
    private double tccMeanValue;
    private int wsymb2;
    private double longitude;
    private double latitude;
    private String place;

    public WeatherForecast(){}

    public WeatherForecast(Date approvedTime, Date validTime, double tValue, double tccMeanValue, int wsymb2, double longitude, double latitude, String place) {
        this.approvedTime = approvedTime;
        this.validTime = validTime;
        this.tValue = tValue;
        this.tccMeanValue = tccMeanValue;
        this.wsymb2 = wsymb2;
        this.longitude = longitude;
        this.latitude = latitude;
        this.place = place;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
