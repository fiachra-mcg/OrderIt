package com.example.orderit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class List extends AppCompatActivity {

    Button diceys_button, TheTempleBar_button, Dunnes_button, DevittsPub_button, Carrolls_button;
    FloatingActionButton cart;
    ImageView home;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //Assigning Variables
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.barRestaurant);

        cart = findViewById(R.id.btnCart);

        diceys_button = findViewById(R.id.Diceys);
        TheTempleBar_button = findViewById(R.id.TheTempleBar);
        Dunnes_button = findViewById(R.id.Dunnes);
        DevittsPub_button = findViewById(R.id.DevittsPub);
        Carrolls_button = findViewById(R.id.Carrolls);

        home = findViewById(R.id.homeLogo);

        //Item listener for nav bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.barRestaurant:
                        return true;

                    case R.id.map:
                        startActivity(new Intent(getApplicationContext()
                                ,MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.reservation:
                        startActivity(new Intent(getApplicationContext()
                                ,Reservation.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });

        //ImageView linking to Homepage activity
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List.this,Homepage.class);
                startActivity(intent);
            }
        });

        //OnClickListener for summary activity
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List.this, SummaryActivity.class);
                startActivity(intent);
            }
        });

        //Button linking to Menu activity
        diceys_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List.this,RestaurantOptions.class);
                //we are passing title to new activity
                intent.putExtra("title", diceys_button.getText());

                startActivity(intent);

            }
        });

        //Button linking to Menu activity
        TheTempleBar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List.this,RestaurantOptions.class);
                //we are passing title to new activity
                intent.putExtra("title", TheTempleBar_button.getText());

                startActivity(intent);

            }
        });

        //Button linking to Menu activity
        Dunnes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List.this,RestaurantOptions.class);
                //we are passing title to new activity
                intent.putExtra("title", Dunnes_button.getText());

                startActivity(intent);

            }
        });

        //Button linking to Menu activity
        DevittsPub_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List.this,RestaurantOptions.class);
                //we are passing title to new activity
                intent.putExtra("title", DevittsPub_button.getText());

                startActivity(intent);

            }
        });

        //Button linking to Menu activity
        Carrolls_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List.this,RestaurantOptions.class);
                //we are passing title to new activity
                intent.putExtra("title", Carrolls_button.getText());

                startActivity(intent);

            }
        });

    }


}