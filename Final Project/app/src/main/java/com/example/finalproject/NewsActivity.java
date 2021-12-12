package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView newsRV;
    private EditText searchEdt;
    private ArrayList<News> newsModalArrayList;
    private NewsAdapter NewsRVAdapter;
    private ProgressBar loadingPB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main);
        searchEdt = findViewById(R.id.etNSearch);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_news);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener()  {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_signin:
                        Intent profileIntent =  new Intent( NewsActivity.this, ProfileActivity.class);
                        startActivity(profileIntent);
                        break;
                    case R.id.action_stocks:
                        Intent stockIntent = new Intent(NewsActivity.this,StocksActivity.class);
                        startActivity(stockIntent);
                        break;
                    case R.id.action_favorites:
                        Toast.makeText(NewsActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_news:
                        Toast.makeText(NewsActivity.this, "News", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        // initializing all our variables and array list.
        loadingPB = findViewById(R.id.idPBNLoading);
        newsRV = findViewById(R.id.Nlist);
        newsModalArrayList = new ArrayList<>();

        // initializing our adapter class.
        NewsRVAdapter = new NewsAdapter(newsModalArrayList, this);

        // setting layout manager to recycler view.
        newsRV.setLayoutManager(new LinearLayoutManager(this));

        // setting adapter to recycler view.
        newsRV.setAdapter(NewsRVAdapter);

        // calling get data method to get data from API.
        getData();


        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // on below line calling a
                // method to filter our array list
                filter(s.toString());
            }
        });

    }//end of OnCreate

    private void filter(String filter) {
        // on below line we are creating a new array list
        // for storing our filtered data.
        ArrayList<News> filteredlist = new ArrayList<>();
        // running a for loop to search the data from our array list.
        for (News item : newsModalArrayList) {
            // on below line we are getting the item which are
            // filtered and adding it to filtered list.
            if (item.getName().toLowerCase().contains(filter.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        // on below line we are checking
        // weather the list is empty or not.
        if (filteredlist.isEmpty()) {
            // if list is empty we are displaying a toast message.
            Toast.makeText(this, "No currency found..", Toast.LENGTH_SHORT).show();
        } else {
            // on below line we are calling a filter
            // list method to filter our list.
            NewsRVAdapter.filterList(filteredlist);
        }
    }

    private void getData() {
        // creating a variable for storing our string.
        String url = "https://api.cryptoapis.io/v1/assets";
        // creating a variable for request queue.
        RequestQueue queue = Volley.newRequestQueue(this);
        // making a json object request to fetch data from API.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // inside on response method extracting data
                // from response and passing it to array list
                // on below line we are making our progress
                // bar visibility to gone.
                loadingPB.setVisibility(View.GONE);
                try {
                    // extracting data from json.
                    JSONArray dataArray = response.getJSONArray("payload");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObj = dataArray.getJSONObject(i);
                        String symbol = dataObj.getString("originalSymbol");
                        String name = dataObj.getString("name");
                        double LastHour = dataObj.getDouble("change1Hour");
                        double LastWeek = dataObj.getDouble("change1Week");
                        String UpdateDate = dataObj.getString("lastUpdate");
                        //String image = dataObj.getString("image");
                        // adding all data to our array list.
                        newsModalArrayList.add(new News(name, symbol,LastHour,LastWeek,UpdateDate));
                    }
                    // notifying adapter on data change.
                    NewsRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    // handling json exception.
                    e.printStackTrace();
                    Toast.makeText(NewsActivity.this, "Something went amiss. Please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // displaying error response when received any error.
                Toast.makeText(NewsActivity.this, "Something went amiss. Please try again later", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                // in this method passing headers as
                // key along with value as API keys.
                HashMap<String, String> headers = new HashMap<>();
                headers.put("x-api-key", "b3027698e69b9be2445714d229778efc19b2fb3c");
                // at last returning headers
                return headers;
            }
        };
        // calling a method to add our
        // json object request to our queue.
        queue.add(jsonObjectRequest);

    }

}