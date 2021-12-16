package com.humayunafzal.makeyourmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchProducts extends AppCompatActivity {

    EditText searchText;
    ImageView searchBtn;
    TextView searchQueryTitle;
    RecyclerView rv_SearchResults;
    FirebaseDatabase database;
    DatabaseReference productsRef;
    List<Product> productList;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        searchText = findViewById(R.id.search_text);
        searchBtn = findViewById(R.id.search_btn);
        searchQueryTitle = findViewById(R.id.search_query_title);
        rv_SearchResults = findViewById(R.id.rv_SearchResults);
        database = FirebaseDatabase.getInstance();
        productsRef = database.getReference("products");
        userId = getIntent().getStringExtra("user_id");

        productList = new ArrayList<>();
        rv_SearchResults.setLayoutManager(new LinearLayoutManager(this) );
        rv_SearchResults.setAdapter(new SearchProductsAdapter(this, productList, userId));

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( searchText.getText().toString().isEmpty() != true ) {
                    String query = searchText.getText().toString().toLowerCase();
                    searchQueryTitle.setText( searchText.getText().toString() );
                    productsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            productList.clear();
                            for(DataSnapshot ds: snapshot.getChildren() ){
                                productsRef.child( ds.getKey().toString() ).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Product tempProduct = snapshot.getValue(Product.class);
                                        if( tempProduct.getName().toLowerCase().contains(query) == true ) {
                                            productList.add(tempProduct ) ;
                                            rv_SearchResults.getAdapter().notifyDataSetChanged();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
                }
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });



    }
}