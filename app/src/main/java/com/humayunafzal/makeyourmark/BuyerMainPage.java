package com.humayunafzal.makeyourmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BuyerMainPage extends AppCompatActivity {

    RecyclerView rv_BMP1, rv_BMP2, rv_BMP3;
    TextView BMP_tv1, BMP_tv2, BMP_tv3;
    String[] categories;
    FirebaseDatabase database;
    DatabaseReference productsRef1, productsRef2, productsRef3, productsByCatRef1, productsByCatRef2, productsByCatRef3, userRef;
    List<Product> productList1, productList2, productList3;
    String userId;
    ImageView searchImg, ordersImg, collectionsImg;
    CircleImageView uImage;
    TextView uName, balance;
    LinearLayout userInfo;
    Button logoutBtn;
    AppDBHandler dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_main_page);

        categories = new String[]{"laptop", "calculator", "notebooks"};
        database = FirebaseDatabase.getInstance();
        productsRef1 = database.getReference("products");
        productsRef2 = database.getReference("products");
        productsRef3 = database.getReference("products");
        productsByCatRef1 = database.getReference("products_by_cat").child( categories[0] );
        productsByCatRef2 = database.getReference("products_by_cat").child( categories[1] );
        productsByCatRef3 = database.getReference("products_by_cat").child( categories[2] );
        userId = getIntent().getStringExtra("user_id");
        searchImg = findViewById(R.id.searchImg);
        ordersImg = findViewById(R.id.ordersImg);
        collectionsImg = findViewById(R.id.collectionsImg);
        uImage = findViewById(R.id.uImage);
        uName = findViewById(R.id.uName);
        balance = findViewById(R.id.balance);
        userRef = database.getReference("users").child( userId );
        userInfo = findViewById(R.id.user_info);
        logoutBtn = findViewById(R.id.logout_btn);
        dbHelper = new AppDBHandler( this );

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if( snapshot.getValue() != null ){
                    User user = snapshot.getValue(User.class);
                    Picasso.get().load( user.getImagePath() ).into( uImage );
                    uName.setText( user.getName() );
                    balance.setText( "Balance: " + user.getBalance() + " Rs" );
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyerMainPage.this, SearchProducts.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });

        ordersImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyerMainPage.this, BuyerOrders.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });

        collectionsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyerMainPage.this, Collections.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });

        rv_BMP1 = findViewById(R.id.rv_BMP1);
        rv_BMP2 = findViewById(R.id.rv_BMP2);
        rv_BMP3 = findViewById(R.id.rv_BMP3);

        BMP_tv1 = findViewById(R.id.BMP_tv1);
        BMP_tv2 = findViewById(R.id.BMP_tv2);
        BMP_tv3 = findViewById(R.id.BMP_tv3);

        BMP_tv1.setText( categories[0] );
        BMP_tv2.setText( categories[1] );
        BMP_tv3.setText( categories[2] );

        productList1 = new ArrayList<>();
        productList2 = new ArrayList<>();
        productList3 = new ArrayList<>();

        rv_BMP1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true) );
        rv_BMP1.setAdapter(new BuyerMainPageProductsAdapter(this, productList1, userId));

        rv_BMP2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true) );
        rv_BMP2.setAdapter(new BuyerMainPageProductsAdapter(this, productList2, userId));

        rv_BMP3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) );
        rv_BMP3.setAdapter(new BuyerMainPageProductsAdapter(this, productList3, userId));

        productsByCatRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList1.clear();
                for(DataSnapshot ds: snapshot.getChildren() ){
                    productsRef1.child( ds.getKey().toString() ).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            productList1.add( snapshot.getValue(Product.class) );
                            rv_BMP1.getAdapter().notifyDataSetChanged();
                            rv_BMP1.scrollToPosition( productList1.size() - 1 );
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        productsByCatRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList2.clear();
                for(DataSnapshot ds: snapshot.getChildren() ){
                    productsRef2.child( ds.getKey().toString() ).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            productList2.add( snapshot.getValue(Product.class) );
                            rv_BMP2.getAdapter().notifyDataSetChanged();
                            rv_BMP2.scrollToPosition( productList2.size() - 1 );
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        productsByCatRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList3.clear();
                for(DataSnapshot ds: snapshot.getChildren() ){
                    productsRef3.child( ds.getKey().toString() ).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            productList3.add( snapshot.getValue(Product.class) );
                            rv_BMP3.getAdapter().notifyDataSetChanged();
                            rv_BMP3.scrollToPosition( productList3.size() - 1 );
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        userInfo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(BuyerMainPage.this, ModifyUserInfoBuyer.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
                return true;
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( FirebaseAuth.getInstance().getCurrentUser() != null )
                    FirebaseAuth.getInstance().signOut();
                dbHelper.getWritableDatabase().execSQL("Update userInfo SET phone='" + userId + "' , status = '2' " );
                Intent intent = new Intent( BuyerMainPage.this, SignIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}