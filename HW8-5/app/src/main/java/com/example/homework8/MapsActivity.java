package com.example.homework8;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.homework8.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    double lat, lon;
    String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        //Initialize and Assign Variables
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //Set Home Selector
        bottomNavigationView.setSelectedItemId(R.id.ic_map);
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
        Intent intent = getIntent();
        lat = Double.parseDouble(intent.getStringExtra("latitude"));
        lon = Double.parseDouble(intent.getStringExtra("longitude"));
        city = intent.getStringExtra("city");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        assert mapFragment != null;
        mapFragment.getMapAsync(MapsActivity.this);

    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(location).title(city));
        float zoomLevel = 12.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
    }
}