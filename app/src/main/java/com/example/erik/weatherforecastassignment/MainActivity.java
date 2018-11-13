package com.example.erik.weatherforecastassignment;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erik.weatherforecastassignment.model.WeatherForecast;
import com.example.erik.weatherforecastassignment.model.WeatherHandler;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WeatherHandler weatherHandler;

    private TextView weatherLocationText;
    private EditText longitudeInputField;
    private EditText latitudeInputField;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherLocationText = findViewById(R.id.weather_location);
        longitudeInputField = findViewById(R.id.weather_longitude);
        latitudeInputField = findViewById(R.id.weather_latitude);
        updateButton = findViewById(R.id.weather_update_button);

        weatherHandler = WeatherHandler.getInstance();

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
                weatherLocationText.setText(weatherForecasts.get(0).getApprovedTime());
            }
            else{
                weatherLocationText.setText(getResources().getString(R.string.weather_location_not_found_string));
            }
            super.onPostExecute(weatherForecasts);
        }

        @Override
        protected List<WeatherForecast> doInBackground(String... strings) {
            List<WeatherForecast> weatherForecasts = weatherHandler
                    .getWeatherForecasts(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
            return weatherForecasts;
        }
    }
}
