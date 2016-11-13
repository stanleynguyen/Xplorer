package com.example.learningzoo.xplorerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String[] ATTRACTIONS = new String[] {
            "Marina Bay Sands",
            "Singapore Flyer",
            "Vivo City",
            "Resort World Sentosa",
            "Buddha Tooth Relic Temple",
            "Singapore Zoo"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        CameraPosition singaporePos = CameraPosition.builder()
                .target(new LatLng(1.3085486,103.8174804))
                .zoom(10)
                .build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(singaporePos), 2000, null);
//
//        map.addMarker(new MarkerOptions()
//                .position(new LatLng(37.4629101,-122.2449094))
//                .title("Facebook")
//                .snippet("Facebook HQ: Menlo Park"))
//                .showInfoWindow();
    }
}
