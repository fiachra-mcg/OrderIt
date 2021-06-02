package com.example.orderit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Menu extends AppCompatActivity  {

    TextView markertxt, address, openingHours;
    FloatingActionButton cart;
    ImageButton beerPlusQuantity, beerMinusQuantity, cokePlusQuantity, cokeMinusQuantity, winePlusQuantity, wineMinusQuantity,
            soupPlusQuantity, soupMinusQuantity, chickenWingsPlusQuantity, chickenWingsMinusQuantity, saladPlusQuantity, saladMinusQuantity,
            steakPlusQuantity, steakMinusQuantity, chickenCurryPlusQuantity, chickenCurryMinusQuantity, lasagnaPlusQuantity, lasagnaMinusQuantity,
            cheeseCakePlusQuantity, cheeseCakeMinusQuantity, chocolateBrowniePlusQuantity, chocolateBrownieMinusQuantity, iceCreamPlusQuantity, iceCreamMinusQuantity;
    int beerQuantity, cokeQuantity, wineQuantity,
            soupQuantity, chickenWingsQuantity, saladQuantity,
            steakQuantity, chickenCurryQuantity, lasagnaQuantity,
            cheeseCakeQuantity, chocolateBrownieQuantity, iceCreamQuantity;
    TextView beerQuantityNumber, beerPrice, cokeQuantityNumber, cokePrice, wineQuantityNumber, winePrice,
            soupQuantityNumber, soupPrice, chickenWingsQuantityNumber, chickenWingsPrice, saladQuantityNumber, saladPrice,
            steakQuantityNumber, steakPrice, chickenCurryQuantityNumber, chickenCurryPrice, lasagnaQuantityNumber, lasagnaPrice,
            cheeseCakeQuantityNumber, cheeseCakePrice, chocolateBrownieQuantityNumber, chocolateBrowniePrice, iceCreamQuantityNumber, iceCreamPrice;
    TextView drinka,drinkb,drinkc, startera, starterb, starterc, maina, mainb, mainc, desserta,
            dessertb, dessertc;
    DatabaseReference reff;
    SharedPreferences sp, sharedPref;
    SharedPreferences.Editor editor;
    Button beerCart, cokeCart, wineCart, soupCart, wingsCart, saladCart, steakCart,curryCart,
            lasagnaCart, cheeseCart, brownieCart, iceCreamCart;

    BottomNavigationView bottomNavigationView;

    ImageView home;

    String placeStr;

    //hashmap values
    final int beerBasePrice = 5;
    final int cokeBasePrice = 3;
    final int wineBasePrice = 5;

    final int soupBasePrice = 6;
    final int chickenWingsBasePrice = 8;
    final int saladBasePrice = 7;

    final int steakBasePrice = 10;
    final int chickenCurryBasePrice = 10;
    final int lasagnaBasePrice = 10;

    final int cheeseCakeBasePrice = 10;
    final int chocolateBrownieBasePrice = 10;
    final int iceCreamBasePrice = 10;


    //Pulls data from database to Menu
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        sharedPref = getSharedPreferences("TITLE", Context.MODE_PRIVATE);

        //Heading is pulled from previous page
        markertxt = findViewById(R.id.marker);
        //we will get data from our Maps activity
        String title = getIntent().getStringExtra("title");
        markertxt.setText(title);

        //Saving menu title to shared preferences
        placeStr = markertxt.getText().toString();

        SharedPreferences.Editor edit = sharedPref.edit();

        edit.putString("markertxt", placeStr);
        edit.apply();

        reff = FirebaseDatabase.getInstance().getReference().child(placeStr);

        address = findViewById(R.id.address);
        openingHours = findViewById(R.id.openingHours);

        drinka = (TextView)findViewById(R.id.beer);
        drinkb = (TextView)findViewById(R.id.coke);
        drinkc = (TextView)findViewById(R.id.wine);

        startera = (TextView) findViewById(R.id.soup);
        starterb = (TextView) findViewById(R.id.chickenWings);
        starterc = (TextView) findViewById(R.id.salad);

        maina = (TextView) findViewById(R.id.steak);
        mainb = (TextView) findViewById(R.id.chickenCurry);
        mainc = (TextView) findViewById(R.id.lasagna);

        desserta = (TextView) findViewById(R.id.cheeseCake);
        dessertb = (TextView) findViewById(R.id.chocolateBrownie);
        dessertc = (TextView) findViewById(R.id.iceCream);

        cokeCart = findViewById(R.id.cokeButton);
        wineCart = findViewById(R.id.wineButton);
        soupCart = findViewById(R.id.soupButton);
        wingsCart = findViewById(R.id.chickenWingsButton);
        saladCart = findViewById(R.id.saladButton);
        steakCart = findViewById(R.id.steakButton);
        curryCart = findViewById(R.id.chickenCurryButton);
        lasagnaCart = findViewById(R.id.lasagnaButton);
        cheeseCart = findViewById(R.id.cheeseCakeButton);
        brownieCart = findViewById(R.id.chocolateBrownieButton);
        iceCreamCart = findViewById(R.id.iceCreamButton);

        beerCart = findViewById(R.id.beerButton);
        drinka = (TextView)findViewById(R.id.beer);

        //Counter implemented
        beerQuantityNumber = findViewById(R.id.quantity);
        beerPlusQuantity = findViewById(R.id.addquantity);
        beerMinusQuantity = findViewById(R.id.subquantity);
        beerPrice = findViewById(R.id.beerPrice);

        cokePrice = findViewById(R.id.cokePrice);
        cokePlusQuantity = findViewById(R.id.cokeaddquantity);
        cokeMinusQuantity = findViewById(R.id.cokesubquantity);
        cokeQuantityNumber = findViewById(R.id.cokequantity);

        winePrice = findViewById(R.id.winePrice);
        winePlusQuantity = findViewById(R.id.wineaddquantity);
        wineMinusQuantity = findViewById(R.id.winesubquantity);
        wineQuantityNumber = findViewById(R.id.winequantity);

        soupQuantityNumber = findViewById(R.id.soupQuantity);
        soupPlusQuantity = findViewById(R.id.soupAddQuantity);
        soupMinusQuantity = findViewById(R.id.soupSubquantity);
        soupPrice = findViewById(R.id.soupPrice);

        chickenWingsPrice = findViewById(R.id.chickenWingsPrice);
        chickenWingsPlusQuantity = findViewById(R.id.chickenWingsAddQuantity);
        chickenWingsMinusQuantity = findViewById(R.id.chickenWingsSubQuantity);
        chickenWingsQuantityNumber = findViewById(R.id.chickenWingsQuantity);

        saladPrice = findViewById(R.id.saladPrice);
        saladPlusQuantity = findViewById(R.id.saladAddQuantity);
        saladMinusQuantity = findViewById(R.id.saladSubQuantity);
        saladQuantityNumber = findViewById(R.id.saladQuantity);

        steakPrice = findViewById(R.id.steakPrice);
        steakPlusQuantity = findViewById(R.id.steakAddQuantity);
        steakMinusQuantity = findViewById(R.id.steakSubQuantity);
        steakQuantityNumber = findViewById(R.id.steakQuantity);

        chickenCurryPrice = findViewById(R.id.chickenCurryPrice);
        chickenCurryPlusQuantity = findViewById(R.id.chickenCurryAddQuantity);
        chickenCurryMinusQuantity = findViewById(R.id.chickenCurrySubQuantity);
        chickenCurryQuantityNumber = findViewById(R.id.chickenCurryQuantity);

        lasagnaPrice = findViewById(R.id.lasagnaPrice);
        lasagnaPlusQuantity = findViewById(R.id.lasagnaAddQuantity);
        lasagnaMinusQuantity = findViewById(R.id.lasagnaSubQuantity);
        lasagnaQuantityNumber = findViewById(R.id.lasagnaQuantity);

        cheeseCakePrice = findViewById(R.id.cheeseCakePrice);
        cheeseCakePlusQuantity = findViewById(R.id.cheeseCakeAddQuantity);
        cheeseCakeMinusQuantity = findViewById(R.id.cheeseCakeSubQuantity);
        cheeseCakeQuantityNumber = findViewById(R.id.cheeseCakeQuantity);

        chocolateBrowniePrice = findViewById(R.id.chocolateBrowniePrice);
        chocolateBrowniePlusQuantity = findViewById(R.id.chocolateBrownieAddQuantity);
        chocolateBrownieMinusQuantity = findViewById(R.id.chocolateBrownieSubQuantity);
        chocolateBrownieQuantityNumber = findViewById(R.id.chocolateBrownieQuantity);

        iceCreamPrice = findViewById(R.id.iceCreamPrice);
        iceCreamPlusQuantity = findViewById(R.id.iceCreamAddQuantity);
        iceCreamMinusQuantity = findViewById(R.id.iceCreamSubQuantity);
        iceCreamQuantityNumber = findViewById(R.id.iceCreamQuantity);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.barRestaurant);

        home = findViewById(R.id.homeLogo);

        //Cart button
        cart = findViewById(R.id.btnCart);

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //Pulling menu, address and opening hours from database
                String sAddress = dataSnapshot.child("Address").getValue().toString();
                String sOpeningHours = dataSnapshot.child("Opening Hours").getValue().toString();

                String beer = dataSnapshot.child("Menu").child("Drink1").getValue().toString();
                String coke = dataSnapshot.child("Menu").child("Drink2").getValue().toString();
                String wine = dataSnapshot.child("Menu").child("Drink3").getValue().toString();

                String soup = dataSnapshot.child("Menu").child("Starter1").getValue().toString();
                String chickenWings = dataSnapshot.child("Menu").child("Starter2").getValue().toString();
                String salad = dataSnapshot.child("Menu").child("Starter3").getValue().toString();

                String steak = dataSnapshot.child("Menu").child("Main1").getValue().toString();
                String chickenCurry = dataSnapshot.child("Menu").child("Main2").getValue().toString();
                String lasagna = dataSnapshot.child("Menu").child("Main3").getValue().toString();

                String cheeseCake = dataSnapshot.child("Menu").child("Dessert1").getValue().toString();
                String chocolateBrownie = dataSnapshot.child("Menu").child("Dessert2").getValue().toString();
                String iceCream = dataSnapshot.child("Menu").child("Dessert3").getValue().toString();

                address.setText(sAddress);
                openingHours.setText(sOpeningHours);

                drinka.setText(beer);
                drinkb.setText(coke);
                drinkc.setText(wine);

                startera.setText(soup);
                starterb.setText(chickenWings);
                starterc.setText(salad);

                maina.setText(steak);
                mainb.setText(chickenCurry);
                mainc.setText(lasagna);

                desserta.setText(cheeseCake);
                dessertb.setText(chocolateBrownie);
                dessertc.setText(iceCream);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Clear cart when opening new menu
        sp.edit().clear().apply();

        //Home Image button to bring you to home page
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Homepage.class);
                startActivity(intent);
            }
        });

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

                    case R.id.barRestaurant:
                        startActivity(new Intent(getApplicationContext()
                                ,List.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.map:
                        startActivity(new Intent(getApplicationContext()
                                ,MainActivity.class));
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
                Intent intent = new Intent(Menu.this, SummaryActivity.class);
                startActivity(intent);

            }
        });

        //"Added to Cart" message displayed on button click
        //"Add n beers to beer counter
        beerCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), drinka.getText() + " X " +
                        beerQuantityNumber.getText() + " added to cart.", Toast.LENGTH_SHORT).show();

                //get beer value currently in cart and add additional beers to it
                int beerNewQuantity = sp.getInt("BEER", 0) + Integer.parseInt(beerQuantityNumber.getText().toString());

                //add new beer value to shared preferences
                editor = sp.edit();
                editor.putInt("BEER", beerNewQuantity);
                editor.apply();

                // change quantity selector to 1
                // change price to base price €
                beerQuantity = 1;
                beerQuantityNumber.setText("1");
                beerPrice.setText("€" + beerBasePrice);
            }
        });

        cokeCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), drinkb.getText() + " X " +
                        cokeQuantityNumber.getText() + " added to cart.", Toast.LENGTH_SHORT).show();

                //get coke value currently in cart and add additional beers to it
                int cokeNewQuantity = sp.getInt("COKE", 0) + Integer.parseInt(cokeQuantityNumber.getText().toString());

                //add new coke value to shared preferences
                editor = sp.edit();
                editor.putInt("COKE", cokeNewQuantity);
                editor.apply();

                // change quantity selector to 1
                // change price to base price €
                cokeQuantity = 1;
                cokeQuantityNumber.setText("1");
                cokePrice.setText("€" + cokeBasePrice);
            }
        });

        wineCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), drinkc.getText() + " X " +
                        wineQuantityNumber.getText() + " added to cart.", Toast.LENGTH_SHORT).show();

                //get wine value currently in cart and add additional beers to it
                int cokeNewQuantity = sp.getInt("WINE", 0) + Integer.parseInt(wineQuantityNumber.getText().toString());

                //add new wine value to shared preferences
                editor = sp.edit();
                editor.putInt("WINE", cokeNewQuantity);
                editor.apply();

                // change quantity selector to 1
                // change price to base price €
                wineQuantity = 1;
                wineQuantityNumber.setText("1");
                winePrice.setText("€" + wineBasePrice);
            }
        });

        soupCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), startera.getText() + " X " +
                        soupQuantityNumber.getText() + " added to cart.", Toast.LENGTH_SHORT).show();

                //get soup value currently in cart and add additional beers to it
                int soupNewQuantity = sp.getInt("SOUP", 0) + Integer.parseInt(soupQuantityNumber.getText().toString());

                //add new soup value to shared preferences
                editor = sp.edit();
                editor.putInt("SOUP", soupNewQuantity);
                editor.apply();

                // change quantity selector to 1
                // change price to base price €
                soupQuantity = 1;
                soupQuantityNumber.setText("1");
                soupPrice.setText("€" + soupBasePrice);
            }
        });

        wingsCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), starterb.getText() + " X " +
                        chickenWingsQuantityNumber.getText() + " added to cart.", Toast.LENGTH_SHORT).show();

                //get wings value currently in cart and add additional beers to it
                int chickenWingsNewQuantity = sp.getInt("CHICKENWINGS", 0) + Integer.parseInt(chickenWingsQuantityNumber.getText().toString());

                //add new wings value to shared preferences
                editor = sp.edit();
                editor.putInt("CHICKENWINGS", chickenWingsNewQuantity);
                editor.apply();

                // change quantity selector to 1
                // change price to base price €
                chickenWingsQuantity = 1;
                chickenWingsQuantityNumber.setText("1");
                chickenWingsPrice.setText("€" + chickenWingsBasePrice);
            }
        });

        saladCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), starterc.getText() + " X " +
                        saladQuantityNumber.getText() + " added to cart.", Toast.LENGTH_SHORT).show();

                //get salad value currently in cart and add additional beers to it
                int saladNewQuantity = sp.getInt("SALAD", 0) + Integer.parseInt(saladQuantityNumber.getText().toString());

                //add new salad value to shared preferences
                editor = sp.edit();
                editor.putInt("SALAD", saladNewQuantity);
                editor.apply();

                // change quantity selector to 1
                // change price to base price €
                saladQuantity = 1;
                saladQuantityNumber.setText("1");
                saladPrice.setText("€" + saladBasePrice);
            }
        });

        steakCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), maina.getText() + " X " +
                        steakQuantityNumber.getText() + " added to cart.", Toast.LENGTH_SHORT).show();

                //get steak value currently in cart and add additional beers to it
                int steakNewQuantity = sp.getInt("STEAK", 0) + Integer.parseInt(steakQuantityNumber.getText().toString());

                //add new steak value to shared preferences
                editor = sp.edit();
                editor.putInt("STEAK", steakNewQuantity);
                editor.apply();

                // change quantity selector to 1
                // change price to base price €
                steakQuantity = 1;
                steakQuantityNumber.setText("1");
                steakPrice.setText("€" + steakBasePrice);
            }
        });

        curryCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), mainb.getText() + " X " +
                        chickenCurryQuantityNumber.getText() + " added to cart.", Toast.LENGTH_SHORT).show();

                //get curry value currently in cart and add additional beers to it
                int chickenCurryNewQuantity = sp.getInt("CHICKENCURRY", 0) + Integer.parseInt(chickenCurryQuantityNumber.getText().toString());

                //add new curry value to shared preferences
                editor = sp.edit();
                editor.putInt("CHICKENCURRY", chickenCurryNewQuantity);
                editor.apply();

                // change quantity selector to 1
                // change price to base price €
                chickenCurryQuantity = 1;
                chickenCurryQuantityNumber.setText("1");
                chickenCurryPrice.setText("€" + chickenCurryBasePrice);
            }
        });

        lasagnaCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), mainc.getText() + " X " +
                        lasagnaQuantityNumber.getText() + " added to cart.", Toast.LENGTH_SHORT).show();

                //get lasagna value currently in cart and add additional beers to it
                int lasagnaNewQuantity = sp.getInt("LASAGNA", 0) + Integer.parseInt(lasagnaQuantityNumber.getText().toString());

                //add new lasagna value to shared preferences
                editor = sp.edit();
                editor.putInt("LASAGNA", lasagnaNewQuantity);
                editor.apply();

                // change quantity selector to 1
                // change price to base price €
                lasagnaQuantity = 1;
                lasagnaQuantityNumber.setText("1");
                lasagnaPrice.setText("€" + lasagnaBasePrice);
            }
        });

        cheeseCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), desserta.getText() + " X " +
                        cheeseCakeQuantityNumber.getText() + " added to cart.", Toast.LENGTH_SHORT).show();

                //get cheeseCake value currently in cart and add additional beers to it
                int cheeseCakeNewQuantity = sp.getInt("CHEESECAKE", 0) + Integer.parseInt(cheeseCakeQuantityNumber.getText().toString());

                //add new cheeseCake value to shared preferences
                editor = sp.edit();
                editor.putInt("CHEESECAKE", cheeseCakeNewQuantity);
                editor.apply();

                // change quantity selector to 1
                // change price to base price €
                cheeseCakeQuantity = 1;
                cheeseCakeQuantityNumber.setText("1");
                cheeseCakePrice.setText("€" + cheeseCakeBasePrice);
            }
        });

        brownieCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), dessertb.getText() + " X " +
                        chocolateBrownieQuantityNumber.getText() + " added to cart.", Toast.LENGTH_SHORT).show();

                //get brownie value currently in cart and add additional beers to it
                int chocolateBrownieNewQuantity = sp.getInt("CHOCOLATEBROWNIE", 0) + Integer.parseInt(chocolateBrownieQuantityNumber.getText().toString());

                //add new brownie value to shared preferences
                editor = sp.edit();
                editor.putInt("CHOCOLATEBROWNIE", chocolateBrownieNewQuantity);
                editor.apply();

                // change quantity selector to 1
                // change price to base price €
                chocolateBrownieQuantity = 1;
                chocolateBrownieQuantityNumber.setText("1");
                chocolateBrowniePrice.setText("€" + chocolateBrownieBasePrice);
            }
        });

        iceCreamCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), dessertc.getText() + " X " +
                        iceCreamQuantityNumber.getText() + " added to cart.", Toast.LENGTH_SHORT).show();

                //get iceCream value currently in cart and add additional beers to it
                int iceCreamNewQuantity = sp.getInt("ICECREAM", 0) + Integer.parseInt(iceCreamQuantityNumber.getText().toString());

                //add new iceCream value to shared preferences
                editor = sp.edit();
                editor.putInt("ICECREAM", iceCreamNewQuantity);
                editor.apply();

                // change quantity selector to 1
                // change price to base price €
                iceCreamQuantity = 1;
                iceCreamQuantityNumber.setText("1");
                iceCreamPrice.setText("€" + iceCreamBasePrice);
            }
        });

        //add and subtract buttons for all items on menu
        beerPlusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int basePrice = 5;
                beerQuantity++;
                beerQuantityNumber.setText(String.valueOf(beerQuantity));
                int bPrice = basePrice * beerQuantity;
                String setnewPrice = String.valueOf(bPrice);
                beerPrice.setText("€" + setnewPrice);
            }
        });

        beerMinusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                int basePrice = 5;
                // because we dont want the quantity go less than 0
                if (beerQuantity == 0) {
                    Toast.makeText(Menu.this, "Can't decrease quantity < 0", Toast.LENGTH_SHORT).show();
                } else {
                    beerQuantity--;
                    beerQuantityNumber.setText(String.valueOf(beerQuantity));
                    int bPrice = basePrice * beerQuantity;
                    String setnewPrice = String.valueOf(bPrice);
                    beerPrice.setText("€" + setnewPrice);

                }
            }
        });

        cokePlusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int basePrice = 3;
                cokeQuantity++;
                cokeQuantityNumber.setText(String.valueOf(cokeQuantity));
                int cPrice = basePrice * cokeQuantity;
                String setnewPrice = String.valueOf(cPrice);
                cokePrice.setText("€" + setnewPrice);
            }
        });

        cokeMinusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                int baseCokePrice = 3;
                // because we dont want the quantity go less than 0
                if (cokeQuantity == 0) {
                    Toast.makeText(Menu.this, "Cant decrease quantity < 0", Toast.LENGTH_SHORT).show();
                } else {
                    cokeQuantity--;
                    cokeQuantityNumber.setText(String.valueOf(cokeQuantity));
                    int cPrice = baseCokePrice * cokeQuantity;
                    String setnewPrice = String.valueOf(cPrice);
                    cokePrice.setText("€" + setnewPrice);

                }
            }
        });

        winePlusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int basePrice = 5;
                wineQuantity++;
                wineQuantityNumber.setText(String.valueOf(wineQuantity));
                int wPrice = basePrice * wineQuantity;
                String setnewPrice = String.valueOf(wPrice);
                winePrice.setText("€" + setnewPrice);
            }
        });

        wineMinusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                int baseWinePrice = 5;
                // because we dont want the quantity go less than 0
                if (wineQuantity == 0) {
                    Toast.makeText(Menu.this, "Cant decrease quantity < 0", Toast.LENGTH_SHORT).show();
                } else {
                    wineQuantity--;
                    wineQuantityNumber.setText(String.valueOf(wineQuantity));
                    int wPrice = baseWinePrice * wineQuantity;
                    String setnewPrice = String.valueOf(wPrice);
                    winePrice.setText("€" + setnewPrice);

                }
            }
        });

        soupPlusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int basePrice = 6;
                soupQuantity++;
                soupQuantityNumber.setText(String.valueOf(soupQuantity));
                int sPrice = basePrice * soupQuantity;
                String setnewPrice = String.valueOf(sPrice);
                soupPrice.setText("€" + setnewPrice);
            }
        });

        soupMinusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                int baseSoupPrice = 6;
                // because we dont want the quantity go less than 0
                if (soupQuantity == 0) {
                    Toast.makeText(Menu.this, "Cant decrease quantity < 0", Toast.LENGTH_SHORT).show();
                } else {
                    soupQuantity--;
                    soupQuantityNumber.setText(String.valueOf(soupQuantity));
                    int sPrice = baseSoupPrice * soupQuantity;
                    String setnewPrice = String.valueOf(sPrice);
                    soupPrice.setText("€" + setnewPrice);

                }
            }
        });

        chickenWingsPlusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int basePrice = 8;
                chickenWingsQuantity++;
                chickenWingsQuantityNumber.setText(String.valueOf(chickenWingsQuantity));
                int wingsPrice = basePrice * chickenWingsQuantity;
                String setnewPrice = String.valueOf(wingsPrice);
                chickenWingsPrice.setText("€" + setnewPrice);
            }
        });

        chickenWingsMinusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                int baseWingsPrice = 8;
                // because we dont want the quantity go less than 0
                if (chickenWingsQuantity == 0) {
                    Toast.makeText(Menu.this, "Cant decrease quantity < 0", Toast.LENGTH_SHORT).show();
                } else {
                    chickenWingsQuantity--;
                    chickenWingsQuantityNumber.setText(String.valueOf(chickenWingsQuantity));
                    int wingsPrice = baseWingsPrice * chickenWingsQuantity;
                    String setnewPrice = String.valueOf(wingsPrice);
                    chickenWingsPrice.setText("€" + setnewPrice);

                }
            }
        });

        saladPlusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int basePrice = 7;
                saladQuantity++;
                saladQuantityNumber.setText(String.valueOf(saladQuantity));
                int salPrice = basePrice * saladQuantity;
                String setnewPrice = String.valueOf(salPrice);
                saladPrice.setText("€" + setnewPrice);
            }
        });

        saladMinusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                int basesaladPrice = 7;
                // because we dont want the quantity go less than 0
                if (saladQuantity == 0) {
                    Toast.makeText(Menu.this, "Cant decrease quantity < 0", Toast.LENGTH_SHORT).show();
                } else {
                    saladQuantity--;
                    saladQuantityNumber.setText(String.valueOf(saladQuantity));
                    int salPrice = basesaladPrice * saladQuantity;
                    String setnewPrice = String.valueOf(salPrice);
                    saladPrice.setText("€" + setnewPrice);

                }
            }
        });

        steakPlusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int basePrice = 10;
                steakQuantity++;
                steakQuantityNumber.setText(String.valueOf(steakQuantity));
                int stePrice = basePrice * steakQuantity;
                String setnewPrice = String.valueOf(stePrice);
                steakPrice.setText("€" + setnewPrice);
            }
        });

        steakMinusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                int baseSteakPrice = 10;
                // because we dont want the quantity go less than 0
                if (steakQuantity == 0) {
                    Toast.makeText(Menu.this, "Cant decrease quantity < 0", Toast.LENGTH_SHORT).show();
                } else {
                    steakQuantity--;
                    steakQuantityNumber.setText(String.valueOf(steakQuantity));
                    int stePrice = baseSteakPrice * steakQuantity;
                    String setnewPrice = String.valueOf(stePrice);
                    steakPrice.setText("€" + setnewPrice);

                }
            }
        });

        chickenCurryPlusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int basePrice = 10;
                chickenCurryQuantity++;
                chickenCurryQuantityNumber.setText(String.valueOf(chickenCurryQuantity));
                int curryPrice = basePrice * chickenCurryQuantity;
                String setnewPrice = String.valueOf(curryPrice);
                chickenCurryPrice.setText("€" + setnewPrice);
            }
        });

        chickenCurryMinusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                int baseCurryPrice = 10;
                // because we dont want the quantity go less than 0
                if (chickenCurryQuantity == 0) {
                    Toast.makeText(Menu.this, "Cant decrease quantity < 0", Toast.LENGTH_SHORT).show();
                } else {
                    chickenCurryQuantity--;
                    chickenCurryQuantityNumber.setText(String.valueOf(chickenCurryQuantity));
                    int curryPrice = baseCurryPrice * chickenCurryQuantity;
                    String setnewPrice = String.valueOf(curryPrice);
                    chickenCurryPrice.setText("€" + setnewPrice);

                }
            }
        });

        lasagnaPlusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int basePrice = 10;
                lasagnaQuantity++;
                lasagnaQuantityNumber.setText(String.valueOf(lasagnaQuantity));
                int lPrice = basePrice * lasagnaQuantity;
                String setnewPrice = String.valueOf(lPrice);
                lasagnaPrice.setText("€" + setnewPrice);
            }
        });

        lasagnaMinusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                int baseLasagnaPrice = 10;
                // because we dont want the quantity go less than 0
                if (lasagnaQuantity == 0) {
                    Toast.makeText(Menu.this, "Cant decrease quantity < 0", Toast.LENGTH_SHORT).show();
                } else {
                    lasagnaQuantity--;
                    lasagnaQuantityNumber.setText(String.valueOf(lasagnaQuantity));
                    int lPrice = baseLasagnaPrice * lasagnaQuantity;
                    String setnewPrice = String.valueOf(lPrice);
                    lasagnaPrice.setText("€" + setnewPrice);

                }
            }
        });

        cheeseCakePlusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int basePrice = 10;
                cheeseCakeQuantity++;
                cheeseCakeQuantityNumber.setText(String.valueOf(cheeseCakeQuantity));
                int cCakePrice = basePrice * cheeseCakeQuantity;
                String setnewPrice = String.valueOf(cCakePrice);
                cheeseCakePrice.setText("€" + setnewPrice);
            }
        });

        cheeseCakeMinusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                int baseCheeseCakePrice = 10;
                // because we dont want the quantity go less than 0
                if (cheeseCakeQuantity == 0) {
                    Toast.makeText(Menu.this, "Cant decrease quantity < 0", Toast.LENGTH_SHORT).show();
                } else {
                    cheeseCakeQuantity--;
                    cheeseCakeQuantityNumber.setText(String.valueOf(cheeseCakeQuantity));
                    int cCakePrice = baseCheeseCakePrice * cheeseCakeQuantity;
                    String setnewPrice = String.valueOf(cCakePrice);
                    cheeseCakePrice.setText("€" + setnewPrice);

                }
            }
        });

        chocolateBrowniePlusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int basePrice = 10;
                chocolateBrownieQuantity++;
                chocolateBrownieQuantityNumber.setText(String.valueOf(chocolateBrownieQuantity));
                int chocPrice = basePrice * chocolateBrownieQuantity;
                String setnewPrice = String.valueOf(chocPrice);
                chocolateBrowniePrice.setText("€" + setnewPrice);
            }
        });

        chocolateBrownieMinusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                int baseChocolateBrowniePrice = 10;
                // because we dont want the quantity go less than 0
                if (chocolateBrownieQuantity == 0) {
                    Toast.makeText(Menu.this, "Cant decrease quantity < 0", Toast.LENGTH_SHORT).show();
                } else {
                    chocolateBrownieQuantity--;
                    chocolateBrownieQuantityNumber.setText(String.valueOf(chocolateBrownieQuantity));
                    int chocPrice = baseChocolateBrowniePrice * chocolateBrownieQuantity;
                    String setnewPrice = String.valueOf(chocPrice);
                    chocolateBrowniePrice.setText("€" + setnewPrice);

                }
            }
        });

        iceCreamPlusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int basePrice = 10;
                iceCreamQuantity++;
                iceCreamQuantityNumber.setText(String.valueOf(iceCreamQuantity));
                int icePrice = basePrice * iceCreamQuantity;
                String setnewPrice = String.valueOf(icePrice);
                iceCreamPrice.setText("€" + setnewPrice);
            }
        });

        iceCreamMinusQuantity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                int baseIceCreamPrice = 10;
                // because we dont want the quantity go less than 0
                if (iceCreamQuantity == 0) {
                    Toast.makeText(Menu.this, "Cant decrease quantity < 0", Toast.LENGTH_SHORT).show();
                } else {
                    iceCreamQuantity--;
                    iceCreamQuantityNumber.setText(String.valueOf(iceCreamQuantity));
                    int icePrice = baseIceCreamPrice * iceCreamQuantity;
                    String setnewPrice = String.valueOf(icePrice);
                    iceCreamPrice.setText("€" + setnewPrice);

                }
            }
        });

    }

}