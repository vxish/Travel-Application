package com.example.travelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private ListView mListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mListview = findViewById(R.id.lstSettings);

        ArrayList<String> options = new ArrayList<>();
        options.add("Intensity");
        options.add("Change Colour Scheme");
        options.add("App Data usage");
        options.add("More");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, options);
        mListview.setAdapter(adapter);
    }
}
