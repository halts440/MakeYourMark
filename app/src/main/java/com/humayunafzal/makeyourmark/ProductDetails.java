package com.humayunafzal.makeyourmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

public class ProductDetails extends AppCompatActivity {

    ImageView pImage;
    TextView pName, pPrice, pDescription;
    NumberPicker quantity;
    Button buy_btn;
    String product_id;
    FirebaseDatabase database;
    DatabaseReference usersRef, productsRef, ordersRef, ordersByBuyerRef, ordersBySellerRef;
    Product p;
    String userId;
    User buyer, seller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        pImage = findViewById(R.id.pImage);
        pName = findViewById(R.id.pName);
        pPrice = findViewById(R.id.pPrice);
        pDescription = findViewById(R.id.pDescription);
        quantity = findViewById(R.id.quantity);
        buy_btn = findViewById(R.id.buy_btn);
        product_id = getIntent().getStringExtra("product_id");
        database = FirebaseDatabase.getInstance();
        productsRef = database.getReference("products");
        ordersRef = database.getReference("orders");
        usersRef = database.getReference("users");
        ordersByBuyerRef = database.getReference("orders_by_buyer");
        ordersBySellerRef = database.getReference("orders_by_seller");
        p = new Product();
        userId = getIntent().getStringExtra("user_id");
        quantity.setMinValue(1);
        quantity.setMaxValue(10);
        buyer = new User();
        seller = new User();

        productsRef.child( product_id ).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                p = snapshot.getValue(Product.class);
                pName.setText( p.getName() );
                pPrice.setText( p.getPrice() + " Rs" );
                pDescription.setText( p.getDescription() );
                Picasso.get().load( p.getImage() ).into( pImage );
                quantity.setMaxValue( Integer.valueOf(p.getStock() ) );
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int totalPrice = quantity.getValue() * Integer.valueOf( p.getPrice() );
                buyer = new User();
                seller = new User();
                usersRef.child( userId ).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if( snapshot.getValue() != null ) {
                            buyer = snapshot.getValue(User.class);
                            if( totalPrice > Integer.valueOf( buyer.getBalance() )) {
                                Toast.makeText(ProductDetails.this, "Not enough balance", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                usersRef.child( p.getStoreId() ).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                        if( snapshot2.getValue() != null ) {
                                            seller = snapshot2.getValue(User.class);
                                            int buyerNewBalance = Integer.valueOf( buyer.getBalance() ) - totalPrice;
                                            int sellerNewBalance = Integer.valueOf( seller.getBalance() ) + totalPrice;

                                            String order_id = UUID.randomUUID().toString();
                                            String orderDate = LocalDate.now().toString();

                                            LocalDateTime myDateObj = LocalDateTime.now();
                                            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("hh:mm");
                                            String orderTime = myDateObj.format(myFormatObj);

                                            Order order = new Order(order_id, p.getProductId(), p.getPrice(), String.valueOf( quantity.getValue() ), userId, p.getStoreId(), orderDate, orderTime );

                                            ordersRef.child( order_id ).setValue( order );
                                            ordersByBuyerRef.child( userId ).child( order_id ).setValue(1);
                                            ordersBySellerRef.child( p.getStoreId() ).child( order_id ).setValue(1);

                                            // update balances
                                            usersRef.child( userId ).child("balance").setValue( String.valueOf(buyerNewBalance) );
                                            usersRef.child( p.getStoreId() ).child("balance").setValue( String.valueOf(sellerNewBalance) );

                                            Toast.makeText(ProductDetails.this, "Order created", Toast.LENGTH_SHORT).show();

                                            try {
                                                OneSignal.postNotification(new JSONObject("{'contents':{'en':'" +
                                                                buyer.getName() + " bought " + order.getQuantity() + " items from your store" + "'},'headings':{'en':'"+
                                                                "New Sales"+
                                                                "'},'include_player_ids': ['" +
                                                                seller.getPlayerId() +
                                                                "']}"),
                                                        new OneSignal.PostNotificationResponseHandler() {
                                                            @Override
                                                            public void onSuccess(JSONObject response) {
                                                                Log.d("response",response.toString());
                                                            }
                                                            @Override
                                                            public void onFailure(JSONObject response) {
                                                                Log.d("response",response.toString());
                                                            }
                                                        });
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { }
                                });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
        });
    }
}