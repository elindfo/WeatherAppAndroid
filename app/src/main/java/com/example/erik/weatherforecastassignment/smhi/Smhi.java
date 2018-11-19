package com.example.erik.weatherforecastassignment.smhi;

import android.util.Log;

import com.example.erik.weatherforecastassignment.model.Place;
import com.example.erik.weatherforecastassignment.model.StringDateTool;
import com.example.erik.weatherforecastassignment.model.WeatherForecast;
import com.example.erik.weatherforecastassignment.model.WeatherProvider;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Smhi implements WeatherProvider {

    public static final String TAG = "WeatherForecastAssignment";

    private static Smhi smhi;
    private SmhiRequest smhiRequest;

    private Smhi(){
        smhiRequest = new SmhiRequest();
    }

    public static Smhi getInstance(){
        if(smhi == null){
            smhi = new Smhi();
        }
        return smhi;
    }

    @Override
    public List<WeatherForecast> getWeatherForecastsByCoord(double lon, double lat){

        String requestUrl = String.format("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/%.6f/lat/%.6f/data.json", lon, lat);

        Log.d(Smhi.TAG, this.getClass().getSimpleName() + ": getWeatherForecastsByCoord: getting forecasts for " + lon + ", " + lat);
        WeatherData data = smhiRequest.getWeatherData(requestUrl);

        if(data == null || data.getTimeSeries().length < 1){
            Log.d(Smhi.TAG, this.getClass().getSimpleName() + ": getWeatherForecastsByCoord: no forecasts found");
            return null;
        }

        Log.d(Smhi.TAG, this.getClass().getSimpleName() + ": getWeatherForecastsByCoord: found forecasts");

        List<WeatherForecast> weatherForecasts = new ArrayList<>();

        for(TimeSeries ts : data.getTimeSeries()){
            WeatherForecast weatherForecast = new WeatherForecast();
            weatherForecast.setApprovedTime(StringDateTool.getDateFromISO8601String(data.getApprovedTime()));
            weatherForecast.setValidTime(StringDateTool.getDateFromISO8601String(ts.getValidTime()));
            weatherForecast.setLongitude(lon);
            weatherForecast.setLatitude(lat);
            for(Parameters p : ts.getParameters()){
                if(p.getName().equals("t")){
                    if(p.getValues().length > 0){
                        weatherForecast.setTValue(p.getValues()[0]);
                    }
                }
                if(p.getName().equals("tcc_mean")){
                    if(p.getValues().length > 0){
                        weatherForecast.setTccMeanValue(p.getValues()[0]);
                    }
                }
                if(p.getName().equals("Wsymb2")){
                    if(p.getValues().length > 0){
                        weatherForecast.setWsymb2((int)p.getValues()[0]);
                    }
                }
            }
            weatherForecasts.add(weatherForecast);
        }
        return weatherForecasts;
    }

    @Override
    public List<Place> getPlaceData(String place) {

        String requestUrl = String.format("https://www.smhi.se/wpt-a/backend_solr/autocomplete/search/%s", place);

        List<PlaceData> data = Arrays.asList(smhiRequest.getPlaceData(requestUrl));

        Log.d(Smhi.TAG, this.getClass().getSimpleName() + ": getPlaceData: Found " + data.size() + " places.");

        if(data == null){
            return null;
        }

        List<Place> places = new ArrayList<>();

        for(PlaceData placeData : data){
            Place p = new Place(
                    Long.parseLong(placeData.getGeonameid()),
                    placeData.getPlace(),
                    placeData.getMunicipality(),
                    placeData.getCounty(),
                    placeData.getLon(),
                    placeData.getLat()
            );
            places.add(p);
        }
        return places;
    }

    private class SmhiRequest{

        private Gson gson;

        public SmhiRequest(){
            gson = new Gson();
        }

        public WeatherData getWeatherData(String requestUrl){
            Log.d(Smhi.TAG, this.getClass().getSimpleName() + ": getWeatherData: fetching weather data from URL: " + requestUrl);
            URL url;
            URLConnection urlConnection;
            try{
                url = new URL(requestUrl);
                urlConnection = url.openConnection();
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonData = jsonParser.parse(new InputStreamReader((InputStream)urlConnection.getContent()));
                return gson.fromJson(jsonData.toString(), WeatherData.class);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        public PlaceData[] getPlaceData(String requestUrl){
            Log.d(Smhi.TAG, this.getClass().getSimpleName() + ": getWeatherData: fetching place data from URL: " + requestUrl);

            URL url;
            URLConnection urlConnection;
            try{
                url = new URL(requestUrl);
                urlConnection = url.openConnection();
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonData = jsonParser.parse(new InputStreamReader((InputStream)urlConnection.getContent()));
                return gson.fromJson(jsonData.toString(), PlaceData[].class);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
