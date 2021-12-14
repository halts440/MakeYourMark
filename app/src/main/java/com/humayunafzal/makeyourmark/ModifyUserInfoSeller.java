package com.humayunafzal.makeyourmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ModifyUserInfoSeller extends AppCompatActivity {

    CircleImageView bImage;
    TextView bName, bBalance;
    EditText bLocation;
    Button update_btn;
    FirebaseDatabase database;
    DatabaseReference userRef;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info_seller);

        userId = getIntent().getStringExtra("user_id");
        bImage = findViewById(R.id.bImage);
        bName = findViewById(R.id.bName);
        bBalance = findViewById(R.id.bBalance);
        bLocation = findViewById(R.id.bLocation);
        update_btn = findViewById(R.id.update_btn);
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users").child( userId );

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if( snapshot.getValue() != null ) {
                    User u = snapshot.getValue(User.class);
                    bName.setText( "Business Name: " +  u.getName() );
                    bBalance.setText( "Balance: " + u.getBalance() );
                    bLocation.setText( u.getLocation() );
                    Picasso.get().load( u.getImagePath() ).into( bImage );
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.child("location").setValue( bLocation.getText().toString() );
                Toast.makeText(ModifyUserInfoSeller.this, "Seller information updated", Toast.LENGTH_SHORT).show();
            }
        });

    }
}