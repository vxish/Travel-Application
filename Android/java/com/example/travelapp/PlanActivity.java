package com.example.travelapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PlanActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    public static final String REQUEST_TAG = "PlanActivity";
    private String apiKey = "AIzaSyBKNcY1uA3ldzZyTZyrmQ9UlG74tTxRpu0";
    private Button mDate;
    private Button mTime;
    private Button mNext;
    private TextView userDate;
    private TextView userTime;
    private Calendar c;
    private DatePickerDialog dpd;
    private TimePickerDialog tpd;
    private RequestQueue mRequestQueue;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    ProgressDialog progress;
    AlertDialog dialog;

    int duration = Toast.LENGTH_LONG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        setContentView(R.layout.activity_plan);
        Places.initialize(getApplicationContext(), apiKey);
        final PlacesClient placesClient = Places.createClient(this);
        progress = new ProgressDialog(this);
        mDate = findViewById(R.id.btnPickDate);
        mTime = findViewById(R.id.btnTime);
        mNext = findViewById(R.id.btnPlanNext);
        userDate = findViewById(R.id.lblDate);
        userTime = findViewById(R.id.lblTime);
        fromAutoComplete();
        toAutoComplete();
        pickDate();
        pickTime();

    }

    public void fromAutoComplete() {
        // Start the autocomplete support fragment
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_FROM);

        // Set the type of data to return
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteSupportFragment.setCountry("UK");

        // Set up a place selection listener to handle the response
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i(REQUEST_TAG, "Place: " + place.getName() + ", " + place.getId());
                UserTravelInfo.mFrom = place.getName();
                Log.i(REQUEST_TAG, "mFrom: " + UserTravelInfo.mFrom);
            }
            @Override
            public void onError(@NonNull Status status) {
                Log.i(REQUEST_TAG, "An error occurred: " + status);
            }
        });
    }

    public void toAutoComplete() {
        // Start the autocomplete support fragment
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_TO);

        // Set the type od data to return
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteSupportFragment.setCountry("UK");

        // Set up a place selection listener to handle the response
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i(REQUEST_TAG, "Place: " + place.getName() + ", " + place.getId());
                UserTravelInfo.mTo = place.getName();
                Log.i(REQUEST_TAG, "mTo: " + UserTravelInfo.mTo);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(REQUEST_TAG, "An error occurred: " + status);
            }
        });
    }

    public void pickDate() {
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(PlanActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        mMonth += 1;
                        String MonthF = "";
                        String DayF = "";
                        if (mMonth < 10) {
                            DecimalFormat formatter = new DecimalFormat("00");
                            MonthF = formatter.format(mMonth);
                            Log.i(REQUEST_TAG, "month: " + MonthF);
                        }
                        if (mDay < 10) {
                            DecimalFormat formatter = new DecimalFormat("00");
                            DayF = formatter.format(mDay);
                            Log.i(REQUEST_TAG, "day: " + DayF);
                        }
                        userDate.setText("Date: " + DayF + "/" + MonthF + "/" + mYear);
                        mDate.setText("Change Date");
                        UserTravelInfo.Date = (mYear + "-" + MonthF + "-" + DayF);
                        Log.i(REQUEST_TAG, "Date: " + UserTravelInfo.Date);
                    }
                }, day, month, year);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }
        });
    }

    public void pickTime() {
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int h, int m) {
        String hr = Integer.toString(h);
        String mm = Integer.toString(m);
        UserTravelInfo.Time = String.format("%02d:%02d", h, m);
        userTime.setText(UserTravelInfo.Time);
        Log.i(REQUEST_TAG, "Time: " + UserTravelInfo.Time);
        mTime.setText("Change Time");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                urlSelctor();
                progress = new ProgressDialog(view.getContext());
                progress.setCancelable(false);
                progress.setMessage("Finding available journeys from " + UserTravelInfo.mFrom + " to " + UserTravelInfo.mTo + ", please wait.");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setProgress(0);
                progress.setMax(100);
                progress.show();
                progressBarStatus = 0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarStatus =  fetchCRC();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        progressBarbHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                progress.setProgress(progressBarStatus);
                            }
                        });
                        if (progressBarStatus >= 100){
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            progress.dismiss();
                            Intent intent = new Intent(PlanActivity.this, SetPlanActivity.class);
                            startActivity(intent);
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(REQUEST_TAG);
        }
    }

    public void urlSelctor()
    {
        if (UserTravelInfo.Time != null)
        {
            UserTravelInfo.futureTravel = true;

        } else if (UserTravelInfo.Date != null)
        {
            UserTravelInfo.futureTravel = true;
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            // Add the buttons
            builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id)
                {
                    // User clicked OK button
                }
            });
            // Create the AlertDialog
             dialog = builder.create();
        }
    }



    public int fetchCRC()
    {
        //Thread to get the From location station Code
        Thread threadA = new Thread() {
            public void run() {
                PlanActivity.ThreadB threadB = new PlanActivity.ThreadB(getApplicationContext());
                JSONObject jsonObjectb = null;
                try {
                    jsonObjectb = threadB.execute().get(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                final JSONObject receivedJSONObjectb = jsonObjectb;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(REQUEST_TAG, "receviedJ: " + receivedJSONObjectb);
                        //receivedJSONObject is Resp
                        if (receivedJSONObjectb != null)
                        {
                            try {
                                JSONArray array = receivedJSONObjectb.getJSONArray("member");
                                JSONObject member = array.getJSONObject(0);
                                if(member.has("station_code")){

                                    UserTravelInfo.CRCfrom = member.getString("station_code");
                                    Log.i(REQUEST_TAG, "CRC: " + UserTravelInfo.CRCfrom);
                                }
                                else {
                                    Log.i(REQUEST_TAG, "Error: " + "No CRCs code found");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

            }
        };
        threadA.start();

        //Thread to get the To location station Code
        Thread threadB = new Thread(){
            public void run(){
                PlanActivity.ThreadC threadC = new PlanActivity.ThreadC(getApplicationContext());
                JSONObject jsonObjectc = null;
                try {
                    jsonObjectc = threadC.execute().get(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                final JSONObject receivedJSONOjectc = jsonObjectc;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(REQUEST_TAG, "receviedJ: " + receivedJSONOjectc);
                        if (receivedJSONOjectc != null)
                        {
                            try {
                                JSONArray array = receivedJSONOjectc.getJSONArray("member");
                                JSONObject member = array.getJSONObject(0);
                                if(member.has("station_code")){
                                    UserTravelInfo.CRCto = member.getString("station_code");
                                    Log.i(REQUEST_TAG, "CRC to: " + UserTravelInfo.CRCto);
                                }
                                else {
                                    Log.i(REQUEST_TAG, "Error: " + "No CRCs code found");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        };

        threadB.start();
        return 100;
    }


    private class ThreadB extends AsyncTask<Void, Void, JSONObject>
    {
        private Context mContext;
        public ThreadB(Context ctx)
        {
            mContext = ctx;
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            final RequestFuture<JSONObject> futureRequest = RequestFuture.newFuture();
            mRequestQueue = CustomVolleyRequestQueue.getInstance(mContext.getApplicationContext())
                    .getRequestQueue();
            String url = "https://transportapi.com/v3/uk/places.json?app_id=272c8506&app_key=089114e932bb36800d5db02c4ac804b1&query="
                    +UserTravelInfo.mFrom+"&type=train_station";
            final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method
                    .GET, url,
                    new JSONObject(), futureRequest, futureRequest);
            jsonRequest.setTag(REQUEST_TAG);
            mRequestQueue.add(jsonRequest);
            try {
                return futureRequest.get(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class ThreadC extends AsyncTask<Void, Void, JSONObject>
    {
        private Context mContext;
        public ThreadC(Context ctx)
        {
            mContext = ctx;
        }
        @Override
        protected JSONObject doInBackground(Void... voids)
        {
            final RequestFuture<JSONObject> futureRequest = RequestFuture.newFuture();
            mRequestQueue = CustomVolleyRequestQueue.getInstance(mContext.getApplicationContext())
                    .getRequestQueue();
            String url = "https://transportapi.com/v3/uk/places.json?app_id=272c8506&app_key=089114e932bb36800d5db02c4ac804b1&query="
                    +UserTravelInfo.mTo+"&type=train_station";
            final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method
                    .GET, url,
                    new JSONObject(), futureRequest, futureRequest);
            jsonRequest.setTag(REQUEST_TAG);
            mRequestQueue.add(jsonRequest);
            try {
                return futureRequest.get(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
