package com.example.photoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
public class ShowImages extends AppCompatActivity {
    private RecyclerView recyclerView;
    ImageAdapter imageAdapter;
    List<upload> mUploads;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        FirebaseRecyclerOptions<upload> options = new FirebaseRecyclerOptions.Builder<upload>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("upload"), upload.class)
                        .build();
        imageAdapter=new ImageAdapter(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(imageAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        imageAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        imageAdapter.stopListening();
    }
}