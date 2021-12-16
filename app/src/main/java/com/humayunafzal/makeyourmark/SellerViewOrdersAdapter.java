package com.humayunafzal.makeyourmark;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SellerViewOrdersAdapter extends RecyclerView.Adapter<SellerViewOrdersAdapter.ViewHolder> {

    Context c;
    List<Order> localData;

    public SellerViewOrdersAdapter(Context c, List<Order> localData) {
        this.c = c;
        this.localData = localData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.view_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if( position%2 == 0 ) {
            holder.getoItem().setBackgroundColor(Color.parseColor("#b0cdd6") );
        }
        else {
            holder.getoItem().setBackgroundColor(Color.parseColor("#FAFAFA") );
        }
        holder.getoId().setText( "Order ID: " + localData.get(position).getOrderId() );
        holder.getpName().setText( "Product ID: " + localData.get(position).getProductId() );
        holder.getpPrice().setText( "Price: " + localData.get(position).getUnitPrice() );
        holder.getoQuantity().setText( "Quantity: " + localData.get(position).getQuantity() );
        holder.getoTotal().setText( "Total: " + String.valueOf( Integer.valueOf( localData.get(position).getUnitPrice()) * Integer.valueOf(localData.get(position).getQuantity()) ));
        holder.getoUser().setText( "Buyer: " + localData.get(position).getBuyer() );
        holder.getoDateTime().setText( "Date Time: " + localData.get(position).getTime() + " " + localData.get(position).getDate() );
    }

    @Override
    public int getItemCount() {
        return localData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView oId;
        private final TextView pName;
        private final TextView pPrice;
        private final TextView oQuantity;
        private final TextView oTotal;
        private final TextView oUser;
        private final TextView oDateTime;
        private final LinearLayout oItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            oId = itemView.findViewById(R.id.oId);
            pName = itemView.findViewById(R.id.pName);
            pPrice = itemView.findViewById(R.id.pPrice);
            oQuantity = itemView.findViewById(R.id.oQuantity);
            oTotal = itemView.findViewById(R.id.oTotal);
            oUser = itemView.findViewById(R.id.oUser);
            oDateTime = itemView.findViewById(R.id.oDateTime);
            oItem = itemView.findViewById(R.id.order_item);
        }

        public TextView getoId() {
            return oId;
        }

        public TextView getpName() {
            return pName;
        }

        public TextView getpPrice() {
            return pPrice;
        }

        public TextView getoQuantity() {
            return oQuantity;
        }

        public TextView getoTotal() {
            return oTotal;
        }

        public TextView getoUser() {
            return oUser;
        }

        public TextView getoDateTime() {
            return oDateTime;
        }

        public LinearLayout getoItem() {
            return oItem;
        }
    }
}
