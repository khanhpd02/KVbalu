package com.example.kvbalu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kvbalu.api.CartAPI;
import com.example.kvbalu.activity.ProductDetailActivity;
import com.example.kvbalu.model.CartModel;
import com.example.kvbalu.model.ProductModel;
import com.example.kvbalu.R;
import com.example.kvbalu.common.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<ProductModel> productList;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvProductName;
        TextView tvProductPrice;
        ConstraintLayout layout, clAddProductToCart;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            layout = itemView.findViewById(R.id.clProduct);

            clAddProductToCart = itemView.findViewById(R.id.clAddProductToCart);

        }


    }

    public ProductAdapter(List<ProductModel> list, Context context) {
        this.context = context;
        this.productList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recycle_product, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(productList.get(position).getProductImages().get(0).getImage()).into(holder.ivProduct);
        holder.tvProductName.setText(productList.get(position).getProductName());
        holder.tvProductPrice.setText("" + productList.get(position).getPrice());

        ProductModel product = productList.get(position);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ProductDetailActivity.class);
            intent.putExtra("productId", product.getId());
            holder.itemView.getContext().startActivity(intent);
        });

        holder.clAddProductToCart.setOnClickListener(v -> {
            long userId = SharedPrefManager.getInstance(context).getUser().getId();
            CartAPI.CART_API.addNewCart(productList.get(position).getId(),userId, 1).enqueue(new Callback<CartModel>() {
                @Override
                public void onResponse(@NonNull Call<CartModel> call, @NonNull Response<CartModel> response) {
                    if (response.isSuccessful()){
                        CartModel cartModel = response.body();
                        if (cartModel != null){
                            Log.e("Cart ID - ", String.valueOf(cartModel.getId()));
                            Toast.makeText(context, "Add " +
                                    cartModel.getQuantity() + " of \"" +
                                    cartModel.getProduct().getProductName() +
                                    "\" successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CartModel> call, @NonNull Throwable t) {

                }
            });
        });
    }
    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }


}
