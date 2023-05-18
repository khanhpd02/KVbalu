package com.example.kvbalu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kvbalu.model.OrderItemModel;
import com.example.kvbalu.R;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
    List<OrderItemModel> itemList;
    Context context;

    public OrderItemAdapter(List<OrderItemModel> list, Context context) {
        this.itemList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_recycle_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(itemList.get(position).getProduct().getProductImages().get(0).getImage()).into(holder.ivProductImg);
        holder.tvProductName.setText(itemList.get(position).getProduct().getProductName());
//        holder.OrderItemNumber = itemList.get(position).getQuantity();
        holder.tvOrderItemProductPrice.setText(itemList.get(position).getProduct().getPrice() + "");
        holder.tvOrderItemQuantity.setText(itemList.get(position).getQuantity() + "");
        holder.tvOrderItemPrice.setText((itemList.get(position).getQuantity() * itemList.get(position).getProduct().getPrice()) + "");

    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImg;
        TextView tvProductName, tvOrderItemProductPrice, tvOrderItemQuantity, tvOrderItemPrice;
//        ConstraintLayout clDecrease, clIncrease, clDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImg = itemView.findViewById(R.id.iv_productImage_orderDetail);

            tvProductName = itemView.findViewById(R.id.tvProductNameOrderItem);
            tvOrderItemPrice = itemView.findViewById(R.id.tvOrderItemPrice);
            tvOrderItemQuantity = itemView.findViewById(R.id.tvProductOrderItemQuantity);
            tvOrderItemProductPrice = itemView.findViewById(R.id.tvOrderItemProductPrice);
        }
    }
}
