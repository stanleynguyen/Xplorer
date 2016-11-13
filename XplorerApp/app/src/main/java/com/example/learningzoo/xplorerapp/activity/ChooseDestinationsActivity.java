package com.example.learningzoo.xplorerapp.activity;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.learningzoo.xplorerapp.R;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Arrays;

public class ChooseDestinationsActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{
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
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    String[] places;
    EditText budgetField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_destinations);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("Itinerary Planner");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        budgetField = (EditText)findViewById(R.id.editText);
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

    @Override
    public void onDrawerItemSelected(View view, int position) {
        Intent intent = new Intent(this, LocateAttractionActivity.class);
        switch (position) {
            case 0:
                intent = new Intent(this, LocateAttractionActivity.class);
                break;
            case 1:
                intent = new Intent(this, ChooseDestinationsActivity.class);
                break;
            case 2:
                intent = new Intent(this, ExpensesManagerActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap map) {}

    protected void planItinerary(View v) {
        ArrayList<Integer> destinations = new ArrayList<Integer>();
        for (int i = 0; i < places.length; i++) {
            CheckedTextView child = (CheckedTextView) listview.getChildAt(i);
            if (child.isChecked()) destinations.add(i + 1);
        }
        Intent intent = new Intent(this, ItineraryPlanActivity.class);
        intent.putIntegerArrayListExtra("DESTINATIONS", destinations );
        intent.putExtra("BUDGET", Double.parseDouble(budgetField.getText().toString()));
        startActivity(intent);
    }

}
