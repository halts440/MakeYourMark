package com.humayunafzal.makeyourmark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SellerSignup extends AppCompatActivity {

    EditText sBusinessName, sEmail, sPassword, sLocation;
    Button sCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_signup);

        sBusinessName = findViewById(R.id.sBusinessName);
        sEmail = findViewById(R.id.sEmail);
        sPassword = findViewById(R.id.sPassword);
        sLocation = findViewById(R.id.sLocation);
        sCreate = findViewById(R.id.sCreate);

        sCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}