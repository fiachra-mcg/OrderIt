package com.example.orderit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaCas;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.bouncycastle.jcajce.provider.asymmetric.rsa.ISOSignatureSpi;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Reservation extends AppCompatActivity {

    Spinner sizeSpinner, timeSpinner, restaurantSpinner;
    CalendarView calendarView;
    Button makeBooking;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    EditText resName, phoneNumber, email;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton cart;

    ImageView home;

    String stringDate;
    Date date, newTodaysDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        //assign variables
        sizeSpinner = findViewById(R.id.partySizeSpinner);
        timeSpinner = findViewById(R.id.partyTimeSpinner);
        restaurantSpinner = findViewById(R.id.restaurantSpinner);

        resName = findViewById(R.id.name);
        phoneNumber = findViewById(R.id.number);
        calendarView = findViewById(R.id.calendar);
        email = findViewById(R.id.email);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.reservation);

        makeBooking = findViewById(R.id.bookDate);

        cart = findViewById(R.id.btnCart);

        home = findViewById(R.id.homeLogo);

        //Applying spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.party_size, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this, R.array.book_time, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);

        ArrayAdapter<CharSequence> restaurantAdapter = ArrayAdapter.createFromResource(this, R.array.restaurant, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restaurantSpinner.setAdapter(restaurantAdapter);

        //get String title from RestaurantOptions activity
        String title = getIntent().getStringExtra("title");

        //change spinner value if string title is not null
        if (title != null) {
            restaurantSpinner.setSelection(((ArrayAdapter<String>) restaurantSpinner.getAdapter()).getPosition(title));
        }

        //ImageView linking to Homepage activity
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reservation.this, Homepage.class);
                startActivity(intent);
            }
        });

        //cart button
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reservation.this, SummaryActivity.class);
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

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy");

        //Getting and formatting date from calendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                try {
                    stringDate = dayOfMonth+"-"+(month+1)+"-"+year;
                    date = simpleDateFormat.parse(stringDate);

                    String today = simpleDateFormat.format(new Date());
                    newTodaysDate = simpleDateFormat.parse(today);

                } catch (ParseException e) {
                    Toast.makeText(Reservation.this, "Can't find todays date", Toast.LENGTH_SHORT).show();

                }
            }
        });

        // click book date button and return to menu
        makeBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date == null
                        || date.before(newTodaysDate)
                        || date.equals(newTodaysDate)
                        || TextUtils.isEmpty(stringDate)
                        || TextUtils.isEmpty(resName.getText().toString())
                        || TextUtils.isEmpty(phoneNumber.getText().toString())
                        || TextUtils.isEmpty(email.getText().toString())
                    ){

                    Toast.makeText(Reservation.this, "Please Fill in Required Information Correctly", Toast.LENGTH_SHORT).show();

                } else {

                    try {
                        //setting variables
                        String place = restaurantSpinner.getSelectedItem().toString();

                        rootNode = FirebaseDatabase.getInstance();

                        String newDate = stringDate;

                        String size = sizeSpinner.getSelectedItem().toString();
                        String time = timeSpinner.getSelectedItem().toString();
                        String name = resName.getText().toString();
                        String number = phoneNumber.getText().toString();
                        String emailAd = email.getText().toString();

                        //replace characters that firebase doesn't accept in email addresses
                        String newEmailAd = emailAd.replace(".", ",")
                                .replace("#","*").replace("$","&")
                                .replace("[","(").replace("]",")");

                        reference = rootNode.getReference().child(place).child("Reservation").child(newDate)
                                .child(newEmailAd).child(time);

                        String messageToSend = ("Hi " + name + ", " + place + " has received your reservation request for "
                                + size + " people " + "on the " + stringDate + " at " + time +
                                ". You will be contacted shortly to confirm your booking and table number.");

                        ReservationHelper helperClass = new ReservationHelper(size, time, name, number, newDate, emailAd);

                        //Making properties for email
                        Properties props = new Properties();
                        props.put("mail.smtp.auth", "true");
                        props.put("mail.smtp.starttls.enable", "true");
                        props.put("mail.smtp.host", "smtp.gmail.com");
                        props.put("mail.smtp.port", "587");

                        //Linking email and password
                        javax.mail.Session session = Session.getInstance(props,
                                new javax.mail.Authenticator() {
                                    @Override
                                    protected PasswordAuthentication getPasswordAuthentication() {
                                        return new PasswordAuthentication("fiachra.mcg1@gmail.com", "cjqkyhufcczyxegu");

                                    }
                                });

                        //Sending email
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(emailAd));
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAd));
                        message.setSubject("Ordit Reservation Request");
                        message.setText(messageToSend);
                        Transport.send(message);
                        Toast.makeText(getApplicationContext(), "Reservation Requested", Toast.LENGTH_LONG).show();

                        //Start List activity
                        Intent intent = new Intent(Reservation.this, List.class);
                        startActivity(intent);

                        reference.setValue(helperClass);

                    } catch (MessagingException e) {
                        Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }
}