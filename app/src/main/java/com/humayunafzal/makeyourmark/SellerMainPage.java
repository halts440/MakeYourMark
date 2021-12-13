package com.humayunafzal.makeyourmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerMainPage extends AppCompatActivity {

    TextView totalSales, salesTotal;
    String phone;
    FirebaseDatabase database;
    DatabaseReference usersRef;
    TextView add_product_btn, manage_products_btn;
    LinearLayout view_orders;

    CircleImageView pImage;
    TextView bName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main_page);

        pImage = findViewById(R.id.pImage);
        bName = findViewById(R.id.bName);

        add_product_btn = findViewById(R.id.add_product_btn);
        manage_products_btn = findViewById(R.id.manage_products_btn);
        totalSales = findViewById(R.id.totalSales);
        salesTotal = findViewById(R.id.salesToday);
        view_orders = findViewById(R.id.view_orders);
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        phone = "03167334892"; //getIntent().getStringExtra("phone");
        usersRef.child( phone ).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if( snapshot.getValue() != null ){
                    User user = snapshot.getValue(User.class);
                    Picasso.get().load( user.getImagePath() ).into( pImage );
                    bName.setText( user.getName() );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        add_product_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerMainPage.this, AddProduct.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
            }
        });

        manage_products_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerMainPage.this, SellerAllProducts.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
            }
        });
    }
}