package com.example.erik.weatherforecastassignment.view;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.erik.weatherforecastassignment.model.Place;
import com.example.erik.weatherforecastassignment.model.WeatherModel;

/**
 * This AsyncTask handles the fetching of new data from the SMHI API and
 * the updating of the database with said data.
 */
public class UpdateWeatherDataAsyncTask extends AsyncTask<Place, Void, Void> {

    private AsyncTaskCompleteListener callback;
    private ProgressDialog progressDialog;

    public UpdateWeatherDataAsyncTask(AsyncTaskCompleteListener callback, ProgressDialog progressDialog){
        this.callback = callback;
        this.progressDialog = progressDialog;
    }

    @Override
    protected void onPreExecute() {
        Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onPreExecute");
        super.onPreExecute();
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Void v) {
        Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onPostExecute");
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        super.onPostExecute(v);
        callback.onTaskComplete(null);
    }

    @Override
    protected Void doInBackground(Place... places) {
        Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": doInBackground");
        if(!isCancelled()){
            WeatherModel.getInstance().setWeatherForecastsByPlace(places[0]);
        }
        return null;
    }

    @Override
    protected void onCancelled(){
        Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onCancelled");
        super.onCancelled();
    }
}
