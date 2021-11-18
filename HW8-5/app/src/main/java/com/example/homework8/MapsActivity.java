package com.example.homework8;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;


import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.homework8.databinding.ActivityMapsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    double lat, lon;
    String city,dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_main);


        Intent intent = getIntent();
        lat = Double.parseDouble(intent.getStringExtra("latitude"));
        lon = Double.parseDouble(intent.getStringExtra("longitude"));
        dateTime = intent.getStringExtra("date");
        city = intent.getStringExtra("city");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_map);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener()  {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Intent mainIntent =  new Intent( MapsActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        break;
                    case R.id.action_results:
                        Toast.makeText(MapsActivity.this, "Go to home to search again!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_map:
                        Toast.makeText(MapsActivity.this, "Map", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_history:
                        Intent historyIntent =  new Intent( MapsActivity.this, HistoryActivity.class);
                        historyIntent.putExtra("latitude", intent.getStringExtra("latitude"));
                        historyIntent.putExtra("longitude", intent.getStringExtra("longitude"));
                        historyIntent.putExtra("date", intent.getStringExtra("date"));
                        startActivity(historyIntent);
                        break;
                }
                return true;
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(location).title(city));
        float zoomLevel = 12.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
    }
}