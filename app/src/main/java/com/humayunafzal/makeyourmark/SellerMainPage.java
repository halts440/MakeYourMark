package com.humayunafzal.makeyourmark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SellerMainPage extends AppCompatActivity {

    TextView totalSales, salesTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main_page);

        totalSales = findViewById(R.id.totalSales);
        salesTotal = findViewById(R.id.salesToday);
    }
}