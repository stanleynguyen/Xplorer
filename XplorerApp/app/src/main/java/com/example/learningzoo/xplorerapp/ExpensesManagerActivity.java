package com.example.learningzoo.xplorerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpensesManagerActivity extends AppCompatActivity {

    private JSONArray jsonarr;
    private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_manager);
        try {
            String data = getSharedPreferences("EXPENSES", 0).getString("saved", "DEFAULT");
            jsonarr = new JSONArray(data);
        } catch (Exception e) {
            Toast.makeText(this, "Problem retrieving!", Toast.LENGTH_SHORT).show();
        }
    }

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
        try {
            JSONObject json = new JSONObject();
            String datetime = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
            json.put("time", datetime);
            String task = ((EditText)findViewById(R.id.editText2)).getText().toString();
            ((EditText)findViewById(R.id.editText2)).setText("");
            json.put("task", task);
            double cost = Double.parseDouble(((EditText)findViewById(R.id.editText3)).getText().toString());
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
        ((TextView)findViewById(R.id.textView4)).setText("Total Cost: $" + total);
    }

}
