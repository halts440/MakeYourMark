package com.humayunafzal.makeyourmark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SellerAllProductsAdapter extends RecyclerView.Adapter<SellerAllProductsAdapter.ViewHolder> {

    Context c;
    List<Product> localData;

    public SellerAllProductsAdapter(Context c,List<Product> localData) {
        this.c = c;
        this.localData = localData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.seller_all_products_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load( localData.get(position).getImage() ).into( holder.getpImage() );
        holder.getpName().setText( localData.get(position).getName() );
        holder.getpPrice().setText( "Price: " + localData.get(position).getPrice() );
        holder.getpStock().setText( "Stock: " + localData.get(position).getStock() );
    }

    @Override
    public int getItemCount() {
        return localData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // all the views in call history item layout
        private final ImageView pImage;
        private final TextView pName;
        private final TextView pPrice;
        private final TextView pStock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing views
            pImage = itemView.findViewById(R.id.pImage);
            pName = itemView.findViewById(R.id.pName);
            pPrice = itemView.findViewById(R.id.pPrice);
            pStock = itemView.findViewById(R.id.pStock);
        }

        public ImageView getpImage() {
            return pImage;
        }

        public TextView getpName() {
            return pName;
        }

        public TextView getpPrice() {
            return pPrice;
        }

        public TextView getpStock() {
            return pStock;
        }
    }
}
