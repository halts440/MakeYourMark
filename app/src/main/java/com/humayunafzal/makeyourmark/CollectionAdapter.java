package com.humayunafzal.makeyourmark;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {

    Context c;
    List<CollectionItem> localData;

    public CollectionAdapter(Context c, List<CollectionItem> localData) {
        this.c = c;
        this.localData = localData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.layout_collection_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load( localData.get(position).getImagePath() ).into( holder.getcImage() );
    }

    @Override
    public int getItemCount() {
        return localData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView cImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cImage = itemView.findViewById(R.id.cImage);
        }

        public ImageView getcImage() {
            return cImage;
        }
    }
}
