package com.example.orderit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    Button available;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    SearchView searchView;
    GoogleMap googleMap;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign variables
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.map);

        cart = findViewById(R.id.btnCart);
        available = findViewById(R.id.availabilities);

        searchView = findViewById(R.id.sv_location);


        //Item listener for nav bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.map:
                        return true;

                    case R.id.barRestaurant:
                        startActivity(new Intent(getApplicationContext()
                                ,List.class));
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

        //SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                java.util.List<Address> addressList = null;

                //Searching for location and if statement for an unknown location
                if (location != null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(MainActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert addressList != null;
                    if(!addressList.isEmpty()){
                        //uses Latitude and longitude to find location and change map view
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        System.out.println(address.getLatitude() + " - " + address.getLongitude());
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Can't find location.", Toast.LENGTH_SHORT).show();
                    }

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        //cart button
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
                startActivity(intent);

            }
        });

        //Availabilities button
        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Availabilities.class);
                startActivity(intent);

            }
        });



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();


    }

    //Requesting location permission
    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        //finding user's last location
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment)
                            getSupportFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(MainActivity.this);

                }
            }
        });
    }

    //Placing map markers and positioning camera
    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;
        //Placing I am Here marker on map
        LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("I Am Here.");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        googleMap.addMarker(markerOptions).showInfoWindow();

        //Restaurant markers
        LatLng Diceys = new LatLng(53.33589272770277, -6.263548853482935);
        googleMap.addMarker(new MarkerOptions().position(Diceys).title("Dicey's Garden Club"));

        LatLng TheTempleBar = new LatLng(53.345496625061635, -6.26418877415585);
        googleMap.addMarker(new MarkerOptions().position(TheTempleBar).title("The Temple Bar"));

        LatLng Carrolls = new LatLng(53.92169634814435, -7.866166223569849);
        googleMap.addMarker(new MarkerOptions().position(Carrolls).title("Carrolls"));

        LatLng DunnessBar = new LatLng(53.94571889729503, -8.095147821076562);
        googleMap.addMarker(new MarkerOptions().position(DunnessBar).title("Dunnes Bar"));

        LatLng DevittsPub = new LatLng(53.33559082997639, -6.265441296544499);
        googleMap.addMarker(new MarkerOptions().position(DevittsPub).title("Devitts Pub"));

        //On Click listener for map markers linking to menu activity
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markerTitle = marker.getTitle();

                //if statement to make current location not clickable
                if (markerTitle.equals(markerOptions.getTitle())){
                    return true;
                }

                //all other markers are clickable
                else {
                    Intent i = new Intent(MainActivity.this, Menu.class);
                    //we are passing title to new activity
                    i.putExtra("title", markerTitle);
                    startActivity(i);


                    return false;
                }
            }
        });


    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectoreDrawable= ContextCompat.getDrawable(context,vectorResId);
        vectoreDrawable.setBounds(0, 0,vectoreDrawable.getIntrinsicWidth(),
                vectoreDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectoreDrawable.getIntrinsicWidth(),
                vectoreDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectoreDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //Requesting location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }

}