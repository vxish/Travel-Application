package com.example.travelapp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class TravelTracker extends IntentService
{
    public static String uFrom;
    public static String uTo;
    public static String uFromPlatform;
    public static String uConnectPlatform;
    public static String uToPlatform;
    public static String uDepartTime;
    public static String uArrivalTime;
    private String TAG = "Travel";

    public TravelTracker(){
        super("Travel Tracker");

    }

    @Override
    public void onCreate() {
        super.onCreate();
    Log.i(TAG, "Hello " + "World");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
