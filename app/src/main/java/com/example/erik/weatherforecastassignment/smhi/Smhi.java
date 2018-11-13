package com.example.erik.weatherforecastassignment.smhi;

import com.example.erik.weatherforecastassignment.model.HourlyForecastData;
import com.example.erik.weatherforecastassignment.model.HourlyForecastDataParameters;
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
import java.util.ArrayList;
import java.util.List;

public class Smhi implements WeatherProvider {

    private static final String PLACE_URL = "https://maceo.sth.kth.se/wpt-a/backend_solr/autocomplete/search/Sigfridstorp";
    private static final String WEATHER_URL = "https://maceo.sth.kth.se/api/category/pmp3g/version/2/geotype/point/lon/14.333/lat/60.383/";

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
    public WeatherForecast getWeatherForecastByCoord(double lon, double lat){
        String requestUrl = "https://maceo.sth.kth.se/api/category/pmp3g/version/2/geotype/point/lon/" + lon + "/lat/" + lat + "/";
        WeatherData data = smhiRequest.getWeatherData(requestUrl);

        if(data == null || data.getTimeSeries().length < 1){
            return null;
        }

        WeatherForecast weatherForecast = new WeatherForecast();
        weatherForecast.setApprovedTime(data.getApprovedTime());

        List<HourlyForecastData> hourlyForecastDataList = new ArrayList<>();
        for(TimeSeries ts : data.getTimeSeries()){
            HourlyForecastData hourlyForecastData = new HourlyForecastData();
            hourlyForecastData.setValidTime(ts.getValidTime());
            List<HourlyForecastDataParameters> hourlyForecastDataParametersList = new ArrayList<>();
            for(Parameters p : ts.getParameters()){
                HourlyForecastDataParameters hourlyForecastDataParameters = new HourlyForecastDataParameters();
                hourlyForecastDataParameters.setName(p.getName());
                hourlyForecastDataParameters.setUnit(p.getUnit());
                hourlyForecastDataParameters.setValues(p.getValues());
                hourlyForecastDataParametersList.add(hourlyForecastDataParameters);
            }
            hourlyForecastData.setHourlyForecastDataParameters(hourlyForecastDataParametersList);
            hourlyForecastDataList.add(hourlyForecastData);
        }
        weatherForecast.setHourlyForecastData(hourlyForecastDataList);

        return weatherForecast;
    }

    private class SmhiRequest{

        private Gson gson;
        public SmhiRequest(){
            gson = new Gson();
        }

        public WeatherData getWeatherData(String requestUrl){
            URL url;
            URLConnection urlConnection;
            try{
                url = new URL(requestUrl);
                urlConnection = url.openConnection();
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonData = jsonParser.parse(new InputStreamReader((InputStream)urlConnection.getContent()));
                return gson.fromJson(jsonData.toString(), WeatherData.class);
            }
            catch (Exception e){}
            return null;
        }

        public PlaceData getPlaceData(String requestUrl){
            URL url;
            URLConnection urlConnection;
            try{
                url = new URL(requestUrl);
                urlConnection = url.openConnection();
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonData = jsonParser.parse(new InputStreamReader((InputStream)urlConnection.getContent()));
                return gson.fromJson(jsonData.toString(), PlaceData.class);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
