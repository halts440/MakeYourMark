package com.humayunafzal.makeyourmark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignIn extends AppCompatActivity {

    EditText lEmail, lPassword;
    Button lSignIn, lSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        lEmail = findViewById(R.id.lEmail);
        lPassword = findViewById(R.id.lPassword);
        lSignIn = findViewById(R.id.lSignIn);
        lSignUp = findViewById(R.id.lSignUp);

        lSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn.this, SignUpOptions.class);
                startActivity(intent);
            }
        });

        lSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}