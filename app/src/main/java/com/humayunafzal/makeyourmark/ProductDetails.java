package com.humayunafzal.makeyourmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

public class ProductDetails extends AppCompatActivity {

    ImageView pImage;
    TextView pName, pPrice, pDescription;
    NumberPicker quantity;
    Button buy_btn;
    String product_id;
    FirebaseDatabase database;
    DatabaseReference productsRef, ordersRef, ordersByBuyerRef, ordersBySellerRef;
    Product p;
    String userId;

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
        ordersByBuyerRef = database.getReference("orders_by_buyer");
        ordersBySellerRef = database.getReference("orders_by_seller");
        p = new Product();
        userId = getIntent().getStringExtra("user_id");
        quantity.setMinValue(1);
        quantity.setMaxValue(10);

        productsRef.child( product_id ).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                p = snapshot.getValue(Product.class);
                pName.setText( p.getName() );
                pPrice.setText( p.getPrice() + " Rs" );
                pDescription.setText( p.getDescription() );
                Picasso.get().load( p.getImage() ).into( pImage );
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get stock value
                //int stockVal = productsRef.child( p.getProductId() );

                String order_id = UUID.randomUUID().toString();
                String orderDate = LocalDate.now().toString();

                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("hh:mm");
                String orderTime = myDateObj.format(myFormatObj);

                Order order = new Order(order_id, p.getProductId(), p.getPrice(), String.valueOf( quantity.getValue() ), userId, p.getStoreId(), orderDate, orderTime );

                ordersRef.child( order_id ).setValue( order );
                ordersByBuyerRef.child( userId ).child( order_id ).setValue(1);
                ordersBySellerRef.child( p.getStoreId() ).child( order_id ).setValue(1);

                Toast.makeText(ProductDetails.this, "Order created", Toast.LENGTH_SHORT).show();
            }
        });
    }
}