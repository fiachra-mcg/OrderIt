package com.example.orderit;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class RestaurantOptions extends AppCompatActivity {

    Button menu_button, availabilities, reservation_button, service_now_button;
    TextView markertxt;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    BottomNavigationView bottomNavigationView;
    ImageView home;
    FloatingActionButton cart;

    String table = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_options);

        //Defining variables
        menu_button = findViewById(R.id.menu);
        availabilities = findViewById(R.id.availabilities);
        reservation_button = findViewById(R.id.reservation);
        service_now_button = findViewById(R.id.service_now);
        markertxt = findViewById(R.id.Restaurants);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.barRestaurant);

        home = findViewById(R.id.homeLogo);

        //Cart button
        cart = findViewById(R.id.btnCart);

        //we will get title from List activity
        String title = getIntent().getStringExtra("title");
        markertxt.setText(title);

        //Item listener for nav bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.reservation:
                        startActivity(new Intent(getApplicationContext()
                                ,Reservation.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.map:
                        startActivity(new Intent(getApplicationContext()
                                ,MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.barRestaurant:
                        startActivity(new Intent(getApplicationContext()
                                ,List.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });

        //cart button
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantOptions.this, SummaryActivity.class);
                startActivity(intent);

            }
        });

        //Home Image button to bring you to home page
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantOptions.this,Homepage.class);
                startActivity(intent);
            }
        });

        //Link button to Menu activity
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantOptions.this, Menu.class);
                //we are passing title to new activity
                intent.putExtra("title", markertxt.getText());

                startActivity(intent);
            }
        });

        //Link button to Availabilities activity
        availabilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantOptions.this, Availabilities.class);
                startActivity(intent);
            }
        });

        //Link button Reservation activity
        reservation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantOptions.this, Reservation.class);
                intent.putExtra("title", markertxt.getText());
                startActivity(intent);
            }
        });

        //Linking button to AlertDialog
        service_now_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pop up Alert Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantOptions.this);
                builder.setCancelable(true);
                builder.setTitle("Service Now");
                builder.setMessage("To request assistance from staff, please enter your table number");
                // Set up the input
                final EditText input = new EditText(RestaurantOptions.this);
                // Specify the type of input expected and max character input
                input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(2) });
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                //Applying the "Cancel" button
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                //Applying the "OK" button
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        table = input.getText().toString();

                        //Getting timestamp
                        Date date = new Date();
                        String currentTime = DateFormat.getDateTimeInstance().format(date);
                        //Saving to database
                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference(title).child("Service Now").child("Table "+ table);
                        Toast.makeText(getApplicationContext(), "A staff member will be with you shortly", Toast.LENGTH_LONG).show();
                        reference.push().setValue(currentTime);
                    }
                });

                builder.show();

            }
        });
    }
}