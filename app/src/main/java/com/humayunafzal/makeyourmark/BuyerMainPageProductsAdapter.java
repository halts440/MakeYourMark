package com.humayunafzal.makeyourmark;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BuyerMainPageProductsAdapter extends RecyclerView.Adapter<BuyerMainPageProductsAdapter.ViewHolder> {

    Context c;
    List<Product> localData;
    String userId;

    public BuyerMainPageProductsAdapter(Context c,List<Product> localData, String userId) {
        this.c = c;
        this.localData = localData;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.buyer_main_page_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load( localData.get(position).getImage() ).into( holder.getpImage() );
        holder.getpName().setText( localData.get(position).getName() );
        holder.getpPrice().setText( "Price: " + localData.get(position).getPrice() );
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
        private final ImageView pImage;
        private final TextView pName;
        private final TextView pPrice;
        private final LinearLayout prod;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing views
            pImage = itemView.findViewById(R.id.pImage);
            pName = itemView.findViewById(R.id.pName);
            pPrice = itemView.findViewById(R.id.pPrice);
            prod = itemView.findViewById(R.id.prod);
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

        public LinearLayout getProd() { return prod; }
    }
}
