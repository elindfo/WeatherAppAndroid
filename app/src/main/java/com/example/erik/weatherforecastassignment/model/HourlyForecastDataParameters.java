package com.example.erik.weatherforecastassignment.model;

public class HourlyForecastDataParameters {

    private String name;
    private String unit;
    private double[] values;

    public HourlyForecastDataParameters(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }
}
