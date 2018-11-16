package com.example.erik.weatherforecastassignment.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

public class NetworkStatus {

    public enum Status{
        MOBILE, WIFI, NO_CONNECTION
    }

    private static final Context context = ApplicationContextProvider.getContext();
    private static final ConnectivityManager connectivityManager =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    public static Status getStatus(){
        if(connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork()).hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
            return Status.MOBILE;
        }
        else if(connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork()).hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
            return Status.WIFI;
        }
        else{
            return Status.NO_CONNECTION;
        }
    }
}
