package com.example.homework8;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.Date;

public class WeatherTable extends AppCompatActivity {
    public String zip_OR_city, temp, feel_like, min_Temp, max_Temp, humidity, pressure, lat, lon, country, description, sunrise, sunset;
    TextView weather_Title, CO, feels, minTemp, maxTemp, humid, press, desc, sunriseT, sunsetT;
    private static final DecimalFormat df =  new DecimalFormat("0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get intent from other activity and get string
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");


        setContentView(R.layout.activity_weather_content);
        //Initialize and Assign Variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home Selector
        bottomNavigationView.setSelectedItemId(R.id.ic_weather);
        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_weather:
                        return true;
                    case R.id.ic_home:
                        startActivity(new Intent(getApplicationContext(),
                                MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ic_map:
                        startActivity(new Intent(getApplicationContext(),
                                MapsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.ic_calender:
                        startActivity(new Intent(getApplicationContext(),
                                Forcast.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        //declare items
        weather_Title = findViewById(R.id.weather_Title);
        CO = findViewById(R.id.country_Value);
        feels =  findViewById(R.id.feelsLike_Value);
        minTemp =  findViewById(R.id.minTemp_Value);
        maxTemp =  findViewById(R.id.maxTemp_Value);
        humid = findViewById(R.id.humidity_Value);
        press = findViewById(R.id.pressure_Value);
        desc = findViewById(R.id.desc_Value);
        sunriseT = findViewById(R.id.sunrise_Value);
        sunsetT = findViewById(R.id.sunset_Value);
        //declaring button
        Button map_Button = findViewById(R.id.map_BTN);

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

        map_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent =  new Intent( getApplicationContext(), MapsActivity.class);
                mapIntent.putExtra("latitude", lat);
                mapIntent.putExtra("longitude", lon);
                mapIntent.putExtra("city", zip_OR_city);
                startActivity(mapIntent);
            }
        });

    }//end of on create
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void place_info(){
        //putting all the necessary information on the activity
        weather_Title.setText("Weather For " + zip_OR_city + ":");
        CO.setText(country);
        feels.setText(k_to_f(feel_like));
        minTemp.setText(k_to_f(min_Temp));
        maxTemp.setText(k_to_f(max_Temp));
        humid.setText(humidity + "%");
        press.setText(pressure + " hPa");
        desc.setText(description);
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
}