package com.humayunafzal.makeyourmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyerOrders extends AppCompatActivity {

    RecyclerView rv_Orders;
    List<Order> orders;
    FirebaseDatabase database;
    DatabaseReference ordersRef, buyerOrderRef;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_orders);

        rv_Orders = findViewById(R.id.rv_Orders);
        userId = getIntent().getStringExtra("user_id");
        database = FirebaseDatabase.getInstance();
        ordersRef = database.getReference("orders");
        buyerOrderRef = database.getReference("orders_by_buyer").child( userId );

        orders = new ArrayList<>();
        rv_Orders.setLayoutManager(new LinearLayoutManager(this) );
        rv_Orders.setAdapter(new BuyerViewOrdersAdapter(this, orders));

        buyerOrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if( snapshot.getValue() != null ) {
                    orders.clear();
                    for(DataSnapshot ds: snapshot.getChildren() ){
                        ordersRef.child( ds.getKey() ).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Order order = snapshot.getValue(Order.class);
                                orders.add( order );
                                rv_Orders.getAdapter().notifyDataSetChanged();
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
}