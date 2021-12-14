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

public class ModifyUserInfoBuyer extends AppCompatActivity {

    CircleImageView uImage;
    TextView uName, uBalance;
    EditText uLocation;
    Button update_btn;
    FirebaseDatabase database;
    DatabaseReference userRef;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info_buyer);

        userId = getIntent().getStringExtra("user_id");
        uImage = findViewById(R.id.uImage);
        uName = findViewById(R.id.uName);
        uBalance = findViewById(R.id.uBalance);
        uLocation = findViewById(R.id.uLocation);
        update_btn = findViewById(R.id.update_btn);
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users").child( userId );

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if( snapshot.getValue() != null ) {
                    User u = snapshot.getValue(User.class);
                    uName.setText( "User Name: " + u.getName() );
                    uBalance.setText( "Balance: " + u.getBalance() );
                    uLocation.setText( u.getLocation() );
                    Picasso.get().load( u.getImagePath() ).into( uImage );
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.child("location").setValue( uLocation.getText().toString() );
                Toast.makeText(ModifyUserInfoBuyer.this, "Buyer information updated", Toast.LENGTH_SHORT).show();
            }
        });

    }
}