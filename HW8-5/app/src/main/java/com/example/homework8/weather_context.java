package com.example.homework8;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class weather_context extends AppCompatActivity {
    //declaring all the items that are going to be saved form the JSON reponse
    public String zip_OR_city, dateTime, temp, min_Temp, max_Temp, humidity, pressure, lat, lon, country, description, sunrise, sunset;
    TextView weather_Title, CO, minTemp, maxTemp, humid, press, sunriseT, sunsetT, longitude, latitude,temperature;
    private static final DecimalFormat df =  new DecimalFormat("0.00");

    //text to speech
    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);

        //assign t1 for the speech
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_results);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener()  {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent mainIntent =  new Intent( weather_context.this, MainActivity.class);
                        startActivity(mainIntent);
                        break;
                    case R.id.action_results:
                        //Speak for Weather Title
                        String toSpeak = weather_Title.getText().toString();
                        Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                        break;
                    case R.id.action_map:
                        Intent mapIntent =  new Intent( weather_context.this, MapsActivity.class);
                        mapIntent.putExtra("latitude", lat);
                        mapIntent.putExtra("longitude", lon);
                        mapIntent.putExtra("city", zip_OR_city);
                        mapIntent.putExtra("date", dateTime);
                        startActivity(mapIntent);
                        break;
                    case R.id.action_history:
                        Intent historyIntent =  new Intent( weather_context.this, HistoryActivity.class);
                        historyIntent.putExtra("latitude", lat);
                        historyIntent.putExtra("longitude", lon);
                        historyIntent.putExtra("date", dateTime);
                        startActivity(historyIntent);
                        break;
                }
                return true;
            }
        });

        // get intent from other activity and get string
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        //declare items
        weather_Title = (TextView) findViewById(R.id.weather_Title);
        press = (TextView) findViewById(R.id.pressure_Value);
        temperature = (TextView) findViewById(R.id.temperature_Value);
        minTemp = (TextView) findViewById(R.id.minTemp_Value);
        maxTemp = (TextView) findViewById(R.id.maxTemp_Value);
        humid =  (TextView) findViewById(R.id.humidity_Value);
        longitude = (TextView) findViewById(R.id.longi_Value);
        latitude = (TextView) findViewById(R.id.latitu_Value);
        sunriseT = (TextView) findViewById(R.id.sunrise_Value);
        sunsetT = (TextView) findViewById(R.id.sunset_Value);


        //doing the request with volley
        RequestQueue queue = Volley.newRequestQueue(this);

        //request a string from the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                handleResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong, please go back and check your input", Toast.LENGTH_LONG).show();
            }
        });

        //add the request to the RequestQueue
        queue.add(stringRequest);


    }// end of onCreate

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void handleResponse(String response){
        //check if response is empty
        if( response != null){
            Log.e("TAG", response);
            try{
                //start parsing JSON object
                JSONObject jsonObject =  new JSONObject(response);

                //get name of the place
                zip_OR_city = jsonObject.getString("name");

                //in main part of the JSON
                JSONObject main = new JSONObject(jsonObject.getString("main"));
                temp = main.getString("temp");
                min_Temp = main.getString("temp_min");
                max_Temp = main.getString("temp_max");
                humidity = main.getString("humidity");
                pressure = main.getString("pressure");

                dateTime = jsonObject.getString("dt");

                //get coord part of the JSON
                JSONObject coord = new JSONObject(jsonObject.getString("coord"));
                lon = coord.getString("lon");
                lat = coord.getString("lat");

                //get the weather part of the JSON
                JSONArray weather = jsonObject.getJSONArray("weather");
                JSONObject weather_C = weather.getJSONObject(0);
                description = weather_C.getString("description");
                place_info();
            } catch (JSONException e) {
                //not reading correctly the json
                Log.e("TAG", "json parsing error: " + e.getMessage());
            }
        } else{
            Log.e("TAG", "response is empty ");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Couldn't get json from server. Check LogCat for possible errors!",Toast.LENGTH_SHORT).show();
                }
            });
        }
    } // end of handleResponse

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void place_info(){
        //putting all the necessary information on the activity
        weather_Title.setText("Weather For " + zip_OR_city + ":");
        press.setText(pressure + " hPa");
        temperature.setText(k_to_f(temp));
        minTemp.setText(k_to_f(min_Temp));
        maxTemp.setText(k_to_f(max_Temp));
        humid.setText(humidity + "%");
        longitude.setText(lon);
        latitude.setText(lat);
        sunriseT.setText(unix_to_human(sunrise));
        sunsetT.setText(unix_to_human(sunset));
    } // end of place_info

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String unix_to_human(String value){
        //conver seconds to milliseconds
        Date date = new java.util.Date(Integer.parseInt(value)*1000L);
        //the format of the date

        SimpleDateFormat sdf = new SimpleDateFormat("h:mm aa");
        // give a timezone reference for formatting (see comment at the bottom)
        String formattedDate = sdf.format(date);
        return formattedDate;

    }//end of unix_to_human

    public String k_to_f(String temp){
        double kelvin = Double.parseDouble(temp);
        double C = kelvin - 273.15;
        double f = C * 1.8 + 32;

        return df.format(f) + "°F";
    }//end of F calculation



} //end of weather_information