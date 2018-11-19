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
import android.widget.TextView;
import android.widget.Toast;

import com.example.erik.weatherforecastassignment.R;
import com.example.erik.weatherforecastassignment.db.FavouriteEntity;
import com.example.erik.weatherforecastassignment.model.Place;
import com.example.erik.weatherforecastassignment.model.WeatherModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.erik.weatherforecastassignment.model.ApplicationContextProvider.getContext;

public class FavouritesActivity extends AppCompatActivity implements OnItemClick, AsyncTaskCompleteListener{

    private RecyclerView favouritesRecyclerView;
    private RecyclerView.Adapter favouritesRecyclerViewAdapter;

    private WeatherModel weatherModel;

    private GetFavouritesAsyncTask getFavouritesAsyncTask;
    private RemoveFavouriteAsyncTask removeFavouriteAsyncTask;
    private UpdateWeatherDataAsyncTask updateWeatherDataAsyncTask;

    @Override
    protected void onDestroy() {
        if(getFavouritesAsyncTask != null){
            getFavouritesAsyncTask.cancel(true);
        }
        if(removeFavouriteAsyncTask != null){
            removeFavouriteAsyncTask.cancel(true);
        }
        if(updateWeatherDataAsyncTask != null){
            updateWeatherDataAsyncTask.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(Place place) {
        updateWeatherDataAsyncTask = new UpdateWeatherDataAsyncTask(this, new ProgressDialog(this));
        updateWeatherDataAsyncTask.execute(place);
    }

    @Override
    public void onTaskComplete(Object result) {
        finish();
    }

    @Override
    public void onLongClick(Place place){
        removeFavouriteAsyncTask = new RemoveFavouriteAsyncTask();
        removeFavouriteAsyncTask.execute(place);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? R.layout.activity_favourites_portrait : R.layout.activity_favourites_landscape);

        weatherModel = WeatherModel.getInstance();

        getSupportActionBar().setTitle("Favourites");

        favouritesRecyclerView = findViewById(R.id.weather_favourites_list);
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favouritesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        favouritesRecyclerViewAdapter = new FavouritesRecyclerViewAdapter(getApplicationContext(), new ArrayList<>(), null);

        getFavouritesAsyncTask = new GetFavouritesAsyncTask(this);
        getFavouritesAsyncTask.execute();
    }

    private class GetFavouritesAsyncTask extends AsyncTask<Void, Void, List<Place>> {

        private OnItemClick callback;

        public GetFavouritesAsyncTask(OnItemClick callback){
            this.callback = callback;
        }


        @Override
        protected void onPostExecute(List<Place> places) {
            if(places != null){
                Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": onPostExecute: Updating favourites list");
                favouritesRecyclerViewAdapter = new FavouritesRecyclerViewAdapter(getApplicationContext(), places, callback);
                favouritesRecyclerView.setAdapter(favouritesRecyclerViewAdapter);
            }
            else{
                Toast.makeText(FavouritesActivity.this, getResources().getString(R.string.weather_location_not_found_string), Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(places);
        }

        @Override
        protected List<Place> doInBackground(Void... voids) {
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": doInBackGround: Fetching favourites");
            List<Place> places = null;
            if(!isCancelled()){
                places = weatherModel.getFavourites();
            }
            return places;
        }

        @Override
        protected void onCancelled(){
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": onCancelled");
            super.onCancelled();
        }
    }

    private class RemoveFavouriteAsyncTask extends AsyncTask<Place, Void, Void> {

        private String placeName;

        @Override
        protected void onPostExecute(Void v) {
            getFavouritesAsyncTask = new GetFavouritesAsyncTask(FavouritesActivity.this);
            getFavouritesAsyncTask.execute();
            Toast.makeText(getContext(), String.format(getResources().getString(R.string.weather_remove_from_favourite), placeName) , Toast.LENGTH_SHORT).show();
            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Place... places) {
            placeName = places[0].getPlace();
            Log.d("WeatherForecastAssignment", this.getClass().getSimpleName() + ": doInBackGround: Removing " + places[0].getCounty());
            if(!isCancelled()){
                WeatherModel.getInstance().removeFavourite(places[0]);
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
