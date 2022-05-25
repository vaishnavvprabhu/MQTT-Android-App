package com.symbivvp.esp8266_adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;



import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
<<<<<<< Updated upstream
=======
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
>>>>>>> Stashed changes
import com.symbivvp.esp8266_adapter.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
<<<<<<< Updated upstream
    ArrayList<String> humlist;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    ArrayAdapter<String> adapter;
=======
    ArrayList<String> humlist, oldhumlist, datelist;
    ArrayList<BarEntry> barEntries;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    ArrayAdapter<String> adapter, oldAdapter;
    Button button;

    BarChart barChart;
    LineChart lineChart;
    ListView listview;
>>>>>>> Stashed changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        System.out.println("Calling fetchData() Function");
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        initializeList();
        new fetchJSONData().start();
<<<<<<< Updated upstream
=======
        barChart = (BarChart) findViewById(R.id.chart);
        button = (Button) findViewById(R.id.button);
        ArrayList<Entry> ysin = new ArrayList();
        ArrayList<Entry> ycos = new ArrayList();


        //new fetchOlderJSONData().start();
>>>>>>> Stashed changes

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               listview = findViewById(R.id.humlistview);
               listview.setVisibility(View.VISIBLE);
            }
        });

        // Refresh  the layout
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // Your code goes here
                        // In this code, we are just
                        // changing the text in the textbox
                        initializeList();
                        new fetchJSONData().start();

                        // This line is important as it explicitly
                        // refreshes only once
                        // If "true" it implicitly refreshes forever
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }


    private void initializeList() {
        humlist = new ArrayList<>();
<<<<<<< Updated upstream
=======
        oldhumlist = new ArrayList<>();
        datelist = new ArrayList<>();
        barEntries = new ArrayList<>();
>>>>>>> Stashed changes
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, humlist);
        binding.humlistview.setAdapter(adapter);
    }

    public void fetchData(){
        String lightApi = "https://api.thingspeak.com/channels/1714621/fields/1.json?api_key=G63QV5S759X5OXZ9&results=2";
        JsonObjectRequest objectRequest =new JsonObjectRequest(Request.Method.GET, lightApi, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println("Entered Try Block");
                            JSONArray feeds = response.getJSONArray("feeds");
                            for(int i=0; i<feeds.length();i++){
                                JSONObject jo = feeds.getJSONObject(i);
                                String l=jo.getString("field1");
                                System.out.println("Value of field 1: "+l);
                                Toast.makeText(getApplicationContext(),l, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error" + error);
            }
        });
    }

    class fetchJSONData extends Thread{
        String data="";
                    @Override
                    public void run() {

                        mainHandler.post(new Runnable() {
                            @Override
                            public void run(){
                                progressDialog = new ProgressDialog(MainActivity.this);
                                progressDialog.setMessage("Fetching Data From ThingSpeak API");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                            }
                        });
                        try {
                            System.out.println("Entered Try Block");
                            URL url = new URL ("https://api.thingspeak.com/channels/1714621/feeds.json?api_key=G63QV5S759X5OXZ9&results=1");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            InputStream inputStream = conn.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                            String line;

                            while ((line = reader.readLine()) != null)
                            {
                                data = data + line;
                            }

                            if(!data.isEmpty())
                            {
                                JSONObject jsonObject = new JSONObject(data);
                                JSONArray value = jsonObject.getJSONArray("feeds");
                                System.out.println(jsonObject.names());
                                humlist.clear();
                                for (int i = 0; i < value.length(); i++)
                                {
                                JSONObject value1=value.getJSONObject(i);
                                String date = value1.getString("created_at");
                                String field1 = value1.getString("field1");
                                String field2 = value1.getString("field2");

<<<<<<< Updated upstream
                                humlist.add(date);
                                humlist.add(field1);
                                humlist.add(field2);
=======
                                datelist.add(date);

                                barEntries.add(new BarEntry(Float.parseFloat(field1),i));
>>>>>>> Stashed changes
                                }
                                BarDataSet barDataSet = new BarDataSet(barEntries,"Humidity");
                                BarData data = new BarData (datelist,barDataSet);
                                barChart.setData(data);
                                barChart.setFocusableInTouchMode(false);
                                // no description text
                                barChart.setDescription("");

                                // enable touch gestures
                                barChart.setTouchEnabled(true);

                                ///barChart.setDragDecelerationFrictionCoef(0.9f);

                                // enable scaling and dragging
                                barChart.setDragEnabled(false);
                                barChart.setScaleEnabled(false);
                                barChart.setDrawGridBackground(false);
                                barChart.setHighlightPerDragEnabled(false);

                                // if disabled, scaling can be done on x- and y-axis separately
                                barChart.setPinchZoom(false);

                                // get the legend (only possible after setting data)
                                Legend l = barChart.getLegend();
                                barDataSet.setColors(new int[] {Color.RED, Color.GREEN, Color.GRAY, Color.BLACK, Color.BLUE});
                                //barChart.setDrawMarkerViews(false);
                                // modify the legend ...
                                l.setForm(Legend.LegendForm.LINE);

                                l.setTextSize(11f);
                                l.setTextColor(Color.BLACK);
                                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                                l.setDrawInside(false);

//        l.setYOffset(11f);

                                XAxis xAxis = barChart.getXAxis();

                                xAxis.setTextSize(11f);
                                xAxis.setTextColor(Color.WHITE);
                                xAxis.setDrawGridLines(false);
                                xAxis.setDrawAxisLine(false);

                                YAxis leftAxis = barChart.getAxisLeft();

                                leftAxis.setTextSize(14f);
                                leftAxis.setTextColor(Color.BLACK);
                                leftAxis.setDrawGridLines(true);
                                leftAxis.setGranularityEnabled(true);

                                YAxis rightAxis = barChart.getAxisRight();

                                rightAxis.setTextSize(18f);
                                rightAxis.setTextColor(Color.RED);
                                rightAxis.setDrawGridLines(false);
                                rightAxis.setDrawZeroLine(false);
                                rightAxis.setGranularityEnabled(false);
                                rightAxis.setEnabled(false);



                        }
                        }
                        catch(MalformedURLException me){
                            me.printStackTrace();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mainHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                if (progressDialog.isShowing())
                                {
                                    progressDialog.dismiss();
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
<<<<<<< Updated upstream
=======

    class fetchOlderJSONData extends Thread {
        String data = "";

        @Override
        public void run() {

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Fetching Data From ThingSpeak API");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });
            try {
                System.out.println("Entered Try Block");
                URL url = new URL("https://api.thingspeak.com/channels/1714621/feeds.json?api_key=G63QV5S759X5OXZ9&results=6");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    data = data + line;
                }

                if (!data.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray value = jsonObject.getJSONArray("feeds");
                    System.out.println(jsonObject.names());
                    oldhumlist.clear();
                    for (int i = 0; i < value.length(); i++) {
                        JSONObject value1 = value.getJSONObject(i);
                        String date = value1.getString("created_at");
                        String field1 = value1.getString("field1");
                        String field2 = value1.getString("field3");

                        oldhumlist.add("Date: " + date);
                        oldhumlist.add("Humidity (%): " + field1);
                        oldhumlist.add("Pump State: " + field2);
                    }
                }
            } catch (MalformedURLException me) {
                me.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mainHandler.post(new Runnable() {

                @Override
                public void run() {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }
>>>>>>> Stashed changes
    }
