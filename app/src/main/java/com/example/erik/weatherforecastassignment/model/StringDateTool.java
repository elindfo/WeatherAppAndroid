package com.example.erik.weatherforecastassignment.model;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StringDateTool {

    public static Date getDateFromISO8601String(String s){
        TimeZone tz = TimeZone.getTimeZone("Europe/Stockholm");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        try {
            return df.parse(s);

        } catch (ParseException e) {}
        return null;
    }

    public static String getDisplayableStringFromDate(Date d){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(d);
    }

    public static int getHourFromDate(Date d){
        return Integer.parseInt(new SimpleDateFormat("HH").format(d));
    }
}
