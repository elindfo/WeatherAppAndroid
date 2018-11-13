package com.example.erik.weatherforecastassignment.model;

public class HourlyForecastData {

    private String validTime;
    private String name;
    private String unit;
    private double[] values;

    public HourlyForecastData() {
    }

    public String getValidTime() {
        return validTime;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public double[] getValues() {
        return values;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setValues(double[] values) {
        this.values = values;
    }
}