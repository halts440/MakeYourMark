package com.humayunafzal.makeyourmark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddProduct extends AppCompatActivity {

    EditText pName, pPrice, pDescription, pStock;
    ImageView pImage;
    Button pAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        pName = findViewById(R.id.pName);
        pPrice = findViewById(R.id.pPrice);
        pDescription = findViewById(R.id.pDescription);
        pStock = findViewById(R.id.pStock);
        pImage = findViewById(R.id.pImage);
        pAdd = findViewById(R.id.pAdd);

        pAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}