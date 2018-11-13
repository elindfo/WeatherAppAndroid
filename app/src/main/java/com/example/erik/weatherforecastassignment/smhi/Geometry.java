package com.example.erik.weatherforecastassignment.smhi;

public class Geometry {

    private String type;
    private double[][] coordinates = new double[][]{};

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[][] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[][] coordinates) {
        this.coordinates = coordinates;
    }
}
