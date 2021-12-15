package com.humayunafzal.makeyourmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    AppDBHandler dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    int status = 0;
    String phoneNumber;
    Intent intent;
    FirebaseDatabase database;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        dbHelper = new AppDBHandler(this);
        db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("Select * from userInfo", null);
        cursor.moveToFirst();
        status = Integer.valueOf( cursor.getString(cursor.getColumnIndex(AppDBHandler.UserInfo.CN_STATUS) ) );
        phoneNumber = cursor.getString(cursor.getColumnIndex(AppDBHandler.UserInfo.CN_PHONE) );
        intent = new Intent();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference().child("users").child(phoneNumber);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // first time user
                if( status == 0 ) {
                    intent = new Intent(SplashScreen.this, SignUpOptions.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                // user is logged in
                else if( status == 1) {
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if( snapshot.getValue() != null ) {
                                User user = snapshot.getValue(User.class);
                                if( user.getType() == "b")
                                    intent = new Intent(SplashScreen.this, BuyerMainPage.class);
                                else
                                    intent = new Intent(SplashScreen.this, SellerMainPage.class);
                                intent.putExtra("user_id", user.getPhone() );
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
                }
                // user was logged out
                else if( status == 2 ) {
                    intent = new Intent(SplashScreen.this, SignIn.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        }, 800);
    }
}