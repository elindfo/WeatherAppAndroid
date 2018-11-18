package com.example.erik.weatherforecastassignment.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.erik.weatherforecastassignment.R;
import com.example.erik.weatherforecastassignment.model.Place;

import java.util.List;

public class FavouritesRecyclerViewAdapter extends RecyclerView.Adapter<FavouritesRecyclerViewAdapter.ViewHolder>{

    private List<Place> places;
    private Context context;
    private OnItemClick callback;

    public FavouritesRecyclerViewAdapter(Context context, List<Place> places, OnItemClick callback) {
        this.context = context;
        this.places = places;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_favourites_listitem, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d("MainRecyclerViewAdapter", "onBindViewHolder: called");
        viewHolder.place.setText(places.get(i).getPlace());
        viewHolder.municipality.setText(places.get(i).getMunicipality());
        viewHolder.county.setText(places.get(i).getCounty());
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView place;
        TextView municipality;
        TextView county;
        RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            place = itemView.findViewById(R.id.weather_favourites_recycler_view_item_place);
            municipality = itemView.findViewById(R.id.weather_favourites_recycler_view_item_municipality);
            county = itemView.findViewById(R.id.weather_favourites_recycler_view_item_county);
            layout = itemView.findViewById(R.id.weather_favourites_recycler_view_layout);
            itemView.setOnClickListener((v) -> {
                int pos = getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    Place place = places.get(pos);
                    callback.onClick(place);
                }
            });

            itemView.setOnLongClickListener((v) -> {
                int pos = getAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    Place place = places.get(pos);
                    callback.onLongClick(place);
                }
                return true;
            });
        }
    }
}
