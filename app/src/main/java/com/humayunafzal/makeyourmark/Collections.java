package com.humayunafzal.makeyourmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Collections extends AppCompatActivity {

    ImageView uploadImageBtn, cameraBtn, myCollectionsBtn, allCollectionsBtn;
    FirebaseDatabase database;
    DatabaseReference collectionsRefD, collectionsByUserRef, collectionsRefD2;
    FirebaseStorage storage;
    StorageReference collectionsRefS;
    String userId;
    RecyclerView rv_Collections;
    List<CollectionItem> collectionItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);

        uploadImageBtn = findViewById(R.id.upload_image_btn);
        cameraBtn = findViewById(R.id.camera_btn);
        myCollectionsBtn = findViewById(R.id.my_collections);
        allCollectionsBtn = findViewById(R.id.all_collections);
        database = FirebaseDatabase.getInstance();
        collectionsRefD = database.getReference("collections");
        collectionsRefD2 = database.getReference("collections");
        storage = FirebaseStorage.getInstance();
        collectionsRefS = storage.getReference("collections");
        userId = getIntent().getStringExtra("user_id");
        rv_Collections = findViewById(R.id.rv_Collections);
        collectionsByUserRef = database.getReference("collections_by_user").child(userId);
        collectionItems = new ArrayList<>();

        rv_Collections.setLayoutManager(new LinearLayoutManager(this) );
        rv_Collections.setAdapter(new CollectionAdapter(this, collectionItems ));

        collectionsRefD.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if( snapshot.getValue() != null ) {
                    collectionItems.clear();
                    for(DataSnapshot ds: snapshot.getChildren() ) {
                        collectionItems.add( ds.getValue(CollectionItem.class) );
                    }
                    rv_Collections.getAdapter().notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        myCollectionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectionsByUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if( snapshot.getValue() != null ) {
                            collectionItems.clear();
                            for(DataSnapshot ds: snapshot.getChildren() ) {
                                collectionsRefD2.child( ds.getKey() ).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot s) {
                                        collectionItems.add( s.getValue(CollectionItem.class) );
                                        rv_Collections.getAdapter().notifyDataSetChanged();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) { }
                                });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
        });

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
            }
        });

        allCollectionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectionsRefD.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if( snapshot.getValue() != null ) {
                            collectionItems.clear();
                            for(DataSnapshot ds: snapshot.getChildren() ) {
                                collectionItems.add( ds.getValue(CollectionItem.class) );
                            }
                            rv_Collections.getAdapter().notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Collections.this, new String[]{Manifest.permission.CAMERA}, 99);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1099);
                }
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
                // upload
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] byteData = baos.toByteArray();
                String fileName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
                UploadTask uploadTask = collectionsRefS.child( fileName ).putBytes(byteData);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> res = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        res.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String collection_id = UUID.randomUUID().toString();
                                CollectionItem collectionItem = new CollectionItem( collection_id, uri.toString(), userId );
                                collectionsRefD.child( collection_id ).setValue( collectionItem );
                                collectionsByUserRef.child(collection_id).setValue(1);
                                Toast.makeText(Collections.this, "Collection added successfully", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Collections.this, "Some issue occurred", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(Collections.this, "Could not upload image", Toast.LENGTH_LONG).show();
            }
        }
        else if( requestCode == 1099 ) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // upload
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteData = baos.toByteArray();
            String fileName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
            UploadTask uploadTask = collectionsRefS.child( fileName ).putBytes(byteData);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> res = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    res.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String collection_id = UUID.randomUUID().toString();
                            CollectionItem collectionItem = new CollectionItem( collection_id, uri.toString(), userId );
                            collectionsRefD.child( collection_id ).setValue( collectionItem );
                            collectionsByUserRef.child(collection_id).setValue(1);
                            Toast.makeText(Collections.this, "Collection added successfully", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Collections.this, "Some issue occurred", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("abc", "HW4");
        if (requestCode == 99)
        {
            Log.d("abc", "HW7");
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {Log.d("abc", "HW9");
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1099);
            }
        }
    }

}