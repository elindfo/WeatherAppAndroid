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
import android.widget.Toast;

import com.example.erik.weatherforecastassignment.R;
import com.example.erik.weatherforecastassignment.model.Place;
import com.example.erik.weatherforecastassignment.model.WeatherModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.erik.weatherforecastassignment.model.ApplicationContextProvider.getContext;

/**
 * The SearchActivity activity displays the search results from a location search, fetches data and
 * updates the database on RecyclerView item click and adds a location to favorites on long item click
 * It implements two interfaces: OnItemClick and AsyncTaskCompleteListener,
 * both used for AsyncTask callbacks.
 */
public class SearchActivity extends AppCompatActivity implements OnItemClick, AsyncTaskCompleteListener{

    private RecyclerView searchRecyclerView;
    private RecyclerView.Adapter searchRecyclerViewAdapter;

    private GetPlaceDataAsyncTask getPlaceDataAsyncTask;
    private UpdateWeatherDataAsyncTask updateWeatherDataAsyncTask;
    private AddToFavoriteAsyncTask addToFavoriteAsyncTask;

    /**
     * Cancels any running AsyncTask when the activity is destroyed
     */
    @Override
    protected void onDestroy() {
        if(getPlaceDataAsyncTask != null){
            Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onDestroy: cancelling getPlaceDataAsyncTask");
            getPlaceDataAsyncTask.cancel(true);
        }
        if(updateWeatherDataAsyncTask != null){
            Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onDestroy: cancelling updateWeatherDataAsyncTask");
            updateWeatherDataAsyncTask.cancel(true);
        }
        if(addToFavoriteAsyncTask != null){
            Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onDestroy: cancelling addToFavoriteAsyncTask");
            addToFavoriteAsyncTask.cancel(true);
        }
        super.onDestroy();
    }


    /**
     * Callback method that takes the Place data received from the clicked RecyclerView item
     * and starts a new AsyncTask to get weather data from the corresponding Place
     * @param place The Place clicked in the list
     */
    @Override
    public void onClick(Place place) {
        Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onClick: " + place.getPlace());
        updateWeatherDataAsyncTask = new UpdateWeatherDataAsyncTask(this, new ProgressDialog(this));
        updateWeatherDataAsyncTask.execute(place);
    }

    /**
     * Callback method that takes the Place data received from the long clicked RecyclerView item
     * and starts a new AsyncTask to add the Place to the favorites list
     * @param place The place clicked in the list
     */
    @Override
    public void onLongClick(Place place) {
        Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onLongClick: " + place.getPlace());
        addToFavoriteAsyncTask = new AddToFavoriteAsyncTask();
        addToFavoriteAsyncTask.execute(place);
    }

    /**
     * Callback method that closes the activity when the corresponding AsyncTask has finished
     * fetching and storing new weather data
     * @param result Result from AsyncTask (not used)
     */
    @Override
    public void onTaskComplete(Object result) {
        Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onTaskComplete: close activity");
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onCreate");
        super.onCreate(savedInstanceState);

        setContentView(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? R.layout.activity_search_portrait : R.layout.activity_search_landscape);

        getSupportActionBar().setTitle(R.string.weather_search_title);

        searchRecyclerView = findViewById(R.id.weather_place_search_list);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));


        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(getApplicationContext(), new ArrayList<>(), null);

        Intent intent = getIntent();
        String place = intent.getStringExtra("place");

        getPlaceDataAsyncTask = new GetPlaceDataAsyncTask(this);
        getPlaceDataAsyncTask.execute(place);
    }

    /**
     * This private AsyncTask fetches the SMHI Place data that is used to populate the RecyclerView
     */
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
                Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onPostExecute: Updating weather list");
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
            Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": doInBackGround: Fetching places for " + strings[0]);
            List<Place> places = null;
            if(!isCancelled()){
                places = WeatherModel.getInstance().getPlaces(strings[0]);
            }
            return places;
        }

        @Override
        protected void onCancelled(){
            Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onCancelled");
            super.onCancelled();
        }
    }

    /**
     * This private AsyncTask adds a Place object to the favorites list
     */
    private class AddToFavoriteAsyncTask extends AsyncTask<Place, Void, Void>{

        private String placeName = "";
        private boolean added = false;

        @Override
        protected void onPostExecute(Void v) {
            Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onPostExecute");
            if(added){
                Toast.makeText(getContext(), String.format(getResources().getString(R.string.weather_add_to_favorite), placeName) , Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), "Already in favorites" , Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Place... places) {
            placeName = places[0].getPlace();
            if(!isCancelled()){
                if(!WeatherModel.getInstance().isFavorite(places[0])){
                    String s = places[0].getGeonameId() + " " + places[0].getCounty() + " " + places[0].getMunicipality();
                    Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": doInBackground: is not favorite, adding " + s);
                    if(WeatherModel.getInstance().addFavorite(places[0])){
                        added = true;
                        Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": doInBackground: added " + placeName + " to favorites");
                    }
                    else{
                        Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": doInBackground: unable to add " + placeName + " to favorites");
                    }
                }
                else{
                    Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": doInBackground: " + placeName + " already in favorites");
                }
            }
            return null;
        }

        @Override
        protected void onCancelled(){
            Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onCancelled");
            super.onCancelled();
        }
    }
}
