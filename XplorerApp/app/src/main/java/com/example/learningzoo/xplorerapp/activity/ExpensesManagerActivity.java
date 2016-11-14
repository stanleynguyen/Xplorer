package com.example.learningzoo.xplorerapp.activity;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningzoo.xplorerapp.R;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.learningzoo.xplorerapp.activity.ItineraryPlanActivity.round;

public class ExpensesManagerActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private JSONArray jsonarr;
    private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    private SimpleAdapter adapter;
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_manager);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("Expenses Manager");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        try {
            String data = getSharedPreferences("EXPENSES", 0).getString("saved", "DEFAULT");
            jsonarr = new JSONArray(data);
        } catch (Exception e) {
            Toast.makeText(this, "Problem retrieving!", Toast.LENGTH_SHORT).show();
        }
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

    protected void onStart() {
        super.onStart();
        try {
            if ( jsonarr != null ) {
                for (int i = 0; i < jsonarr.length(); i++) {
                    JSONObject o = jsonarr.getJSONObject(i);
                    Map<String, String> datum = new HashMap<String, String>();
                    datum.put("item", o.getString("task") + ": $" + o.getDouble("expense"));
                    datum.put("date", o.getString("time"));
                    data.add(datum);
                }
                calculateNewTotal();
            } else {
                jsonarr = new JSONArray();
                ((TextView)findViewById(R.id.textView4)).setText("Total Cost: $0");
            }
            adapter = new SimpleAdapter(
                    this,
                    data,
                    android.R.layout.simple_list_item_2,
                    new String[]{"item", "date"},
                    new int[]{android.R.id.text1, android.R.id.text2}
            );
            final ExpensesManagerActivity _this = this;
            ListView lv = (ListView) findViewById(R.id.listView1);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        jsonarr = remove(jsonarr, position);
                        data.remove(position);
                        adapter.notifyDataSetChanged();
                        getSharedPreferences("EXPENSES", 0).edit().putString("saved", jsonarr.toString()).apply();
                        calculateNewTotal();
                    } catch (JSONException e) {
                        Toast.makeText(_this, "Problem removing!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (JSONException e) {
            Toast.makeText(this, "Problem retrieving!", Toast.LENGTH_SHORT).show();
        }
    }

    protected void saveNewExpense(View v) {
        String task = ((EditText)findViewById(R.id.editText2)).getText().toString().trim();
        String costString = ((EditText)findViewById(R.id.editText3)).getText().toString().trim();
        if ( task.equals("") || costString.equals("") ) return;
        try {
            JSONObject json = new JSONObject();
            String datetime = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
            json.put("time", datetime);
            ((EditText)findViewById(R.id.editText2)).setText("");
            json.put("task", task);
            double cost = Double.parseDouble(costString);
            ((EditText)findViewById(R.id.editText3)).setText("");
            json.put("expense", cost);
            jsonarr.put(json);
            Map<String, String> datum = new HashMap<String, String>();
            datum.put("item", task + ": $" + cost);
            datum.put("date", datetime);
            data.add(datum);
            adapter.notifyDataSetChanged();
            getSharedPreferences("EXPENSES", 0).edit().putString("saved", jsonarr.toString()).apply();
            calculateNewTotal();
        } catch (JSONException exception) {
            Toast.makeText(this, "Problem saving!", Toast.LENGTH_SHORT).show();
        }
    }

    protected JSONArray remove(JSONArray a, int position) throws JSONException{
        JSONArray returnedVal = new JSONArray();
        for (int i = 0; i < a.length(); i++) {
            if (i != position) returnedVal.put(jsonarr.getJSONObject(i));
        }

        return returnedVal;
    }

    protected void calculateNewTotal() throws JSONException {
        double total = 0;
        for(int i = 0; i < jsonarr.length(); i++) {
            JSONObject json = jsonarr.getJSONObject(i);
            total += json.getDouble("expense");
        }
        ((TextView)findViewById(R.id.textView4)).setText("Total Expenditure: $" + round(total, 2));
    }

}
