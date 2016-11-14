package com.example.learningzoo.xplorerapp.activity;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.learningzoo.xplorerapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Random;

public class LocateAttractionActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private GoogleMap gMap;
    private static final String[] ATTRACTIONS = new String[] {
            "Marina Bay Sands",
            "Singapore Flyer",
            "Vivo City",
            "Resort World Sentosa",
            "Buddha Tooth Relic Temple",
            "Singapore Zoo",
            "Universal Studios Singapore",
            "Singapore Night Safari",
            "Merlion Park"
    };

    private static HashMap<String, LatLng> LOCATIONS = new HashMap<String, LatLng>();
    static {
        LOCATIONS.put("MBS", new LatLng(1.2830268,103.8585449));
        LOCATIONS.put("SF", new LatLng(1.2892988,103.8609481));
        LOCATIONS.put("VC", new LatLng(1.264241,103.8201383));
        LOCATIONS.put("RWS", new LatLng(1.255179,103.819622));
        LOCATIONS.put("BTRT", new LatLng(1.2815722,103.8420042));
        LOCATIONS.put("SZ", new LatLng(1.4043485,103.7908343));
        LOCATIONS.put("USS", new LatLng(1.2540421,103.8216197));
        LOCATIONS.put("NS", new LatLng(1.4021872,103.7858719));
        LOCATIONS.put("MP", new LatLng(1.2867974,103.8521726));
    }

    private static HashMap<String, String> TITLES = new HashMap<String, String>();
    static {
        TITLES.put("MBS", ATTRACTIONS[0]);
        TITLES.put("SF", ATTRACTIONS[1]);
        TITLES.put("VC", ATTRACTIONS[2]);
        TITLES.put("RWS", ATTRACTIONS[3]);
        TITLES.put("BTRT", ATTRACTIONS[4]);
        TITLES.put("SZ", ATTRACTIONS[5]);
        TITLES.put("USS", ATTRACTIONS[6]);
        TITLES.put("NS", ATTRACTIONS[7]);
        TITLES.put("MP", ATTRACTIONS[8]);
    }

    private static HashMap<String, String> SNIPPETS = new HashMap<String, String>();
    static {
        SNIPPETS.put("MBS", "10 Bayfront Avenue, 018956");
        SNIPPETS.put("SF", "30 Raffles Ave, Singapore 039803");
        SNIPPETS.put("VC", "1 Harbourfront Walk, Singapore 098585");
        SNIPPETS.put("RWS", "8 Sentosa Gateway, 098269");
        SNIPPETS.put("BTRT", "288 S Bridge Rd, 058840");
        SNIPPETS.put("SZ", "80 Mandai Lake Rd, 729826");
        SNIPPETS.put("USS", "8 Sentosa Gateway, 098269");
        SNIPPETS.put("NS", "80 Mandai Lake Rd, Singapore 729826");
        SNIPPETS.put("MP", "1 Fullerton Rd, Singapore 049213");
    }

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_attraction);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("Attractions Locator");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ATTRACTIONS);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        gMap = map;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraPosition singaporePos = CameraPosition.builder()
                .target(new LatLng(1.3085486,103.8174804))
                .zoom(10)
                .build();

        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(singaporePos), 2000, null);
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

    protected void toChoose(View v) {
        Intent intent = new Intent(this, ChooseDestinationsActivity.class);
        startActivity(intent);
    }

    protected void toExpense(View v) {
        startActivity(new Intent(this, ExpensesManagerActivity.class));
    }

    public void search(View v) {
        String input = ((AutoCompleteTextView)findViewById(R.id.autoCompleteTextView)).getText().toString();
        String searchResult = FuzzyChecker.magic(input);
        if ( searchResult.equals("not found") ) {
            Toast.makeText(this, "Not Found!", Toast.LENGTH_SHORT).show();
        } else {
            gMap.addMarker(new MarkerOptions()
                    .position(LOCATIONS.get(searchResult))
                    .title(TITLES.get(searchResult))
                    .snippet(SNIPPETS.get(searchResult)))
                    .showInfoWindow();
            CameraPosition newPos = CameraPosition.builder()
                    .target(LOCATIONS.get(searchResult))
                    .zoom(16)
                    .build();
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(newPos), 3000, null);
        }
    }

    public void feelingLucky(View v) {
        String[] possibleKeys = new String[]{"MBS", "SF", "VC", "RWS", "BTRT", "SZ", "USS", "NS", "MP"};
        int luckyNumber = randInt(0, 8);
        String searchResult = possibleKeys[luckyNumber];

        gMap.addMarker(new MarkerOptions()
                .position(LOCATIONS.get(searchResult))
                .title(TITLES.get(searchResult))
                .snippet(SNIPPETS.get(searchResult)))
                .showInfoWindow();
        CameraPosition newPos = CameraPosition.builder()
                .target(LOCATIONS.get(searchResult))
                .zoom(16)
                .build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(newPos), 3000, null);
    }

    public static int randInt(int min, int max) {

        // NOTE: This will (intentionally) not run as written so that folks
        // copy-pasting have to think about how to initialize their
        // Random instance.  Initialization of the Random instance is outside
        // the main scope of the question, but some decent options are to have
        // a field that is initialized once and then re-used as needed or to
        // use ThreadLocalRandom (if using at least Java 1.7).
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
