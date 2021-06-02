package com.example.orderit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SummaryActivity extends AppCompatActivity {

    ListView list;
    EditText tableNum;
    Button clear;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;
    SharedPreferences sp;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    Button payment_page;
    ImageView home;
    CheckBox checkBox;
    BottomNavigationView bottomNavigationView;

    int total = 0;

    HashMap<String, Integer> basePricesEur = new HashMap<String, Integer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        //Assigning Variables
        sp = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        arrayList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);

        list = findViewById(R.id.list);
        clear = findViewById(R.id.clear);
        payment_page = findViewById(R.id.payment);
        tableNum = findViewById(R.id.tableNumber);
        checkBox = findViewById(R.id.checkBox);

        home = findViewById(R.id.homeLogo);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Order");

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.barRestaurant);

        //all base prices stored in hash map (must be same name as shared pref keys)
        basePricesEur.put("BEER", 5);
        basePricesEur.put("COKE", 3);
        basePricesEur.put("WINE", 5);

        basePricesEur.put("SOUP", 6);
        basePricesEur.put("CHICKENWINGS", 8);
        basePricesEur.put("SALAD", 7);

        basePricesEur.put("STEAK", 10);
        basePricesEur.put("CHICKENCURRY", 10);
        basePricesEur.put("LASAGNA", 10);

        basePricesEur.put("CHEESECAKE", 10);
        basePricesEur.put("CHOCOLATEBROWNIE", 10);
        basePricesEur.put("ICECREAM", 10);

        // create a map containing all the keys / items from the shared preferences
        // using this map we can loop through the items and assign base prices / make calculations etc
        Map<String, ?> keys = sp.getAll();

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

        //check if our Map is empty (cart empty)
        if (keys == null) {
            Toast.makeText(getApplicationContext(), "Cart Is Empty", Toast.LENGTH_LONG).show();
        }
        // else loop through items in cart to get total price based on the base prices hash map and
        // map created from the shared preferences
        else {
            for (Map.Entry<String, ?> entry : keys.entrySet()) {

                String item = entry.getKey();
                int quantity = sp.getInt(item, 0);
                int price = 0;
                if (basePricesEur.containsKey(item)) {
                    price = basePricesEur.get(item) * quantity;
                }

                total += price;

                // Also add items in cart and their prices to array list for cart view
                arrayList.add(item + " X" + quantity + ": €" + price);

            }
        }
        // if items are in the cart add total price to array list for cart view
        if (total > 0){
            arrayList.add("Total" + ": €" + total);
        }
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Button to clear cart
        clear.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onClick(View v) {

                sp.edit().clear().apply();
                list.setVisibility(View.GONE);

            }

        });

        //Enabling payButton
        CompoundButton.OnCheckedChangeListener checker = new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean b) {
                if(checkBox.isChecked()){
                    payment_page.setEnabled(true);
                }
                else if(payment_page.isEnabled()){
                    payment_page.setEnabled(false);
                }

            }

        };
        checkBox.setOnCheckedChangeListener(checker);

        //linking payment button
        payment_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.getCount()==0 || TextUtils.isEmpty(tableNum.getText().toString())) {
                    Toast.makeText(SummaryActivity.this, "Please Fill in Table Number or add items to your Cart", Toast.LENGTH_SHORT).show();
                } else {
                    int table = Integer.parseInt(tableNum.getText().toString());
                    int amount = total * 100;

                    Intent intent = new Intent(SummaryActivity.this, Payment.class);
                    intent.putStringArrayListExtra("list", arrayList);
                    intent.putExtra("TABLE", table);
                    intent.putExtra("AMOUNT", amount);
                    startActivity(intent);

                }
            }
        });

        //Home Image button to bring you to home page
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryActivity.this,Homepage.class);
                startActivity(intent);
            }
        });

    }
}