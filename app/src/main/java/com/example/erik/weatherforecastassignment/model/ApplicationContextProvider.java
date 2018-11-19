package com.example.erik.weatherforecastassignment.model;

import android.app.Application;
import android.content.Context;

/**
 * Application class used to get the current application Context
 */
public class ApplicationContextProvider extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
