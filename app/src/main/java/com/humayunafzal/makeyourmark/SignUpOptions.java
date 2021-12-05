package com.humayunafzal.makeyourmark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpOptions extends AppCompatActivity {

    Button optCreateBuyer, optCreateSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_options);

        optCreateBuyer = findViewById(R.id.optCreateBuyer);
        optCreateSeller = findViewById(R.id.optCreateSeller);

        // if user wants to create buyer profile, open the signup page for buyers
        optCreateBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpOptions.this, BuyerSignup.class);
                startActivity(intent);
            }
        });

        // if user wants to create seller profile, open the signup page for sellers
        optCreateSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpOptions.this, SellerSignup.class);
                startActivity(intent);
            }
        });
    }
}