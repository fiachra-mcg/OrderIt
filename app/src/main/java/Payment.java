package com.example.orderit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Payment extends AppCompatActivity {

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    EditText email, cardName;
    SharedPreferences sp, sharedPref;
    ListView list;
    ImageView home;
    private PaymentsClient paymentsClient;
    String emailAdd, card;
    Button payButton;
    BottomNavigationView bottomNavigationView;

    private static final String BACKEND_URL = "http://10.0.2.2:4242/";
    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;
    int amountString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //Defining variables
        rootNode = FirebaseDatabase.getInstance();
        email = findViewById(R.id.email);
        cardName = findViewById(R.id.cardName);
        list = findViewById(R.id.list);
        home = findViewById(R.id.homeLogo);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.barRestaurant);
        payButton = findViewById(R.id.payButton);

        amountString = getIntent().getIntExtra("AMOUNT", 0);


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
                                , com.example.orderit.List.class));
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

        //Home Image button to bring you to home page
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment.this, Homepage.class);
                startActivity(intent);
            }
        });

        // Configure the SDK with your Stripe publishable key so it can make requests to Stripe
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51IiJnOEzexIrErwnAW0UhrqqR7p4IlaeiArzq8gctyplOsiFWbzwZXc3iHbbOpthVTH2B7MHiTgP74rwX66ujBVT00gE8KDM7J")
        );
        startCheckout();

    }

    private void startCheckout() {
        // Create a PaymentIntent by calling the server's endpoint.
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        int amount = amountString;
        Map<String, Object> payMap = new HashMap<>();
        Map<String, Object> itemMap = new HashMap<>();
        List<Map<String, Object>> itemList = new ArrayList<>();
        payMap.put("currency", "eur");
        itemMap.put("id", "photo_subscription");
        itemMap.put("amount", amount);
        itemList.add(itemMap);
        payMap.put("items", itemList);
        String json = new Gson().toJson(payMap);

        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(BACKEND_URL + "create-payment-intent")
                .post(body)
                .build();
        httpClient.newCall(request)
                .enqueue(new PayCallback(this));

        // Hook up the pay button to the card widget and stripe instance
        payButton.setOnClickListener((View view) -> {
            CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            emailAdd = email.getText().toString();
            card = cardName.getText().toString();
            if (params == null || card.isEmpty() || emailAdd.isEmpty()) {
                Toast.makeText(Payment.this, "Please Fill in Required Information", Toast.LENGTH_SHORT).show();
            }else {
                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                stripe.confirmPayment(this, confirmParams);

                //get shared preferences details
                sp = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                sharedPref = getApplicationContext().getSharedPreferences("TITLE", Context.MODE_PRIVATE);

                String place = sharedPref.getString("markertxt", "");

                //replace characters that firebase doesn't accept in email addresses
                String newEmailAd = emailAdd.replace(".", ",")
                        .replace("#","*").replace("$","&")
                        .replace("[","(").replace("]",")");

                //Get order details
                Intent i = getIntent();
                ArrayList<String> arrayList = i.getStringArrayListExtra("list");

                int tableNum = i.getIntExtra("TABLE", 0);

                String table = "Table " + tableNum;

                //Getting today's date
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());

                //Saving data to database
                reference = rootNode.getReference(place);
                reference.child("Orders").child(table).child(newEmailAd).child(format).setValue(arrayList);

                //Clearing shared Preferences
                sharedPref.edit().clear().apply();
                sp.edit().clear().apply();

                //Start Menu page
                Intent intent = new Intent(Payment.this, Menu.class);
                intent.putExtra("title", place);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    private static final class PayCallback implements Callback {
        @NonNull private final WeakReference<Payment> activityRef;
        PayCallback(@NonNull Payment activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final Payment activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error: " + e.toString(), Toast.LENGTH_LONG
                    ).show()
            );
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final Payment activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG
                        ).show()
                );
            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }

    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");
    }

    private static final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<Payment> activityRef;
        PaymentResultCallback(@NonNull Payment activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final Payment activity = activityRef.get();
            if (activity == null) {
                return;
            }

            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Toast.makeText(activity, "Ordered Successfully", Toast.LENGTH_SHORT).show();

            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final Payment activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }

    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }

}