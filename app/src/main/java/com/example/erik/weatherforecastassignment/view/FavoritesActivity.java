package com.example.erik.weatherforecastassignment.view;

import android.app.ProgressDialog;
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
 * The FavoritesActivity activity displays the current favorites list, fetches data and
 * updates the database on RecyclerView item click and removes a location from favorites on long item click
 * It implements two interfaces: OnItemClick and AsyncTaskCompleteListener,
 * both used for AsyncTask callbacks.
 */
public class FavoritesActivity extends AppCompatActivity implements OnItemClick, AsyncTaskCompleteListener{

    private RecyclerView favoritesRecyclerView;
    private RecyclerView.Adapter favoritesRecyclerViewAdapter;

    private GetFavoritesAsyncTask getFavoritesAsyncTask;
    private RemoveFavoriteAsyncTask removeFavoriteAsyncTask;
    private UpdateWeatherDataAsyncTask updateWeatherDataAsyncTask;

    /**
     * Cancels any running AsyncTask when the activity is destroyed
     */
    @Override
    protected void onDestroy() {
        if(getFavoritesAsyncTask != null){
            Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onDestroy: cancelling getFavoritesAsyncTask");
            getFavoritesAsyncTask.cancel(true);
        }
        if(removeFavoriteAsyncTask != null){
            Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onDestroy: cancelling removeFavoriteAsyncTask");
            removeFavoriteAsyncTask.cancel(true);
        }
        if(updateWeatherDataAsyncTask != null){
            Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onDestroy: cancelling updateWeatherDataAsyncTask");
            updateWeatherDataAsyncTask.cancel(true);
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
     * and starts a new AsyncTask to remove the Place from the favorites list
     * @param place The place clicked in the list
     */
    @Override
    public void onLongClick(Place place){
        Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onLongClick: " + place.getPlace());
        removeFavoriteAsyncTask = new RemoveFavoriteAsyncTask();
        removeFavoriteAsyncTask.execute(place);
    }

    /**
     * Callback method that closes the activity when the corresponding AsyncTask has finished
     * fetching and storing new weather data
     * @param result Result from AsyncTask (not used)
     */
    @Override
    public void onTaskComplete(Object result) {
        Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onTaskComplete");
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onCreate");
        super.onCreate(savedInstanceState);

        setContentView(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? R.layout.activity_favorites_portrait : R.layout.activity_favorites_landscape);

        getSupportActionBar().setTitle(R.string.weather_favorites_title);

        favoritesRecyclerView = findViewById(R.id.weather_favorites_list);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoritesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        favoritesRecyclerViewAdapter = new FavoritesRecyclerViewAdapter(getApplicationContext(), new ArrayList<>(), null);

        getFavoritesAsyncTask = new GetFavoritesAsyncTask(this);
        getFavoritesAsyncTask.execute();
    }

    /**
     * This private AsyncTask fetches the favorites list from the database and updates the
     * RecyclerView to show found favorites.
     */
    private class GetFavoritesAsyncTask extends AsyncTask<Void, Void, List<Place>> {

        private OnItemClick callback;

        public GetFavoritesAsyncTask(OnItemClick callback){
            this.callback = callback;
        }


        @Override
        protected void onPostExecute(List<Place> places) {
            if(places != null){
                Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": onPostExecute: Updating favorites list");
                favoritesRecyclerViewAdapter = new FavoritesRecyclerViewAdapter(getApplicationContext(), places, callback);
                favoritesRecyclerView.setAdapter(favoritesRecyclerViewAdapter);
            }
            else{
                Toast.makeText(FavoritesActivity.this, getResources().getString(R.string.weather_location_not_found_string), Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(places);
        }

        @Override
        protected List<Place> doInBackground(Void... voids) {
            Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": doInBackGround: Fetching favorites");
            List<Place> places = null;
            if(!isCancelled()){
                places = WeatherModel.getInstance().getFavorites();
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
     * This private AsyncTask removes the chosen Place from the favorites list and updates
     * the RecyclerView with the new updated favorites list
     */
    private class RemoveFavoriteAsyncTask extends AsyncTask<Place, Void, Void> {

        private String placeName;

        @Override
        protected void onPostExecute(Void v) {
            getFavoritesAsyncTask = new GetFavoritesAsyncTask(FavoritesActivity.this);
            getFavoritesAsyncTask.execute();
            Toast.makeText(getContext(), String.format(getResources().getString(R.string.weather_remove_from_favorite), placeName) , Toast.LENGTH_SHORT).show();
            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Place... places) {
            placeName = places[0].getPlace();
            Log.d(MainActivity.TAG, this.getClass().getSimpleName() + ": doInBackGround: removing " + places[0].getCounty());
            if(!isCancelled()){
                WeatherModel.getInstance().removeFavorite(places[0]);
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
