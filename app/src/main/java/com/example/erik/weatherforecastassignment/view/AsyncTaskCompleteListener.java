package com.example.erik.weatherforecastassignment.view;

//https://stackoverflow.com/questions/3291490/common-class-for-asynctask-in-android

/**
 * Interface used to enable callback from AsyncTask on completed task
 */
public interface AsyncTaskCompleteListener<T> {
    void onTaskComplete(T result);
}
