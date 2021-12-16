package com.humayunafzal.makeyourmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BuyerSignup extends AppCompatActivity {

    EditText userName, phone, location;
    ImageView profilePicture;
    Button create_btn;
    FirebaseDatabase database;
    DatabaseReference usersRef;
    FirebaseStorage storage;
    StorageReference logosRef;
    AppDBHandler dbHelper;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_signup);

        dbHelper = new AppDBHandler( this );
        userName = findViewById(R.id.user_name);
        phone = findViewById(R.id.phone);
        location = findViewById(R.id.location);
        create_btn = findViewById(R.id.create_btn);
        profilePicture = findViewById(R.id.profile_picture);
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        storage = FirebaseStorage.getInstance();
        logosRef = storage.getReference("profile_pictures");

        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( userName.getText().toString().isEmpty() || phone.getText().toString().isEmpty() ||
                        location.getText().toString().isEmpty() ) {
                    Toast.makeText(BuyerSignup.this, "Fill out all fields", Toast.LENGTH_LONG).show();
                }
                else {
                    // Get the data from an ImageView as bytes
                    profilePicture.setDrawingCacheEnabled(true);
                    profilePicture.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    String fileName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
                    UploadTask uploadTask = logosRef.child( fileName ).putBytes(data);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> res = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            res.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    User newUser = new User(userName.getText().toString(), phone.getText().toString(), location.getText().toString(), uri.toString(), "b", "10000", OneSignal.getDeviceState().getUserId() );
                                    usersRef.child( phone.getText().toString() ).setValue(newUser);
                                    Toast.makeText(BuyerSignup.this, "Account created successfully", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(BuyerSignup.this, BuyerMainPage.class);
                                    intent.putExtra("user_id", phone.getText().toString() );
                                    dbHelper.getWritableDatabase().execSQL("Update userInfo SET phone='" + phone.getText().toString() + "' , status = '1' " );
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BuyerSignup.this, "Some issue occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            try {
                final InputStream imageStream = getContentResolver().openInputStream( data.getData() );
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                profilePicture.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(BuyerSignup.this, "Could not load image", Toast.LENGTH_LONG).show();
            }
        }
    }

}