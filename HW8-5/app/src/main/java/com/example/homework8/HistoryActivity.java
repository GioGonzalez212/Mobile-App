package com.example.homework8;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.icu.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HistoryActivity extends AppCompatActivity {

    //OpenWeatherMaps API info
    public static final String WEATHER_API_KEY = "9b62e565e692ab0a108fdeef89e96980";
    public static final String URL = "https://api.openweathermap.org/data/2.5/onecall/timemachine?";
    private static final DecimalFormat df =  new DecimalFormat("0.00");

    private String TAG = HistoryActivity.class.getSimpleName();
    private ListView lv;

    double lat, lon;
    String dateTime;

    ArrayList<HashMap<String, String>> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Intent intent = getIntent();
        lat = Double.parseDouble(intent.getStringExtra("latitude"));
        lon = Double.parseDouble(intent.getStringExtra("longitude"));
        dateTime = intent.getStringExtra("date");




        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_history);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener()  {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent mainIntent =  new Intent( HistoryActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        break;
                    case R.id.action_results:
                        Toast.makeText(HistoryActivity.this, "Go to home to search again!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_map:
                        Toast.makeText(HistoryActivity.this, "Go to home to search again!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_history:
                        Toast.makeText(HistoryActivity.this, "History", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        historyList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        new GetHistoryList().execute();
    }
    private class GetHistoryList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(HistoryActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response

            String NewURL = URL + "lat=" + lat + "&lon=" + lon + "&dt=" + dateTime + "&appid=" + WEATHER_API_KEY;
            String jsonStr = sh.makeServiceCall(NewURL);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray hourly = jsonObj.getJSONArray("hourly");


                    // looping through All hourly length
                    for (int i = 0; i < hourly.length(); i++) {
                        JSONObject c = hourly.getJSONObject(i);
                        String date = c.getString("dt");
                        String temperature = c.getString("temp");

                        String DateElement = unix_to_human(date);
                        //string for temp
                        String TempElement = "Temperature: " + k_to_f(temperature);


                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("dt", DateElement);
                        contact.put("temp", TempElement);

                        // adding contact to contact list
                        historyList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. " + NewURL + "\nCheck LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(HistoryActivity.this, historyList,
                    R.layout.list_item, new String[]{ "dt", "temp"},
                    new int[]{R.id.date_History, R.id.temp_History});
            lv.setAdapter(adapter);
        }

        public String k_to_f(String temp){
            double kelvin = Double.parseDouble(temp);
            double C = kelvin - 273.15;
            double f = C * 1.8 + 32;

            return df.format(f) + "°F";
        }//end of F calculation

        @RequiresApi(api = Build.VERSION_CODES.N)
        public String unix_to_human(String value){
            //conver seconds to milliseconds
            Date date = new java.util.Date(Integer.parseInt(value)*1000L);
            //the format of the date

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm aa");
            // give a timezone reference for formatting (see comment at the bottom)
            String formattedDate = sdf.format(date);
            return formattedDate;

        }//end of unix_to_human
    }
}
