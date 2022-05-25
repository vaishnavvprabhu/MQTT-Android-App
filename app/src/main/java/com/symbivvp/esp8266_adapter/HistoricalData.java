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
import android.widget.Toast;



import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class HistoricalData extends AppCompatActivity {

    ActivityMainBinding bindingnew;
    ArrayList<String> humlist, oldhumlist, datelist;
    ArrayList<BarEntry> barEntries;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;
    ArrayAdapter<String> adapter, oldAdapter;
    Button button;

    BarChart barChart;
    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_historical_data);

        bindingnew = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_historical_data);
        System.out.println("Calling fetchData() Function");
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        initializeList();
        new fetchJSONData().start();
        barChart = (BarChart) findViewById(R.id.chart);
        button = (Button) findViewById(R.id.button);
        ArrayList<Entry> ysin = new ArrayList();
        ArrayList<Entry> ycos = new ArrayList();

        //new fetchOlderJSONData().start();

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
        //oldhumlist = new ArrayList<>();
        datelist = new ArrayList<>();
        barEntries = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, humlist);
        //oldAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, oldhumlist);
        //bindingnew.olddata.setAdapter(adapter);
        //bindingnew.olddata.setAdapter(oldAdapter);
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
                    progressDialog = new ProgressDialog(HistoricalData.this);
                    progressDialog.setMessage("Fetching Data From ThingSpeak API");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });
            try {
                System.out.println("Entered Try Block");
                URL url = new URL ("https://api.thingspeak.com/channels/1714621/feeds.json?api_key=G63QV5S759X5OXZ9&results=10");
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
                        String field2 = value1.getString("field3");

                        humlist.add("Date: "+date);
                        humlist.add("Humidity (%): "+field1);
                        humlist.add("Pump State: "+ field2);

                    }


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

    class fetchOlderJSONData extends Thread {
        String data = "";

        @Override
        public void run() {

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(HistoricalData.this);
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
}
