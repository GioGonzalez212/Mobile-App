package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FavoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_favorites);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener()  {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_signin:
                        Intent profileIntent =  new Intent( FavoriteActivity.this, ProfileActivity.class);
                        startActivity(profileIntent);
                        break;
                    case R.id.action_stocks:
                        Intent stockIntent = new Intent(FavoriteActivity.this,StocksActivity.class);
                        startActivity(stockIntent);
                        break;
                    case R.id.action_favorites:
                        Toast.makeText(FavoriteActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_news:
                        Intent newsIntent = new Intent(FavoriteActivity.this,NewsActivity.class);
                        startActivity(newsIntent);
                        break;
                }
                return true;
            }
        });


    }
}