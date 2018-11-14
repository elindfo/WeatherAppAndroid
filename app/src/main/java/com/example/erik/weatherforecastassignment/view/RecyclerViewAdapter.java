package com.example.erik.weatherforecastassignment.view;

import android.content.Context;
import android.content.res.Resources;
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
    private Context context;

    public RecyclerViewAdapter(Context context, List<WeatherForecast> weatherForecasts) {
        this.context = context;
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
        viewHolder.time.setText(StringDateTool.getDisplayableString(weatherForecasts.get(i).getValidTime()));

        setWeatherImage(viewHolder, i);
    }

    private void setWeatherImage(ViewHolder viewHolder, int i){
        int hour = StringDateTool.getHour(weatherForecasts.get(i).getValidTime());
        boolean dayTime = false;
        switch(hour){
            //DayTime
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:{
                dayTime = true;
                break;
            }
        }
        int drawableResourceId = 0;
        if(dayTime){
            drawableResourceId = context.getResources().getIdentifier("d" + (int)weatherForecasts.get(i).getWsymb2(), "drawable", context.getPackageName());
        }
        else{
            drawableResourceId = context.getResources().getIdentifier("n" + (int)weatherForecasts.get(i).getWsymb2(), "drawable", context.getPackageName());
        }
        viewHolder.weatherImage.setImageDrawable(context.getDrawable(drawableResourceId));
    }

    @Override
    public int getItemCount() {
        return weatherForecasts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView weatherImage;
        TextView temperature;
        TextView time;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            weatherImage = itemView.findViewById(R.id.weather_recycler_view_item_image);
            temperature = itemView.findViewById(R.id.weather_recycler_view_item_temperature);
            time = itemView.findViewById(R.id.weather_recycler_view_item_time);
            parentLayout = itemView.findViewById(R.id.weather_recycler_view_layout);
        }
    }
}
