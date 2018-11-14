package com.example.erik.weatherforecastassignment.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.erik.weatherforecastassignment.R;
import com.example.erik.weatherforecastassignment.model.WeatherForecast;

import java.util.List;

//https://www.youtube.com/watch?v=Vyqz_-sJGFk

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<WeatherForecast> weatherForecasts;

    public RecyclerViewAdapter(List<WeatherForecast> weatherForecasts) {
        this.weatherForecasts = weatherForecasts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d("RecyclerViewAdapter", "onBindViewHolder: called");
        viewHolder.temperature.setText(String.valueOf(weatherForecasts.get(i).getTValue()));
        viewHolder.time.setText(weatherForecasts.get(i).getValidTime());
    }

    @Override
    public int getItemCount() {
        return weatherForecasts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //ImageView weatherImage;
        TextView temperature;
        TextView time;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //weatherImage = itemView.findViewById(R.id.weather_recycler_view_item_image);
            temperature = itemView.findViewById(R.id.weather_recycler_view_item_temperature);
            time = itemView.findViewById(R.id.weather_recycler_view_item_time);
            parentLayout = itemView.findViewById(R.id.weather_recycler_view_layout);
        }
    }
}
