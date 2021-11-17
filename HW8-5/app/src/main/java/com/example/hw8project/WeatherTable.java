package com.example.hw8project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class weather_information extends AppCompatActivity {
    //declaring all the items that are going to be saved form the JSON reponse
    public String zip_OR_city, temp, feel_like, min_Temp, max_Temp, humidity, pressure, lat, lon, country, description, sunrise, sunset;
    TextView weather_Tittle, CO, feels, minTemp, maxTemp, humid, press, desc, sunriseT, sunsetT;
    private static final DecimalFormat df =  new DecimalFormat("0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_information);

        // get intent from other activity and get string
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        //declare items
        weather_Tittle = (TextView) findViewById(R.id.weather_Tittle);
        CO = (TextView) findViewById(R.id.country_Value);
        feels = (TextView) findViewById(R.id.feelsLike_Value);
        minTemp = (TextView) findViewById(R.id.minTemp_Value);
        maxTemp = (TextView) findViewById(R.id.maxTemp_Value);
        humid =  (TextView) findViewById(R.id.humidity_Value);
        press = (TextView) findViewById(R.id.pressure_Value);
        desc = (TextView) findViewById(R.id.desc_Value);
        sunriseT = (TextView) findViewById(R.id.sunrise_Value);
        sunsetT = (TextView) findViewById(R.id.sunset_Value);
        //declaring button
        Button map_Button = (Button) findViewById(R.id.map_BTN);

        //doing the request with volley
        RequestQueue queue = Volley.newRequestQueue(this);

        //request a string from the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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

        map_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent =  new Intent( weather_information.this, MapsActivity.class);
                mapIntent.putExtra("latitude", lat);
                mapIntent.putExtra("longitude", lon);
                mapIntent.putExtra("city", zip_OR_city);
                startActivity(mapIntent);
            }
        });

    }// end of onCreate

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
                feel_like = main.getString("feels_like");
                min_Temp = main.getString("temp_min");
                max_Temp = main.getString("temp_max");
                humidity = main.getString("humidity");
                pressure = main.getString("pressure");

                //get coord part of the JSON
                JSONObject coord = new JSONObject(jsonObject.getString("coord"));
                lon = coord.getString("lon");
                lat = coord.getString("lat");

                //get the sys part of the JSON
                JSONObject sys = new JSONObject(jsonObject.getString("sys"));
                country = sys.getString("country");
                sunrise = sys.getString("sunrise");
                sunset = sys.getString("sunset");

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

    public void place_info(){
        //putting all the necessary information on the activity
        weather_Tittle.setText("Weather For " + zip_OR_city + ":");
        CO.setText(country);
        feels.setText(farenheit_calculation(feel_like));
        minTemp.setText(farenheit_calculation(min_Temp));
        maxTemp.setText(farenheit_calculation(max_Temp));
        humid.setText(humidity + "%");
        press.setText(pressure + " hPa");
        desc.setText(description);
        sunriseT.setText(unix_to_human(sunrise));
        sunsetT.setText(unix_to_human(sunset));
    } // end of place_info

    public String unix_to_human(String value){
        long unixSeconds = Long.parseLong(value);
        //conver seconds to milliseconds
        Date date = new java.util.Date(unixSeconds*1000L);
        //the format of the date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat(" HH:mm:ss ");
        // give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
        String formattedDate = sdf.format(date);
        return formattedDate;

    }//end of unix_to_human

    public String farenheit_calculation(String temp){
        double kelvin = Double.parseDouble(temp);
        double C = kelvin - 273.15;
        double f = C * 1.8 + 32;
        return df.format(f) + "°F";
    }//end of k_to_c

} //end of weather_information

