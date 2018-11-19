package com.example.erik.weatherforecastassignment.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

/**
 * Helper class to get the current network status. It checks whether the app currently uses wifi,
 * mobile data or has no network connection.
 */
public class NetworkStatus {

    /**
     * Network status enum
     */
    public enum Status{
        MOBILE, WIFI, NO_CONNECTION
    }

    private static final Context context = ApplicationContextProvider.getContext();
    private static final ConnectivityManager connectivityManager =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    /**
     * Static method used to get the current network status.
     * @return Enum representation of the current network status
     */
    public static Status getStatus(){
        if(connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork()) == null){
            return Status.NO_CONNECTION;
        }
        else if(connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork()).hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
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
