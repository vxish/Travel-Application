package com.example.travelapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView nearby, plan, help, settings;
    private String TAG = "Near";
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    UserTravelInfo.lng = location.getLongitude();
                    UserTravelInfo.lat = location.getLatitude();
                }
            }
        });
        //Define the cards
        setContentView(R.layout.activity_main);
        nearby = findViewById(R.id.crdViewNearby);
        plan = findViewById(R.id.crdPlanTrip);
        help = findViewById(R.id.crdHelp);
        settings = findViewById(R.id.crdSettings);

        //Adding Click listeners to the cards
        nearby.setOnClickListener(this);
        plan.setOnClickListener(this);
        settings.setOnClickListener(this);
        help.setOnClickListener(this);

        Log.i(TAG, "Lat: " + UserTravelInfo.lat);
        Log.i(TAG, "Lng: " + UserTravelInfo.lng);

        isNetworkAvailable();
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return  activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onClick(View view)
    {
        Intent i;

        switch (view.getId())
        {
            case R.id.crdViewNearby : i = new Intent(this, NearbyActivity.class);startActivity(i); break;
            case R.id.crdPlanTrip : i = new Intent(this, PlanActivity.class);startActivity(i); break;
            case R.id.crdSettings : i = new Intent(this, SettingsActivity.class);startActivity(i); break;
            case R.id.crdHelp : i = new Intent(this, HelpActivity.class);startActivity(i); break;
            default:break;
        }
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}
