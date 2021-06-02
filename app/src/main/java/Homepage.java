package com.example.orderit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Homepage extends AppCompatActivity {

    Button see_nearby, to_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //Assign variables
        see_nearby = findViewById(R.id.see_map);
        to_order = findViewById(R.id.pick_restaurant);

        //linking button to Main Activity
        see_nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, MainActivity.class);
                startActivity(intent);

            }
        });


        //linking button to list activity
        to_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, List.class);
                startActivity(intent);

            }
        });


    }

}