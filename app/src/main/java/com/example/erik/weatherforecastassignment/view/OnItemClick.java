package com.example.erik.weatherforecastassignment.view;

import com.example.erik.weatherforecastassignment.model.Place;

/**
 * Interface used to enable callback from AsyncTask on RecyclerView item click
 */
public interface OnItemClick {
    void onClick(Place place);
    void onLongClick(Place place);
}
