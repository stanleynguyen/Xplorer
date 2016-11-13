package com.example.learningzoo.xplorerapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningzoo.xplorerapp.R;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class ItineraryPlanActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private int[] destinationsQueue;
    private double budget;
    private int[][] itiPlan;
    ListView lv;
    private double cost;

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_plan);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("Itinerary Planner");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        Bundle extra = getIntent().getExtras();
        ArrayList<Integer> destinations = extra.getIntegerArrayList("DESTINATIONS");
        destinationsQueue = new int[destinations.size()];
        for ( int i = 0; i < destinations.size(); i++ ) {
            destinationsQueue[i] = destinations.get(i);
        }
        budget = extra.getDouble("BUDGET");
        itiPlan = getMostOptimalTrip(destinationsQueue, budget);

        String[] data = generateItiItems(itiPlan);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        lv = (ListView)findViewById(R.id.listView1);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int[] trip = itiPlan[position];
                String mapUrl = "http://maps.google.com/maps?saddr=" + LOCATIONS[trip[1]] + "&daddr="
                                + LOCATIONS[trip[2]] + "&dirflg=";
                switch (trip[0]) {
                    case 0:
                        mapUrl += "r";
                        break;
                    case 1:
                        mapUrl += "d";
                        break;
                    case 2:
                        mapUrl += "w";
                        break;
                }
                Intent mapIntent = new Intent( Intent.ACTION_VIEW, Uri.parse(mapUrl) );
                startActivity(mapIntent);
            }
        });

        cost = totalCost(itiPlan);
        ((TextView)findViewById(R.id.textView)).setText("Cost: $" + cost);
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

    protected static String[] generateItiItems(int[][] plan) {
        String[] result = new String[plan.length];
        for (i=0; i < result.length; i++) {
            result[i] = "";
            result[i] += "Travel from " + DESTINATIONS[plan[i][1]] + " to " + DESTINATIONS[plan[i][2]] + " by ";
            switch (plan[i][0]) {
                case 0:
                    result[i] += "public transport";
                    break;
                case 1:
                    result[i] += "taxi";
                    break;
                case 2:
                    result[i] += "walking";
            }
        }
        return result;
    }

    protected void addToExpenses(View v) {
        try {
            String data = getSharedPreferences("EXPENSES", 0).getString("saved", "DEFAULT");
            JSONArray jsonArray = new JSONArray(data);
            JSONObject newObj = new JSONObject();
            newObj.put("task", "Transportation Cost");
            newObj.put("expense", cost);
            String datetime = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
            newObj.put("time", datetime);
            jsonArray.put(newObj);
            getSharedPreferences("EXPENSES", 0).edit().putString("saved", jsonArray.toString()).apply();
            Toast.makeText(this, "Successfully added to expenses", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(this, "Got problem! Please try again!", Toast.LENGTH_SHORT).show();
        }
    }

    final static private String[] LOCATIONS = new String[6];
    static {
        LOCATIONS[0] = "Marina+Bay+Sands,+10+Bayfront+Avenue,+Singapore+018956";
        LOCATIONS[1] = "30+Raffles+Ave,+Singapore+Flyer,+Singapore+039803";
        LOCATIONS[2] = "VivoCity,+1+Harbourfront+Walk,+Singapore+098585";
        LOCATIONS[3] = "8+Sentosa+Gateway,+Resorts+World+Sentosa,+098269";
        LOCATIONS[4] = "288+S+Bridge+Rd,+Buddha+Tooth+Relic+Temple,+Singapore+058840";
        LOCATIONS[5] = "80+Mandai+Lake+Rd,+Singapore+Zoo,+Singapore+729826";
    }

    //diamond code the data in
    //assign each location to a unique number
    final static private String[] DESTINATIONS = new String[6];
    static {
        DESTINATIONS[0] = "Marina Bay Sands";
        DESTINATIONS[1] = "Singapore Flyer";
        DESTINATIONS[2] = "Vivo City";
        DESTINATIONS[3] = "Resorts World Sentosa";
        DESTINATIONS[4] = "Buddha Tooth Relic Temple";
        DESTINATIONS[5] = "Zoo";
    }
    final static private int mNoOfDestinations = DESTINATIONS.length;

    //arrays of cost and time taken
    //outer most array index: 1 is public transport, 2 is taxi, 3 is foot
    //second layer of array determines the starting point (from)
    //third layer of array determines the end point (to)
    final static private double[][][] COST = new double[3][mNoOfDestinations][mNoOfDestinations];
    static {
        //public transports
        //from MBS
        COST[0][0][1] = 0.83;
        COST[0][0][2] = 1.18;
        COST[0][0][3] = 4.03;
        COST[0][0][4] = 0.88;
        COST[0][0][5] = 1.96;
        //from Spore Flyer
        COST[0][1][0] = 0.83;
        COST[0][1][2] = 1.26;
        COST[0][1][3] = 4.03;
        COST[0][1][4] = 0.98;
        COST[0][1][5] = 1.89;
        //from Vivo
        COST[0][2][0] = 1.18;
        COST[0][2][1] = 1.26;
        COST[0][2][3] = 2.00;
        COST[0][2][4] = 0.98;
        COST[0][2][5] = 1.99;
        //from Sentosa
        COST[0][3][0] = 1.18;
        COST[0][3][1] = 1.26;
        COST[0][3][2] = 0.00;
        COST[0][3][4] = 0.98;
        COST[0][3][5] = 1.99;
        //from Temple
        COST[0][4][0] = 0.88;
        COST[0][4][1] = 0.98;
        COST[0][4][2] = 0.98;
        COST[0][4][3] = 3.98;
        COST[0][4][5] = 1.91;
        //from Zoo
        COST[0][5][0] = 1.88;
        COST[0][5][1] = 1.96;
        COST[0][5][2] = 2.11;
        COST[0][5][3] = 4.99;
        COST[0][5][4] = 1.91;

        //taxi
        //from MBS
        COST[1][0][1] = 3.22;
        COST[1][0][2] = 6.96;
        COST[1][0][3] = 8.50;
        COST[1][0][4] = 4.98;
        COST[1][0][5] = 18.40;
        //from Spore Flyer
        COST[1][1][0] = 4.32;
        COST[1][1][2] = 7.84;
        COST[1][1][3] = 9.38;
        COST[1][1][4] = 4.76;
        COST[1][1][5] = 18.18;
        //from Vivo
        COST[1][2][0] = 8.30;
        COST[1][2][1] = 7.96;
        COST[1][2][3] = 4.54;
        COST[1][2][4] = 6.42;
        COST[1][2][5] = 22.58;
        //from Sentosa
        COST[1][3][0] = 8.74;
        COST[1][3][1] = 8.40;
        COST[1][3][2] = 3.22;
        COST[1][3][4] = 6.64;
        COST[1][3][5] = 22.80;
        //from Temple
        COST[1][4][0] = 5.32;
        COST[1][4][1] = 4.76;
        COST[1][4][2] = 4.98;
        COST[1][4][3] = 6.52;
        COST[1][4][5] = 18.40;
        //from Zoo
        COST[1][5][0] = 22.48;
        COST[1][5][1] = 19.40;
        COST[1][5][2] = 21.48;
        COST[1][5][3] = 23.68;
        COST[1][5][4] = 21.60;

        //on foot all free so leave to default 0
    }

    //same structure as cost
    final static private int[][][] TIME = new int[3][mNoOfDestinations][mNoOfDestinations];
    static {
        //public
        TIME[0][0][1] = 17;
        TIME[0][0][2] = 26;
        TIME[0][0][3] = 35;
        TIME[0][0][4] = 19;
        TIME[0][0][5] = 84;

        TIME[0][1][0] = 17;
        TIME[0][1][2] = 31;
        TIME[0][1][3] = 38;
        TIME[0][1][4] = 24;
        TIME[0][1][5] = 85;

        TIME[0][2][0] = 24;
        TIME[0][2][1] = 29;
        TIME[0][2][3] = 10;
        TIME[0][2][4] = 18;
        TIME[0][2][5] = 85;

        TIME[0][3][0] = 33;
        TIME[0][3][1] = 38;
        TIME[0][3][2] = 10;
        TIME[0][3][4] = 27;
        TIME[0][3][5] = 92;

        TIME[0][4][0] = 18;
        TIME[0][4][1] = 23;
        TIME[0][4][2] = 19;
        TIME[0][4][3] = 28;
        TIME[0][4][5] = 83;

        TIME[0][5][0] = 86;
        TIME[0][5][1] = 87;
        TIME[0][5][2] = 86;
        TIME[0][5][3] = 96;
        TIME[0][5][4] = 84;

        //taxi
        TIME[1][0][1] = 3;
        TIME[1][0][2] = 14;
        TIME[1][0][3] = 19;
        TIME[1][0][4] = 8;
        TIME[1][0][5] = 30;

        TIME[1][1][0] = 6;
        TIME[1][1][2] = 13;
        TIME[1][1][3] = 18;
        TIME[1][1][4] = 8;
        TIME[1][1][5] = 29;

        TIME[1][2][0] = 12;
        TIME[1][2][1] = 14;
        TIME[1][2][3] = 9;
        TIME[1][2][4] = 11;
        TIME[1][2][5] = 31;

        TIME[1][3][0] = 13;
        TIME[1][3][1] = 14;
        TIME[1][3][2] = 4;
        TIME[1][3][4] = 12;
        TIME[1][3][5] = 32;

        TIME[1][4][0] = 7;
        TIME[1][4][1] = 8;
        TIME[1][4][2] = 9;
        TIME[1][4][3] = 14;
        TIME[1][4][5] = 30;

        TIME[1][5][0] = 32;
        TIME[1][5][1] = 29;
        TIME[1][5][2] = 32;
        TIME[1][5][3] = 36;
        TIME[1][5][4] = 30;

        //by foot
        TIME[2][0][1] = 14;
        TIME[2][0][2] = 69;
        TIME[2][0][3] = 76;
        TIME[2][0][4] = 28;
        TIME[2][0][5] = 269;

        TIME[2][1][0] = 14;
        TIME[2][1][2] = 81;
        TIME[2][1][3] = 88;
        TIME[2][1][4] = 39;
        TIME[2][1][5] = 264;

        TIME[2][2][0] = 69;
        TIME[2][2][1] = 81;
        TIME[2][2][3] = 12;
        TIME[2][2][4] = 47;
        TIME[2][2][5] = 270;

        TIME[2][3][0] = 76;
        TIME[2][3][1] = 88;
        TIME[2][3][2] = 12;
        TIME[2][3][4] = 55;
        TIME[2][3][5] = 285;

        TIME[2][4][0] = 28;
        TIME[2][4][1] = 39;
        TIME[2][4][2] = 47;
        TIME[2][4][3] = 55;
        TIME[2][4][5] = 264;

        TIME[2][5][0] = 269;
        TIME[2][5][1] = 264;
        TIME[2][5][2] = 270;
        TIME[2][5][3] = 285;
        TIME[2][5][4] = 264;
    }

    private static int i,j,k; //counter

    protected static int[][] fastestByPublic(int[] destinationsQueue) {
        //find fastest by public transport and return a 2d array describing
        //the trip. the child array will always have 3 members: first is method (0),
        //second is from, third is to.
        int[] destinations = new int[destinationsQueue.length + 1];
        destinations[0] = 0;
        for (i = 1; i < destinations.length; i++) {
            destinations[i] = destinationsQueue[i-1];
        }

        int[][] result = new int[destinations.length][3];
        int min, temp;
        for (i = 0; i < destinations.length - 1; i++) {
            min = i+1;
            for (j = min; j < destinations.length; j++) {
                if ( TIME[0][destinations[i]][destinations[j]] < TIME[0][destinations[i]][destinations[min]] ) min = j;
            }
            temp = destinations[min];
            destinations[min] = destinations[i+1];
            destinations[i+1] = temp;
            result[i] = new int[]{0, destinations[i], destinations[i+1]};
        }
        result[destinations.length - 1] = new int[]{0, destinations[destinations.length-1], destinations[0]};
        return result;
    }

    protected static double totalCost(int[][] currentTrip) {
        double result = 0;
        for ( int[] t : currentTrip) {
            result += COST[t[0]][t[1]][t[2]];
        }
        return round(result, 2);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    protected static int[][] flipToTaxi(int[][] currentTrip, double budget) {
        //return the new trip from the make-most-sense to the make-least-sense
        // routes flipped to taxi measured by minute saved per extra dollar
        //keep it within budget
        double[] effArray = new double[currentTrip.length];
        HashMap<Double, Integer> effMap = new HashMap<Double, Integer>();
        for (i = 0; i < currentTrip.length; i++) {
            double timeDiff = TIME[0][currentTrip[i][1]][currentTrip[i][2]] - TIME[1][currentTrip[i][1]][currentTrip[i][2]];
            double moneyDiff = COST[1][currentTrip[i][1]][currentTrip[i][2]] - COST[0][currentTrip[i][1]][currentTrip[i][2]];
            double efficiency = timeDiff / moneyDiff;
            if ( efficiency < 0 ) efficiency *= -1000;
            effArray[i] = efficiency;
            effMap.put(efficiency, i);
        }

        Arrays.sort(effArray);
        int[] effFlips = new int[currentTrip.length]; //arrays of effcient flips from least to most
        for (i = 0; i < effArray.length; i++) {
            effFlips[i] = effMap.get(effArray[i]);
        }

        int[][] newTrip = twoDclone(currentTrip);
        i = effArray.length - 1;
        while ( totalCost(newTrip) < budget  && i > -1 ) {
            currentTrip = twoDclone(newTrip);
            newTrip[effFlips[i]][0] = 1;
            i--;
        }
        if ( totalCost(newTrip) < budget ) currentTrip = twoDclone(newTrip);
        return currentTrip;
    }

    protected static int[][] flipToFoot(int[][] currentTrip, double budget) {
        //return the new trip from the make-most-sense to the make-least-sense
        // routes flipped to foot measured by dollar saved per extra minute
        //keep it just right within budget, not going to far
        double[] effArray = new double[currentTrip.length];
        HashMap<Double, Integer> effMap = new HashMap<Double, Integer>();
        for (i = 0; i < currentTrip.length; i++) {
            double timeDiff = TIME[2][currentTrip[i][1]][currentTrip[i][2]] - TIME[0][currentTrip[i][1]][currentTrip[i][2]];
            double moneyDiff = COST[0][currentTrip[i][1]][currentTrip[i][2]];
            double efficiency = moneyDiff / timeDiff;
            if ( efficiency < 0 ) efficiency *= -1000;
            effArray[i] = efficiency;
            effMap.put(efficiency, i);
        }

        Arrays.sort(effArray);
        int[] effFlips = new int[currentTrip.length];
        for (i = 0; i < effArray.length; i++) {
            effFlips[i] = effMap.get(effArray[i]);
        }

        int[][] newTrip = twoDclone(currentTrip);
        i = currentTrip.length - 1;
        while ( totalCost(newTrip) > budget) {
            newTrip[effFlips[i]][0] = 2;
            currentTrip = twoDclone(newTrip);
            i--;
        }
        return currentTrip;
    }

    protected static int[][] getMostOptimalTrip(int[] destinationsQueue, double budget) {
        int[][] publicTransportTrip = fastestByPublic(destinationsQueue);
        int[][] optimizedTrip;
        if (totalCost(publicTransportTrip) > budget) optimizedTrip = flipToFoot(publicTransportTrip, budget);
        else if (totalCost(publicTransportTrip) < budget) optimizedTrip = flipToTaxi(publicTransportTrip, budget);
        else optimizedTrip = publicTransportTrip;
        return optimizedTrip;
    }

    public static int[][] twoDclone(int[][] obs) {
        int[][] result = new int[obs.length][obs[0].length];
        for (int i=0; i<obs.length; i++) {
            for (int j=0; j<obs[0].length; j++) {
                result[i][j] = obs[i][j];
            }
        }
        return result;
    }
}
