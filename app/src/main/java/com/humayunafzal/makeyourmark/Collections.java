package com.humayunafzal.makeyourmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

    ImageView uploadImageBtn;
    FirebaseDatabase database;
    DatabaseReference collectionsRefD;
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
        database = FirebaseDatabase.getInstance();
        collectionsRefD = database.getReference("collections");
        storage = FirebaseStorage.getInstance();
        collectionsRefS = storage.getReference("collections");
        userId = getIntent().getStringExtra("user_id");
        rv_Collections = findViewById(R.id.rv_Collections);
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

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
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
    }
}