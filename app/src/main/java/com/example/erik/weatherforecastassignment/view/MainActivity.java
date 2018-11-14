package com.example.erik.weatherforecastassignment.view;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.erik.weatherforecastassignment.R;
import com.example.erik.weatherforecastassignment.model.WeatherForecast;
import com.example.erik.weatherforecastassignment.model.WeatherModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WeatherModel weatherModel;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private EditText longitudeInputField;
    private EditText latitudeInputField;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherModel = WeatherModel.getInstance();

        mRecyclerView = findViewById(R.id.weather_recycler_view);
        longitudeInputField = findViewById(R.id.weather_longitude);
        latitudeInputField = findViewById(R.id.weather_latitude);
        updateButton = findViewById(R.id.weather_update_button);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        updateButton.setOnClickListener((view) -> {
            //TODO Make async
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
            if(weatherForecasts.size() > 0){
                System.out.println("Approved Time: " + weatherForecasts.get(0).getApprovedTime());
                System.out.println("Valid Time: " + weatherForecasts.get(0).getValidTime());
                System.out.println("Temperature: " + weatherForecasts.get(0).getTValue());
                System.out.println("Tcc: " + weatherForecasts.get(0).getTccMeanValue());
                System.out.println("Longitude: " + weatherForecasts.get(0).getLongitude());
                System.out.println("Latitude: " + weatherForecasts.get(0).getLatitude());
                System.out.println();
            }

            super.onPostExecute(weatherForecasts);
        }

        @Override
        protected List<WeatherForecast> doInBackground(String... strings) {
            List<WeatherForecast> weatherForecasts = weatherModel
                    .getWeatherForecasts(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
            return weatherForecasts;
        }
    }
}
