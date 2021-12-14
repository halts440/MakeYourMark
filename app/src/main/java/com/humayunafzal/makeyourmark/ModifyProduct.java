package com.humayunafzal.makeyourmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ModifyProduct extends AppCompatActivity {

    TextView pName, pDescription;
    EditText pPrice, pStock;
    ImageView pImage;
    Button modify_btn;
    FirebaseDatabase database;
    DatabaseReference productsRef;
    String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_product);

        productId = getIntent().getStringExtra("product_id");
        pName = findViewById(R.id.pName);
        pDescription = findViewById(R.id.pDescription);
        pPrice = findViewById(R.id.pPrice);
        pStock = findViewById(R.id.pStock);
        pImage = findViewById(R.id.pImage);
        modify_btn = findViewById(R.id.modify_btn);
        database = FirebaseDatabase.getInstance();
        productsRef = database.getReference("products").child( productId );

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if( snapshot.getValue() != null ) {
                    Product p = snapshot.getValue(Product.class);
                    pName.setText( p.getName() );
                    pDescription.setText( p.getDescription() );
                    pPrice.setText( p.getPrice() );
                    pStock.setText( p.getStock() );
                    Picasso.get().load( p.getImage() ).into( pImage );
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        modify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productsRef.child("price").setValue( pPrice.getText().toString() );
                productsRef.child("stock").setValue( pStock.getText().toString() );
                Toast.makeText(ModifyProduct.this, "Product information updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}