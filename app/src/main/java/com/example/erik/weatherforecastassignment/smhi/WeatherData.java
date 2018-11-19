package com.example.erik.weatherforecastassignment.smhi;

/**
 * Class used by Gson to convert from JSON to WeatherData
 */
public class WeatherData {
    private String approvedTime;
    private String referenceTime;
    private Geometry geometry;
    private TimeSeries[] timeSeries;

    public String getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(String approvedTime) {
        this.approvedTime = approvedTime;
    }

    public String getReferenceTime() {
        return referenceTime;
    }

    public void setReferenceTime(String referenceTime) {
        this.referenceTime = referenceTime;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public TimeSeries[] getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(TimeSeries[] timeSeries) {
        this.timeSeries = timeSeries;
    }
}
