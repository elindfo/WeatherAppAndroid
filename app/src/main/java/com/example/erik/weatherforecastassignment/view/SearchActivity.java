package com.example.erik.weatherforecastassignment.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.erik.weatherforecastassignment.R;
import com.example.erik.weatherforecastassignment.model.Place;
import com.example.erik.weatherforecastassignment.model.StringDateTool;
import com.example.erik.weatherforecastassignment.model.WeatherForecast;
import com.example.erik.weatherforecastassignment.model.WeatherModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.erik.weatherforecastassignment.model.ApplicationContextProvider.getContext;

public class SearchActivity extends AppCompatActivity implements OnItemClick{

    private TextView searchTerm;
    private RecyclerView searchRecyclerView;
    private RecyclerView.Adapter searchRecyclerViewAdapter;

    private GetPlaceDataAsyncTask getPlaceDataAsyncTask;
    private StoreWeatherDataAsyncTask storeWeatherDataAsyncTask;

    private WeatherModel weatherModel;

    @Override
    protected void onDestroy() {
        if(getPlaceDataAsyncTask != null){
            getPlaceDataAsyncTask.cancel(true);
        }
        if(storeWeatherDataAsyncTask != null){
            storeWeatherDataAsyncTask.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(Place place) {
        storeWeatherDataAsyncTask = new StoreWeatherDataAsyncTask();
        storeWeatherDataAsyncTask.execute(place);
    }

    @Override
    public void onLongClick(Place place) {
        Toast.makeText(getContext(), String.format(getResources().getString(R.string.weather_add_to_favourite), place.getPlace()) , Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? R.layout.activity_search_portrait : R.layout.activity_search_landscape);

        weatherModel = WeatherModel.getInstance();

        getSupportActionBar().setTitle("Location");

        searchTerm = findViewById(R.id.weather_place_search_text);

        searchRecyclerView = findViewById(R.id.weather_place_search_list);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));


        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(getApplicationContext(), new ArrayList<>(), null);

        Intent intent = getIntent();
        String place = intent.getStringExtra("place");

        searchTerm.setText(R.string.weather_search_favourite_hint);

        getPlaceDataAsyncTask = new GetPlaceDataAsyncTask(this);
        getPlaceDataAsyncTask.execute(place);
    }

    private class GetPlaceDataAsyncTask extends AsyncTask<String, Void, List<Place>>{

        private ProgressDialog progressDialog;
        private OnItemClick callback;

        public GetPlaceDataAsyncTask(OnItemClick callback){
            this.callback = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SearchActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            if(places != null && places.size() > 0){
                Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": onPostExecute: Updating recyclerView");
                searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(getApplicationContext(), places, callback);
                searchRecyclerView.setAdapter(searchRecyclerViewAdapter);
            }
            else{
                Toast.makeText(SearchActivity.this, getResources().getString(R.string.weather_location_not_found_string), Toast.LENGTH_SHORT).show();
            }
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            super.onPostExecute(places);
        }

        @Override
        protected List<Place> doInBackground(String... strings) {
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": doInBackGround: Fetching places for " + strings[0]);
            List<Place> places = null;
            if(!isCancelled()){
                places = weatherModel.getPlaces(strings[0]);
            }
            return places;
        }

        @Override
        protected void onCancelled(){
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": onCancelled");
            super.onCancelled();
        }
    }

    private class StoreWeatherDataAsyncTask extends AsyncTask<Place, Void, Void>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SearchActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void v) {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            super.onPostExecute(v);
            finish();
        }

        @Override
        protected Void doInBackground(Place... places) {
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": doInBackground");
            if(!isCancelled()){
                weatherModel.setWeatherForecastsByPlace(places[0]);
            }
            return null;
        }

        @Override
        protected void onCancelled(){
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": onCancelled");
            super.onCancelled();
        }
    }
}
