package com.example.travelapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.travelapp.UserTravelInfo.Date;
import static com.example.travelapp.UserTravelInfo.Time;

public class SetPlanActivity extends AppCompatActivity implements PlanAdapter.OnItemClickListener
{
    public static final String TAG = "SetPlan";
    private RecyclerView mRecyclerView;
    private Button mBtnStartTrack;
    private ViewGroup bg;
    private PlanAdapter mPlanAdapter;
    private ArrayList<PlanItem> mPlanList;
    private RequestQueue mRequestQueue;
    String nAStatus = "";
    String nDStatus = "";
    private FusedLocationProviderClient client;
    private Animation animationUp;
    private Animation animationDown;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_plan);
        mRecyclerView = findViewById(R.id.recyclerSetPlan);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPlanList = new ArrayList<>();
        Log.i(TAG, "From:" + UserTravelInfo.mFrom);
        Log.i(TAG, "To:" + UserTravelInfo.mTo);
        mRequestQueue = Volley.newRequestQueue(this);
        mPlanAdapter = new PlanAdapter(SetPlanActivity.this, mPlanList);
        mRecyclerView.setAdapter(mPlanAdapter);
        mBtnStartTrack = findViewById(R.id.btnStartTrack);
        animationUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        bg = findViewById(R.id.transitionsContainer);
        bg.setVisibility(View.GONE);
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(SetPlanActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        client.getLastLocation().addOnSuccessListener(SetPlanActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    UserTravelInfo.lng = location.getLongitude();
                    UserTravelInfo.lat = location.getLatitude();

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        parseJSONfetchInfo();
    }

    public void parseJSONfetchInfo()
    {
        Log.i(TAG, "CRC From:" + UserTravelInfo.CRCfrom);
        Log.i(TAG, "CRC To:" + UserTravelInfo.CRCto);
        String url = "";
        if (UserTravelInfo.futureTravel){
            url = "https://transportapi.com/v3/uk/train/station/"+UserTravelInfo.CRCfrom+"/"+Date+"/"+Time+"/" +
                    "timetable.json?app_id=272c8506&app_key=089114e932bb36800d5db02c4ac804b1&" +
                    "destination="+UserTravelInfo.CRCto+"&station_detail=destination&train_status=passenger&type=departure";
        }else {
            url = "https://transportapi.com/v3/uk/train/station/"+UserTravelInfo.CRCfrom+"/live.json?app_id=272c8506&app_" +
                    "key=089114e932bb36800d5db02c4ac804b1&darwin=true&destination="+UserTravelInfo.CRCto+"&station_" +
                    "detail=destination&train_status=passenger&type=departure";
        }
        Log.i(TAG, "Selected URL " + url);
        Log.i(TAG, "futureTravelVal: " + UserTravelInfo.futureTravel);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            JSONObject dep = response.getJSONObject("departures");
                            JSONArray allInfo = dep.getJSONArray("all");
                            for (int i = 0; i < allInfo.length(); i++){
                                JSONObject all = allInfo.getJSONObject(i);
                                String nPlatform = "Platform: ";
                                String nDestination = "";
                                String nDtime = "";
                                String nAtime = "12:00";
                                String nLocation = "";
                                String nStops = "";
                                String nDuration = "";
                                if (all.has("platform")){
                                    nPlatform += all.getString("platform");
                                    Log.i(TAG, "D Status: " + nPlatform);
                                }

                                if (all.has("destination_name")){
                                    nDestination = all.getString("destination_name");
                                    Log.i(TAG, "D Status: " + nDestination);
                                }


                                if (all.has("aimed_departure_time")){
                                    nDtime = all.getString("aimed_departure_time");
                                    Log.i(TAG, "D Status: " + nDtime);
                                }


                                if (all.has("status")){
                                    nDStatus = all.getString("status");
                                    Log.i(TAG, "D Status: " + nDStatus);
                                }

                                if (all.has("status")){
                                    nAStatus = all.getString("status");
                                    Log.i(TAG, "A Status: " + nAStatus);
                                }

                                if (all.isNull("aimed_departure_time"))
                                {
                                    UserTravelInfo.isNull = true;
                                    Intent intent = new Intent(SetPlanActivity.this, PlanActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                nLocation = UserTravelInfo.mFrom;
                                mPlanList.add(new PlanItem(nPlatform, nDestination, nDtime, nAtime, nLocation, nStops, nDuration, nAStatus, nDStatus));
                            }
                            mPlanAdapter.notifyDataSetChanged();
                            mPlanAdapter = new PlanAdapter(SetPlanActivity.this, mPlanList);
                            mRecyclerView.setAdapter(mPlanAdapter);
                            mPlanAdapter.setOnItemClickListener(SetPlanActivity.this);

                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);

    }

    @Override
    public void onItemClick(int position) {
        final PlanItem clickedItem = mPlanList.get(position);

        if (bg.isShown()){
            bg.setVisibility(View.GONE);
            mBtnStartTrack.setVisibility(View.GONE);
            bg.startAnimation(animationUp);
            mBtnStartTrack.startAnimation(animationUp);
        } else {
            bg.setVisibility(View.VISIBLE);
            mBtnStartTrack.setVisibility(View.VISIBLE);
            bg.startAnimation(animationDown);
            mBtnStartTrack.startAnimation(animationDown);
        }



        mBtnStartTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TravelTracker.uTo = clickedItem.getmDestination();
                Log.i(TAG, "uTO:" + TravelTracker.uTo);

                TravelTracker.uFrom = clickedItem.getmLocation();
                Log.i(TAG, "uFROM:" + TravelTracker.uFrom);

                TravelTracker.uDepartTime = clickedItem.getmDepTime();
                Log.i(TAG, "uDEPART:" + TravelTracker.uDepartTime);

                TravelTracker.uArrivalTime = clickedItem.getmArrTime();
                Log.i(TAG, "uARRIVE:" + TravelTracker.uArrivalTime);



            }
        });
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}