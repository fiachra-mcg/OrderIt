package com.example.orderit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Availabilities extends AppCompatActivity {

    FirebaseDatabase rootnode;
    DatabaseReference carReference, devReference, dicReference, dunReference, tempReference;
    TextView carAvailable, devAvailable, dicAvailable, dunAvailable, tempAvailable;
    ImageView home;
    FloatingActionButton cart;
    BottomNavigationView bottomNavigationView;
    CalendarView calendarView;
    String date;
    Date newTodaysDate, date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availabilities);

        //getting todays date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy");
        String today = simpleDateFormat.format(new Date());


        //Assign variables
        home = findViewById(R.id.homeLogo);
        cart = findViewById(R.id.btnCart);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.map);
        calendarView = findViewById(R.id.availCalendar);


        carAvailable = findViewById(R.id.carrollsAvailable);
        devAvailable = findViewById(R.id.devittsAvailable);
        dicAvailable = findViewById(R.id.diceysAvailable);
        dunAvailable = findViewById(R.id.dunnesAvailable);
        tempAvailable = findViewById(R.id.templeBarAvailable);

        //linking button to list activity
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Availabilities.this, Homepage.class);
                startActivity(intent);
            }
        });

        //cart button
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Availabilities.this, SummaryActivity.class);
                startActivity(intent);

            }
        });

        //Item listener for nav bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.reservation:
                        startActivity(new Intent(getApplicationContext()
                                , Reservation.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.map:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.barRestaurant:
                        startActivity(new Intent(getApplicationContext()
                                , List.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });


        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");

        //Getting and formatting date from calendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = dayOfMonth+"-"+(month+1)+"-"+year;
                try {
                    date1 = sdf.parse(date);

                    String today = simpleDateFormat.format(new Date());
                    newTodaysDate = simpleDateFormat.parse(today);
                } catch (ParseException e) {
                    Toast.makeText(Availabilities.this, "Can't find todays date", Toast.LENGTH_SHORT).show();
                }


                //Assigning variables for firebase
                rootnode = FirebaseDatabase.getInstance();
                carReference = rootnode.getReference().child("Carrolls").child("Reservation").child(date);
                devReference = rootnode.getReference().child("Devitts Pub").child("Reservation").child(date);
                dicReference = rootnode.getReference().child("Dicey's Garden Club").child("Reservation").child(date);
                dunReference = rootnode.getReference().child("Dunnes Bar").child("Reservation").child(date);
                tempReference = rootnode.getReference().child("The Temple Bar").child("Reservation").child(date);

                //Getting the amount of reservation requests in Carrolls
                carReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        if (date1.before(newTodaysDate)) {
                            carAvailable.setText("Invalid Date");
                            carAvailable.setTextColor(Color.RED);
                        }
                        else if (!dataSnapshot.exists()
                                || dataSnapshot.getChildrenCount()<5){
                            carAvailable.setText("High Availability");
                            carAvailable.setTextColor(Color.parseColor("#007B35"));
                        }
                        else if (dataSnapshot.getChildrenCount()>=5
                                && dataSnapshot.getChildrenCount()<15){
                            carAvailable.setText("Medium Availability");
                            carAvailable.setTextColor(Color.parseColor("#F2C100"));
                        }
                        else {
                            carAvailable.setText("Low Availability");
                            carAvailable.setTextColor(Color.RED);
                        }
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError databaseError) {

                    }
                });

                //Getting the amount of reservation requests in Devitts Pub
                devReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        if (date1.before(newTodaysDate)) {
                            devAvailable.setText("Invalid Date");
                            devAvailable.setTextColor(Color.RED);
                        }
                        else if (!dataSnapshot.exists()
                                || dataSnapshot.getChildrenCount()<5){
                            devAvailable.setText("High Availability");
                            devAvailable.setTextColor(Color.parseColor("#007B35"));
                        }
                        else if (dataSnapshot.getChildrenCount()>=5
                                && dataSnapshot.getChildrenCount()<15){
                            devAvailable.setText("Medium Availability");
                            devAvailable.setTextColor(Color.parseColor("#FFDE59"));
                        }
                        else {
                            devAvailable.setText("Low Availability");
                            devAvailable.setTextColor(Color.RED);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //Getting the amoiunt of reservation requests in Dicey's Garden Club
                dicReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        if (date1.before(newTodaysDate)) {
                            dicAvailable.setText("Invalid Date");
                            dicAvailable.setTextColor(Color.RED);
                        }
                        else if (!dataSnapshot.exists()
                                || dataSnapshot.getChildrenCount()<5){
                            dicAvailable.setText("High Availability");
                            dicAvailable.setTextColor(Color.parseColor("#007B35"));
                        }
                        else if (dataSnapshot.getChildrenCount()>=5
                                && dataSnapshot.getChildrenCount()<15){
                            dicAvailable.setText("Medium Availability");
                            dicAvailable.setTextColor(Color.parseColor("#FFDE59"));
                        }
                        else {
                            dicAvailable.setText("Low Availability");
                            dicAvailable.setTextColor(Color.RED);
                        }
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError databaseError) {

                    }
                });

                //Getting the amoiunt of reservation requests in Dunnes Bar
                dunReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        if (date1.before(newTodaysDate)) {
                            dunAvailable.setText("Invalid Date");
                            dunAvailable.setTextColor(Color.RED);
                        }
                        else if (!dataSnapshot.exists()
                                || dataSnapshot.getChildrenCount()<5){
                            dunAvailable.setText("High Availability");
                            dunAvailable.setTextColor(Color.parseColor("#007B35"));
                        }
                        else if (dataSnapshot.getChildrenCount()>=5
                                && dataSnapshot.getChildrenCount()<15){
                            dunAvailable.setText("Medium Availability");
                            dunAvailable.setTextColor(Color.parseColor("#FFDE59"));
                        }
                        else{
                            dunAvailable.setText("Low Availability");
                            dunAvailable.setTextColor(Color.RED);
                        }
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError databaseError) {

                    }
                });

                //Getting the amoiunt of reservation requests in The Temple Bar
                tempReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                        if (date1.before(newTodaysDate)) {
                            tempAvailable.setText("Invalid Date");
                            tempAvailable.setTextColor(Color.RED);
                        }
                        else if (!dataSnapshot.exists()
                                || dataSnapshot.getChildrenCount()<5){
                            tempAvailable.setText("High Availability");
                            tempAvailable.setTextColor(Color.parseColor("#007B35"));
                        }
                        else if (dataSnapshot.getChildrenCount()>=5
                                && dataSnapshot.getChildrenCount()<15){
                            tempAvailable.setText("Medium Availability");
                            tempAvailable.setTextColor(Color.parseColor("#FFDE59"));
                        }
                        else {
                            tempAvailable.setText("Low Availability");
                            tempAvailable.setTextColor(Color.RED);
                        }
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }
}