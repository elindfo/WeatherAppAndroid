package com.example.erik.weatherforecastassignment.smhi;

/**
 * Class used by Gson to convert from JSON to WeatherData
 */
public class TimeSeries {

    private String validTime;
    private Parameters[] parameters;

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public Parameters[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameters[] parameters) {
        this.parameters = parameters;
    }
}
