package com.humayunafzal.makeyourmark;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchProductsAdapter extends RecyclerView.Adapter<SearchProductsAdapter.ViewHolder> {

    Context c;
    List<Product> localData;
    String userId;

    public SearchProductsAdapter(Context c,List<Product> localData, String userId) {
        this.c = c;
        this.localData = localData;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.search_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if( position == getItemCount()-1 ) {
            holder.getLine().setBackgroundColor(Color.parseColor("#FAFAFA"));
        }
        else {
            holder.getLine().setBackgroundColor(Color.parseColor("#c9c9c9"));
        }
        Picasso.get().load( localData.get(position).getImage() ).into( holder.getpImage() );
        holder.getpName().setText( localData.get(position).getName() );
        holder.getpPrice().setText( "Price: " + localData.get(position).getPrice() );
        holder.getpStock().setText( "Stock: " + localData.get(position).getStock() );
        holder.getProd().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c, ProductDetails.class);
                intent.putExtra("product_id", localData.get(position).getProductId() );
                intent.putExtra("user_id", userId );
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return localData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // all the views in call history item layout
        private final RoundedImageView pImage;
        private final TextView pName;
        private final TextView pPrice;
        private final TextView pStock;
        private final LinearLayout prod;
        private final View line;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing views
            pImage = itemView.findViewById(R.id.pImage);
            pName = itemView.findViewById(R.id.pName);
            pPrice = itemView.findViewById(R.id.pPrice);
            pStock = itemView.findViewById(R.id.pStock);
            prod = itemView.findViewById(R.id.prod);
            line = itemView.findViewById(R.id.line);
        }

        public RoundedImageView getpImage() {
            return pImage;
        }

        public TextView getpName() {
            return pName;
        }

        public TextView getpPrice() {
            return pPrice;
        }

        public LinearLayout getProd() { return prod; }

        public TextView getpStock() {
            return pStock;
        }

        public View getLine() {
            return line;
        }
    }
}
