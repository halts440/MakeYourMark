package com.humayunafzal.makeyourmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AddProduct extends AppCompatActivity {

    EditText pName, pPrice, pDescription, pStock, pCategory;
    ImageView pImage;
    Button pAdd;
    FirebaseDatabase database;
    DatabaseReference productsRef;
    DatabaseReference productsByCatRef;
    DatabaseReference productsByStoreRef;
    FirebaseStorage storage;
    StorageReference productsImageRef;
    String storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        pName = findViewById(R.id.pName);
        pPrice = findViewById(R.id.pPrice);
        pDescription = findViewById(R.id.pDescription);
        pStock = findViewById(R.id.pStock);
        pImage = findViewById(R.id.pImage);
        pCategory = findViewById(R.id.pCategory);
        pAdd = findViewById(R.id.pAdd);
        storeId = getIntent().getStringExtra("phone");
        database = FirebaseDatabase.getInstance();
        productsRef = database.getReference("products");
        productsByCatRef = database.getReference("products_by_cat");
        productsByStoreRef = database.getReference("products_by_store");
        storage = FirebaseStorage.getInstance();
        productsImageRef = storage.getReference("productImages");

        pAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( pName.getText().toString().isEmpty() || pPrice.getText().toString().isEmpty() ||
                        pDescription.getText().toString().isEmpty() ||  pCategory.getText().toString().isEmpty() ||
                        pStock.getText().toString().isEmpty() ) {
                    Toast.makeText(AddProduct.this, "Fill out all fields", Toast.LENGTH_LONG).show();
                }
                else {
                    // Get the data from an ImageView as bytes
                    pImage.setDrawingCacheEnabled(true);
                    pImage.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) pImage.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    String fileName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
                    UploadTask uploadTask = productsImageRef.child( fileName ).putBytes(data);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> res = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            res.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String product_id = UUID.randomUUID().toString();
                                    Product product = new Product( product_id, pName.getText().toString(), pPrice.getText().toString(), pStock.getText().toString(), uri.toString(), storeId, pCategory.getText().toString(), pDescription.getText().toString() );
                                    productsRef.child( product_id ).setValue(product);
                                    productsByCatRef.child( pCategory.getText().toString() ).child( product_id ).setValue(1);
                                    productsByStoreRef.child( storeId ).child( product_id ).setValue(1);
                                    Toast.makeText(AddProduct.this, "Product added successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        }
                    });
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddProduct.this, "Some issue occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        pImage.setOnClickListener(new View.OnClickListener() {
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
                pImage.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddProduct.this, "Could not load image", Toast.LENGTH_LONG).show();
            }
        }
    }
}