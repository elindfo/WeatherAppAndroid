package com.example.erik.weatherforecastassignment;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erik.weatherforecastassignment.model.HourlyForecastData;
import com.example.erik.weatherforecastassignment.model.HourlyForecastDataParameters;
import com.example.erik.weatherforecastassignment.model.WeatherForecast;
import com.example.erik.weatherforecastassignment.model.WeatherHandler;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WeatherHandler weatherHandler;

    private TextView weatherLocationText;
    private TextView weatherTemperatureText;
    private TextView weatherApprovedTimeText;
    private EditText longitudeInputField;
    private EditText latitudeInputField;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherLocationText = findViewById(R.id.weather_location);
        weatherTemperatureText = findViewById(R.id.weather_temperature);
        weatherApprovedTimeText = findViewById(R.id.weather_approved_time);
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

    private class UpdateWeatherAsyncTask extends AsyncTask<String, Void, WeatherForecast>{

        @Override
        protected void onPostExecute(WeatherForecast weatherForecast) {
            if(weatherForecast != null){
                System.out.println("Approved Time: " + weatherForecast.getApprovedTime());
                System.out.println();
                for(HourlyForecastData hfd : weatherForecast.getHourlyForecastData()){
                    System.out.println("Valid time: " + hfd.getValidTime());
                    for(HourlyForecastDataParameters hfdp : hfd.getHourlyForecastDataParameters()){
                        if(hfdp.getName().equals("t")){
                            System.out.println("Name: " + hfdp.getName());
                            System.out.println("Unit: " + hfdp.getUnit());
                            System.out.print("Values: ");
                            for(double d : hfdp.getValues()){
                                System.out.print(d + " ");
                            }
                            System.out.println();
                            System.out.println();
                        }
                    }
                }
            }
            else{
                weatherLocationText.setText(getResources().getString(R.string.weather_location_not_found_string));
            }
            super.onPostExecute(weatherForecast);
        }

        @Override
        protected WeatherForecast doInBackground(String... strings) {
            WeatherForecast weatherForecast = weatherHandler
                    .getWeatherForecast(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
            return weatherForecast;
        }
    }
}
