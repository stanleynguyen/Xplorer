package com.example.learningzoo.xplorerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;

import java.util.HashMap;

public class LocateAttractionActivity extends AppCompatActivity implements OnMapReadyCallback {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_attraction);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ATTRACTIONS);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);
    }

    protected void toChoose(View v) {
        Intent intent = new Intent(this, ChooseDestinationsActivity.class);
        startActivity(intent);
    }

    protected void toExpense(View v) {
        startActivity(new Intent(this, ExpensesManagerActivity.class));
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
//
//        map.addMarker(new MarkerOptions()
//                .position(new LatLng(37.4629101,-122.2449094))
//                .title("Facebook")
//                .snippet("Facebook HQ: Menlo Park"))
//                .showInfoWindow();
    }

    public void search(View v) {
        String input = ((AutoCompleteTextView)findViewById(R.id.autoCompleteTextView)).getText().toString();
        String searchResult = FuzzyChecker.magic(input);
        if ( searchResult.equals("not found") ) {
            Toast.makeText(this, "Not Found!", Toast.LENGTH_SHORT).show();
        } else {
            gMap.addMarker(new MarkerOptions()
                    .position(LOCATIONS.get(searchResult))
                    .title(TITLES.get(searchResult)))
                    .showInfoWindow();
            CameraPosition newPos = CameraPosition.builder()
                    .target(LOCATIONS.get(searchResult))
                    .zoom(16)
                    .build();
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(newPos), 3000, null);
        }
    }
}
