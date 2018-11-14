package com.example.erik.weatherforecastassignment.view;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StringDateTool {

    public static String getDisplayableString(String s){
        TimeZone tz = TimeZone.getTimeZone("Europe/Stockholm");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        Date d = null;
        try {
            d = df.parse(s);
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(d);
        } catch (ParseException e) {}
        return s;
    }

    public static int getHour(String s){
        TimeZone tz = TimeZone.getTimeZone("Europe/Stockholm");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        Date d = null;
        try {
            d = df.parse(s);
            return Integer.parseInt(new SimpleDateFormat("HH").format(d));
        } catch (ParseException e) {}
        return -1;
    }
}
