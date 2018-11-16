package com.example.erik.weatherforecastassignment.view;

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

    private EditText longitudeInputField;
    private EditText latitudeInputField;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherModel = WeatherModel.getInstance();

        approvedTime = findViewById(R.id.weather_approvedtime);
        approvedTime.setText(String.format(getResources().getString(R.string.weather_approvedtime_text), "Not set"));
        mRecyclerView = findViewById(R.id.weather_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        mAdapter = new RecyclerViewAdapter(getApplicationContext(), new ArrayList<>());
        longitudeInputField = findViewById(R.id.weather_longitude);
        latitudeInputField = findViewById(R.id.weather_latitude);
        updateButton = findViewById(R.id.weather_update_button);

        updateButton.setOnClickListener((view) -> {
            //TODO Make async
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": updateButton: CLICKED");
            String longitude = longitudeInputField.getText().toString();
            String latitude = latitudeInputField.getText().toString();
            if(longitude.matches("") || latitude.matches("")){
                Toast.makeText(this, getResources().getString(R.string.weather_longitude_latitude_missing_string), Toast.LENGTH_SHORT).show();
            }
            else{
                new UpdateWeatherAsyncTask().execute(longitude, latitude);
            }
        });
    }

    private class UpdateWeatherAsyncTask extends AsyncTask<String, Void, List<WeatherForecast>>{
        @Override
        protected void onPostExecute(List<WeatherForecast> weatherForecasts) {
            if(weatherForecasts != null && weatherForecasts.size() > 0){
                Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": onPostExecute: Updating recyclerView");
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
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": doInBackGround: Fetching forecasts for lon " + strings[0] + ", lat " + strings[1]);
            List<WeatherForecast> weatherForecasts = weatherModel
                    .getWeatherForecastsByCoordinates(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
            return weatherForecasts;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
