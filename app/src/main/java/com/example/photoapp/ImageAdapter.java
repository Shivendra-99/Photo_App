package com.example.photoapp;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;
public class ImageAdapter extends FirebaseRecyclerAdapter<upload,ImageAdapter.ImageViewHolder> {
    public ImageAdapter(@NonNull FirebaseRecyclerOptions<upload> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull ImageViewHolder holder, int position, @NonNull upload model) {
        holder.textView.setText(model.getName());
        Log.d("from recyler",model.imageUrl);
        Picasso.get().load(model.getImageUrl()).into(holder.imageView);
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.imageloading,parent,false);
        return new ImageViewHolder(view);
    }
    public class ImageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textView;
        public ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.uploadedText);
            imageView=itemView.findViewById(R.id.uploadedImage);
        }
    }
}
