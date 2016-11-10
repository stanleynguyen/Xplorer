package com.example.learningzoo.xplorerapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class ChooseDestinationsActivity extends AppCompatActivity {
    final static private String[] DESTINATIONS = new String[6];
    static {
        DESTINATIONS[0] = "Marina Bay Sands";
        DESTINATIONS[1] = "Singapore Flyer";
        DESTINATIONS[2] = "Vivo City";
        DESTINATIONS[3] = "Resorts World Sentosa";
        DESTINATIONS[4] = "Buddha Tooth Relic Temple";
        DESTINATIONS[5] = "Zoo";
    }
    ListView listview;
    String[] places;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_destinations);
        context = this;
        listview = (ListView)findViewById(R.id.list_view);
        //string array
        places = Arrays.copyOfRange(DESTINATIONS, 1, DESTINATIONS.length);
        // set adapter for listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, places);
        listview.setAdapter(adapter);
        listview.setItemsCanFocus(false);
        // we want multiple clicks
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    protected void planItinerary(View v) {
        ArrayList<Integer> destinations = new ArrayList<Integer>();
        for (int i = 0; i < places.length; i++) {
            CheckedTextView child = (CheckedTextView) listview.getChildAt(i);
            if (child.isChecked()) destinations.add(i);
        }
        Log.d("destinations", destinations.toString());
    }

}
