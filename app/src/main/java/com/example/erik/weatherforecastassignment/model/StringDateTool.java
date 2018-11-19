package com.example.erik.weatherforecastassignment.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Tool class that helps converting between Date and String representations of date and time
 */
public class StringDateTool {


    /**
     * Converts from date string to Date object with the correct timezone
     * @param s ISO8601 formatted date string
     * @return Date
     */
    public static Date getDateFromISO8601String(String s){
        TimeZone tz = TimeZone.getTimeZone("Europe/Stockholm");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        try {
            return df.parse(s);
        } catch (ParseException e) {}
        return null;
    }

    /**
     * Converts from Date object to ISO8601 formatted date string with the correct timezone
     * @param date The Date object to be converted
     * @return The date string
     */
    public static String getISO8601StringFromDate(Date date){
        TimeZone tz = TimeZone.getTimeZone("Europe/Stockholm");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }

    /**
     * Converts from Date object to a visually pleasing date string used in UI
     * @param d The Date object to be converted
     * @return The date string
     */
    public static String getDisplayableStringFromDate(Date d){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(d);
    }

    /**
     * Extracts the current hour in 24h format from a Date object
     * @param d The Date object to be used
     * @return Hours as int
     */
    public static int getHourFromDate(Date d){
        return Integer.parseInt(new SimpleDateFormat("HH").format(d));
    }
}
