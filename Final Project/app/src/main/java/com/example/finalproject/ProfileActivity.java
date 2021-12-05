package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_main);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_signin);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener()  {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_signin:
                        Toast.makeText(ProfileActivity.this, "Account", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_stocks:
                        Intent stocksIntent =  new Intent( ProfileActivity.this, StocksActivity.class);
                        startActivity(stocksIntent);
                        break;

                }
                return true;
            }
        });

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        Button logoutBtn = (Button) findViewById(R.id.LogoutBtn);

        //handle click, logout
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                checkUser();
            }
        });

    }

    private void checkUser() {
        TextView emailTv = (TextView) findViewById(R.id.EmailTv);
        TextView nameTv = (TextView) findViewById(R.id.NameTv);
        ImageView imageTv = (ImageView) findViewById(R.id.ImageTv);
        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            //user not logged in
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        else{
            //user logged in
            //get user info
            String email = firebaseUser.getEmail();
            String name = firebaseUser.getDisplayName();
            Uri image = firebaseUser.getPhotoUrl();
            //set email
            Picasso.get().load(image).resize(200, 200).into(imageTv);
            nameTv.setText(name);
            emailTv.setText(email);
        }
    }

    private void signOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //user is now signed out
                        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                        finish();
                    }
                });
    }
}