package com.humayunafzal.makeyourmark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BuyerViewOrdersAdapter extends RecyclerView.Adapter<BuyerViewOrdersAdapter.ViewHolder> {

    Context c;
    List<Order> localData;

    public BuyerViewOrdersAdapter(Context c, List<Order> localData) {
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
        holder.getoId().setText( localData.get(position).getOrderId() );
        holder.getpName().setText( localData.get(position).getProductId() );
        holder.getpPrice().setText( localData.get(position).getUnitPrice() );
        holder.getoQuantity().setText( localData.get(position).getQuantity() );
        holder.getoTotal().setText( String.valueOf( Integer.valueOf( localData.get(position).getUnitPrice()) * Integer.valueOf(localData.get(position).getQuantity()) ));
        holder.getoUser().setText( localData.get(position).getStoreId() );
        holder.getoDateTime().setText( localData.get(position).getTime() + " " + localData.get(position).getDate() );
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            oId = itemView.findViewById(R.id.oId);
            pName = itemView.findViewById(R.id.pName);
            pPrice = itemView.findViewById(R.id.pPrice);
            oQuantity = itemView.findViewById(R.id.oQuantity);
            oTotal = itemView.findViewById(R.id.oTotal);
            oUser = itemView.findViewById(R.id.oUser);
            oDateTime = itemView.findViewById(R.id.oDateTime);
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
    }
}
