package com.example.erik.weatherforecastassignment.model;

/**
 * Model class that contains the values representing a Place. Contains cherry picked
 * data from the SMHI API place data response.
 */
public class Place {

    private long geonameId;
    private String place;
    private String municipality;
    private String county;
    private double longitude;
    private double latitude;

    public Place(long geonameId, String place, String municipality, String county, double longitude, double latitude) {
        this.geonameId = geonameId;
        this.place = place;
        this.municipality = municipality;
        this.county = county;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public long getGeonameId() {
        return geonameId;
    }

    public void setGeonameId(long geonameId) {
        this.geonameId = geonameId;
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
