package com.example.erik.weatherforecastassignment.view;

//https://stackoverflow.com/questions/3291490/common-class-for-asynctask-in-android
public interface AsyncTaskCompleteListener<T> {
    public void onTaskComplete(T result);
}
