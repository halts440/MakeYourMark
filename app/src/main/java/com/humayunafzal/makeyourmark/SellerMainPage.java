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

import java.time.LocalDate;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerMainPage extends AppCompatActivity {

    TextView totalSales, salesToday;
    String phone;
    FirebaseDatabase database;
    DatabaseReference usersRef, sellerOrdersRef, ordersRef;
    TextView add_product_btn, manage_products_btn, balance;
    LinearLayout view_orders_btn;
    int countSalesToday, countTotalSales;
    LinearLayout userInfo;

    CircleImageView bImage;
    TextView bName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main_page);

        bImage = findViewById(R.id.bImage);
        bName = findViewById(R.id.bName);
        balance = findViewById(R.id.balance);
        countSalesToday = 0;
        countTotalSales = 0;

        userInfo = findViewById(R.id.user_info);
        add_product_btn = findViewById(R.id.add_product_btn);
        manage_products_btn = findViewById(R.id.manage_products_btn);
        totalSales = findViewById(R.id.totalSales);
        salesToday = findViewById(R.id.salesToday);
        view_orders_btn = findViewById(R.id.view_orders_btn);
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        phone = "03167334892"; //getIntent().getStringExtra("phone");
        sellerOrdersRef = database.getReference("orders_by_seller").child( phone );
        ordersRef = database.getReference("orders");
        usersRef.child( phone ).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if( snapshot.getValue() != null ){
                    User user = snapshot.getValue(User.class);
                    Picasso.get().load( user.getImagePath() ).into( bImage );
                    bName.setText( user.getName() );
                    balance.setText( "Balance: " + user.getBalance() + " Rs" );
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
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

        view_orders_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerMainPage.this, SellerOrders.class);
                intent.putExtra("user_id", phone);
                startActivity(intent);
            }
        });

        sellerOrdersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if( snapshot.getValue() != null ) {
                    String dateToday = LocalDate.now().toString();
                    countSalesToday = 0;
                    countTotalSales = 0;
                    for(DataSnapshot ds: snapshot.getChildren() ) {
                        countTotalSales += 1;
                        ordersRef.child( ds.getKey() ).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if( snapshot.getValue(Order.class).getDate() == dateToday ) {
                                    countSalesToday += 1;
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });
                    }
                    totalSales.setText( String.valueOf(countTotalSales) );
                    salesToday.setText( String.valueOf(countSalesToday) );
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        userInfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(SellerMainPage.this, ModifyUserInfoSeller.class);
                intent.putExtra("user_id", phone );
                startActivity(intent);
                return true;
            }
        });
    }
}