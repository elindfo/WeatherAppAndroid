package com.example.erik.weatherforecastassignment.view;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erik.weatherforecastassignment.R;
import com.example.erik.weatherforecastassignment.model.NetworkStatus;
import com.example.erik.weatherforecastassignment.model.StringDateTool;
import com.example.erik.weatherforecastassignment.model.WeatherForecast;
import com.example.erik.weatherforecastassignment.model.WeatherModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.erik.weatherforecastassignment.model.ApplicationContextProvider.getContext;

public class MainActivity extends AppCompatActivity {

    private WeatherModel weatherModel;

    private TextView approvedTime;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private EditText placeInputField;
    private Button updateButton;

    private GetWeatherByPlaceAsyncTask getWeatherByPlaceAsyncTask;
    private GetLastUpdatedWeatherAsyncTask getLastUpdatedWeatherAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? R.layout.activity_main_portrait : R.layout.activity_main_landscape);

        weatherModel = WeatherModel.getInstance();

        approvedTime = findViewById(R.id.weather_approvedtime);
        approvedTime.setText(String.format(getResources().getString(R.string.weather_approvedtime_text), "Not set"));
        mRecyclerView = findViewById(R.id.weather_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        mAdapter = new RecyclerViewAdapter(getApplicationContext(), new ArrayList<>());
        placeInputField = findViewById(R.id.weather_place);
        updateButton = findViewById(R.id.weather_update_button);

        updateButton.setOnClickListener((view) -> {
            //TODO Make async
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": updateButton: CLICKED");
            String place = placeInputField.getText().toString();
            if(place.matches("")){
                Toast.makeText(this, getResources().getString(R.string.weather_place_missing_string), Toast.LENGTH_SHORT).show();
            }
            else{
                getWeatherByPlaceAsyncTask = new GetWeatherByPlaceAsyncTask();
                getWeatherByPlaceAsyncTask.execute(place);
            }
        });
    }

    private class GetWeatherByPlaceAsyncTask extends AsyncTask<String, Void, List<WeatherForecast>>{
        @Override
        protected void onPostExecute(List<WeatherForecast> weatherForecasts) {
            if(weatherForecasts != null && weatherForecasts.size() > 0){
                Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": onPostExecute: Updating recyclerView");
                getSupportActionBar().setTitle(String.format("Weather in %s" ,weatherForecasts.get(0).getPlace()));
                approvedTime.setText(String.format(getResources()
                                .getString(R.string.weather_approvedtime_text),
                        StringDateTool.getDisplayableStringFromDate(weatherForecasts.get(0).getApprovedTime())));
                mAdapter = new RecyclerViewAdapter(getApplicationContext(), weatherForecasts);
                mRecyclerView.setAdapter(mAdapter);
            }
            else{
                Toast.makeText(MainActivity.this, getResources().getString(R.string.weather_location_not_found_string), Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(weatherForecasts);
        }

        @Override
        protected List<WeatherForecast> doInBackground(String... strings) {
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": doInBackGround: Fetching forecasts for " + strings[0]);
            List<WeatherForecast> weatherForecasts = null;
            if(!isCancelled()){
                weatherForecasts = weatherModel
                        .getWeatherForecastsByPlace(strings[0]);
            }
            return weatherForecasts;
        }

        @Override
        protected void onCancelled(){
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": onCancelled");
            super.onCancelled();
        }
    }

    private class GetLastUpdatedWeatherAsyncTask extends AsyncTask<NetworkStatus.Status, Void, List<WeatherForecast>>{

        private NetworkStatus.Status status;

        @Override
        protected void onPostExecute(List<WeatherForecast> weatherForecasts) {
            if(weatherForecasts != null && weatherForecasts.size() > 0){
                Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": onPostExecute: Updating recyclerView");
                getSupportActionBar().setTitle(String.format("Weather in %s" ,weatherForecasts.get(0).getPlace()));
                approvedTime.setText(String.format(getResources()
                                .getString(R.string.weather_approvedtime_text),
                        StringDateTool.getDisplayableStringFromDate(weatherForecasts.get(0).getApprovedTime())));
                mAdapter = new RecyclerViewAdapter(getApplicationContext(), weatherForecasts);
                mRecyclerView.setAdapter(mAdapter);
                if(status == NetworkStatus.Status.NO_CONNECTION){
                    Toast.makeText(MainActivity.this, "No internet connection. Data may be outdated.", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(MainActivity.this, "No weather history found", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(weatherForecasts);
        }

        @Override
        protected List<WeatherForecast> doInBackground(NetworkStatus.Status... networkStatus) {
            status = networkStatus[0];
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": doInBackGround: networkStatus: " + status + " - Loading previous data");
            List<WeatherForecast> weatherForecasts = null;
            if(!isCancelled()){
                weatherForecasts = weatherModel
                        .getLastUpdatedWeatherForecasts(status);
            }
            return weatherForecasts;
        }

        @Override
        protected void onCancelled() {
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": onCancelled");
            super.onCancelled();
        }
    }

    @Override
    protected void onStart() {
        NetworkStatus.Status status = NetworkStatus.getStatus();
        Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": onStart: " + status);
        getLastUpdatedWeatherAsyncTask = new GetLastUpdatedWeatherAsyncTask();
        getLastUpdatedWeatherAsyncTask.execute(status);
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": onDestroy");
        if(getWeatherByPlaceAsyncTask != null){
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": onDestroy: getWeatherByCoordAsyncTask not null");
            getWeatherByPlaceAsyncTask.cancel(true);
        }
        if(getLastUpdatedWeatherAsyncTask != null){
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": onDestroy: getLastUpdatedWeatherAsyncTask not null");
            getLastUpdatedWeatherAsyncTask.cancel(true);
        }
        super.onDestroy();
    }
}
