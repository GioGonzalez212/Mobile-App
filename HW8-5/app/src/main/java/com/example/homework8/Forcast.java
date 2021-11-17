package com.example.homework8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Forcast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forcast);

        //Initialize and Assign Variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home Selector
        bottomNavigationView.setSelectedItemId(R.id.ic_calender);
        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_weather:
                        startActivity(new Intent(getApplicationContext(),
                                WeatherTable.class));
                        overridePendingTransition(0,0);
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
                        return true;
                }
                return false;
            }
        });
    }
}