package com.humayunafzal.makeyourmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class OTPVerification extends AppCompatActivity {

    String phoneNumber;
    EditText code_;
    Button verify;
    FirebaseAuth mAuth;
    String code;
    String mVerificationId;
    FirebaseDatabase database;
    DatabaseReference userRef;
    AppDBHandler dbHelper;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        phoneNumber = getIntent().getStringExtra("phone");
        code_ = findViewById(R.id.code);
        verify = findViewById(R.id.verify);
        code = "";
        mVerificationId = "";
        dbHelper = new AppDBHandler( this );
        user = new User();

        mAuth = FirebaseAuth.getInstance();
        sendVerificationCode(phoneNumber);

        database = FirebaseDatabase.getInstance();
        userRef = database.getReference().child("users");

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = code_.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    return;
                }
                verifyVerificationCode(code);
            }
        });
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                code_.setText(code);
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPVerification.this,  "OTP Verification Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTPVerification.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                                userRef.child( "0" + phoneNumber.substring(3) ).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if( snapshot.getValue() != null ) {
                                            user = snapshot.getValue(User.class);
                                            dbHelper.getWritableDatabase().execSQL("Update userInfo SET phone='" + "0" + phoneNumber.substring(3) + "' , status = '1' " );

                                            Intent intent = new Intent();
                                            if( user.getType() == "b")
                                                intent = new Intent(OTPVerification.this, BuyerMainPage.class);
                                            else
                                                intent = new Intent(OTPVerification.this, SellerMainPage.class);
                                            intent.putExtra("user_id", user.getPhone() );
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { }
                                });
                            }
                        else {
                            Toast.makeText(OTPVerification.this, "An issue occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OTPVerification.this, "OTP verification failed", Toast.LENGTH_LONG).show();
            }
        });
    }

}