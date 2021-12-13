package com.humayunafzal.makeyourmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.LinkAddress;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SellerAllProducts extends AppCompatActivity {

    RecyclerView rv_sellerAllProducts;
    FirebaseDatabase database;
    DatabaseReference productsRef;
    List<Product> allProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_all_products);

        database = FirebaseDatabase.getInstance();
        productsRef = database.getReference("products");
        rv_sellerAllProducts = findViewById(R.id.rv_sellerAllProducts);
        allProducts = new ArrayList<>();

        rv_sellerAllProducts.setLayoutManager(new LinearLayoutManager(this) );
        rv_sellerAllProducts.setAdapter(new SellerAllProductsAdapter(this, allProducts));

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if( snapshot.getValue() != null ) {
                    allProducts.clear();
                    for(DataSnapshot ds : snapshot.getChildren() ) {
                        Product p = ds.getValue(Product.class);
                        allProducts.add(p);
                    }
                    rv_sellerAllProducts.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}