package com.example.travelapp;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class NearbyActivity extends AppCompatActivity implements  NearbyAdapter.onItemClickListener
{
    private static final String TAG = "Nearby";
    private RecyclerView mRecyclerView;
    private NearbyAdapter mNearbyAdapter;
    private ArrayList<NearbyItem> mNearbyList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        //Data and adapter invokes (DON NOT CHANGE ORDERS!)
        mRecyclerView = findViewById(R.id.recyclerViewNearby);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNearbyList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();
        mNearbyAdapter = new NearbyAdapter(NearbyActivity.this, mNearbyList);
        mRecyclerView.setAdapter(mNearbyAdapter);


    }

    private void parseJSON()
    {
        String url = "https://transportapi.com/v3/uk/places.json?app_id=272c8506&app_key=089114e932bb36800d5db02c4ac804b1&" +
                "lat="+UserTravelInfo.lat+"&lon="+UserTravelInfo.lng+"&type=train_station,bus_stop,tube_station";
        Log.i(TAG, "URl: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            JSONArray jsonArray = response.getJSONArray("member");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject member = jsonArray.getJSONObject(i);
                                String nName = "";
                                String nType = "";
                                String nDesc = "";
                                String nCode = "";
                                if (member.has("name")){
                                    nName = member.getString("name");
                                    Log.d(TAG, "Name is: " + nName);
                                }
                                if(member.has("type")) {
                                    nType = member.getString("type");
                                    Log.d(TAG, "Type is: " + nType);
                                }
                                if(member.has("description")){
                                    nDesc = member.getString("description");
                                    Log.d(TAG, "Desc is: " + nDesc);
                                }
                                else
                                {
                                    nDesc += nName;
                                    Log.d(TAG, "Desc is: " + nDesc);
                                }

                                if(member.has("station_code")){
                                    nCode = member.getString("station_code");
                                }
                                else{
                                    nCode = "---";
                                }

                                if(nType.contains("bus_stop")){
                                    nType = "Bus Stop";


                                }
                                else if (nType.contains("train_station"))
                                {
                                    nType = "Train Station";
                                }
                                else {
                                    nType = "Tube Station";
                                }
                                mNearbyList.add(new NearbyItem(nName, nType, nDesc, nCode));
                            }
                            mNearbyAdapter.notifyDataSetChanged();
                            mNearbyAdapter = new NearbyAdapter(NearbyActivity.this, mNearbyList);
                            mRecyclerView.setAdapter(mNearbyAdapter);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }


    @Override
    public void onItemClick(int position) {
        final NearbyItem nearbyItem = mNearbyList.get(position);
        UserTravelInfo.NearbyCode = nearbyItem.getmCode();
    }


}
