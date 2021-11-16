package com.humayunafzal.makeyourmark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

public class ProductDetails extends AppCompatActivity {

    ImageView pdImage;
    TextView pdName, pdPrice, pdDescription;
    NumberPicker pdQuantity;
    Button pdBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        pdImage = findViewById(R.id.pdImage);
        pdName = findViewById(R.id.pdName);
        pdPrice = findViewById(R.id.pdPrice);
        pdDescription = findViewById(R.id.pdDescription);
        pdQuantity = findViewById(R.id.pdQuantity);
        pdBuy = findViewById(R.id.pdBuy);

        pdBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}