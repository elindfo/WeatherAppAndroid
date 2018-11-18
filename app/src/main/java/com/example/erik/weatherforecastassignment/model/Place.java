package com.example.erik.weatherforecastassignment.model;

public class Place {

    private String place;
    private double longitude;
    private double latitude;

    public Place(String place, double longitude, double latitude) {
        this.place = place;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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
