package com.example.homework8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //OpenWeatherMaps API info
    public static final String WEATHER_API_KEY = "9b62e565e692ab0a108fdeef89e96980";
    public static final String URL = "https://api.openweathermap.org/data/2.5/weather?appid=";
    //Intent needed
    public static final String EXTRA_MESSAGE = "com./*EXAMPLE*/.weatherrams.MESSAGE";
    //location GPS part
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    //format for the GPS location to two decimal places
    private static final DecimalFormat df =  new DecimalFormat("0.00");

    //Speech to text
    private final int REQ_CODE = 100;
    TextView textView;

    //GPS tracker
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener()  {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_results:
                        Toast.makeText(MainActivity.this, "input in search to get results", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_map:
                        Toast.makeText(MainActivity.this, "input in search to get map", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_history:
                        Toast.makeText(MainActivity.this, "input in search to get history", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        //declaration of needed items
        EditText userIN = (EditText) findViewById(R.id.user_input);
        ImageButton go_Button = (ImageButton) findViewById(R.id.goButton);
        ImageButton location_Button = (ImageButton) findViewById(R.id.LocationButton);

        // getting user geolocation permission
        try{
            if (ActivityCompat.checkSelfPermission(this, mPermission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);
                /*if the permission is not granted by the user, this condition
                 * will execute everytime, otherwise your else part will work */
            }
        } catch (Exception e) {
            Log.e("TAG","Exception is: " + e.getMessage());
        }


        //adding listener to go button
        go_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String City_Zip = userIN.getText().toString();
                //check if user input is empty
                if (City_Zip.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Can not enter and empty value \n - Try again.", Toast.LENGTH_SHORT).show();
                } else { //if not empty then
                    //check if city or zip
                    if (City_Zip.matches("\\d+(?:\\.\\d+)?")){
                        //if number then check length --> it must be 5 numbers
                        Log.e("TAG", "its a Zip-code");
                        if (City_Zip.length() == 5){
                            //correct zip format
                            onZipGO(City_Zip);
                        } else{
                            //incorrect length show message
                            Toast.makeText(getApplicationContext(), "Incorrect Zip format \n - Try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("TAG", "its a City ");
                        onCityGO(City_Zip);
                    }

                }
            }
        });

        //add listener to location button
        location_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(MainActivity.this);

                //check if gps is enabled
                if (gps.canGetLocation()){
                    String lat = df.format(gps.getLatitude());
                    String lon = df.format(gps.getLongitude());
                    Log.e("TAG", "[+] lat is: "  + lat + "\n [+] lon is: " + lon);
                    onLocationGO(lat, lon);
                } else {
                    //cant get location
                    // GPS or network is not enable
                    // Ask user to enable GPS or check settings
                    gps.showSettingsAlert();
                }
            }
        });

        //add listener to speech to text button
        textView = findViewById(R.id.user_input);
        ImageView speak = findViewById(R.id.MicButton);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    } // end of onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textView.setText((CharSequence) result.get(0));
                }
                break;
            }
        }
    }

    public void onZipGO(String value){
        //creating url for ZIP
        String url = URL + WEATHER_API_KEY + "&zip=" + value;
        Log.e("TAG","the url is: " + url);
        sendURL(url);
    } // end of onZipGO

    public void onCityGO(String value){
        //creating the url for City
        String url = URL + WEATHER_API_KEY + "&q=" + value;
        Log.e("TAG", "the url is: " + url);
        sendURL(url);
    }// end of onCityGO

    public void onLocationGO(String lat, String lon){
        //creating url for geolocation
        String url = URL + WEATHER_API_KEY + "&lat=" + lat + "&lon=" + lon;
        Log.e("TAG", "the url is: " + url);
        sendURL(url);
    } //end of onLocationGO

    public void sendURL(String url){
        Toast.makeText(getApplicationContext(), "Getting requested information", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),weather_context.class);
        intent.putExtra("url", url);
        startActivity(intent);
    } //end of sendURL

}
