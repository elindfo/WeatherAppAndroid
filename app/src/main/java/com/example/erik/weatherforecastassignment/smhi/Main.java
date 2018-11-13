package com.example.erik.weatherforecastassignment.smhi;

import com.google.gson.*;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

public class Main {

    private static final String PLACE_URL = "https://maceo.sth.kth.se/wpt-a/backend_solr/autocomplete/search/Sigfridstorp";
    private static final String WEATHER_URL = "https://maceo.sth.kth.se/api/category/pmp3g/version/2/geotype/point/lon/14.333/lat/60.383/";

    public static void main(String[] args) { //https://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
        Gson gson = new Gson();
        URL url;
        URLConnection request;
        try {
            url = new URL(WEATHER_URL);
            request = url.openConnection();
            request.connect();

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonData = jsonParser.parse(new InputStreamReader((InputStream)request.getContent()));
            WeatherData weatherData = gson.fromJson(jsonData.toString(), WeatherData.class);
            System.out.println("approvedTime: " + weatherData.getApprovedTime());
            Arrays.stream(weatherData.getTimeSeries()).forEach(s -> {
                System.out.println("validTime: " + s.getValidTime());
                Arrays.stream(s.getParameters()).forEach(p -> {
                    if(p.getName().equals("t") || p.getName().equals("tcc_mean")){
                        System.out.println("level: " + p.getLevel());
                        System.out.println("levelType: " + p.getLevelType());
                        System.out.println("name: " + p.getName());
                        System.out.println("unit: " + p.getUnit());
                        System.out.print("type: ");
                        Arrays.stream(p.getValues()).forEach(v -> System.out.print(v + " "));
                        System.out.println();
                        System.out.println();
                    }
                });
            });
            //printPlaceData(placeData);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //printPlaceData(placeData);
    }

    private static void printPlaceData(PlaceData[] placeData){
        for(PlaceData pd : placeData){
            System.out.println(pd.getPlace());
            System.out.println(pd.getCountry());
            System.out.println(pd.getCounty());
            System.out.println(pd.getDistrict());
            System.out.println(pd.getGeonameid());
            System.out.println(pd.getLat());
            System.out.println(pd.getLon());
            System.out.println(pd.getMunicipality());
            System.out.println(pd.getPopulation());
            Arrays.stream(pd.getType()).forEach(System.out::println);
            System.out.println();
        }
    }
}
