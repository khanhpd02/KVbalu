package com.example.kvbalu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.kvbalu.API.CartAPI;
import com.example.kvbalu.Interface.CartRecycleInterface;
import com.example.kvbalu.Model.CartModel;
import com.example.kvbalu.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final CartRecycleInterface cartRecycleInterface;

    List<CartModel> cartList;
    Context context;

    public CartAdapter(CartRecycleInterface cartRecycleInterface, List<CartModel> cartList, Context context) {
        this.cartRecycleInterface = cartRecycleInterface;
        this.cartList = cartList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImg;
        TextView tvProductName, tvCartProductPrice, tvCartQuantity, tvCartPrice;
        ConstraintLayout clDecrease, clIncrease, clDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImg = itemView.findViewById(R.id.ivProductImgCart);

            tvProductName = itemView.findViewById(R.id.tvProductNameCart);
            tvCartPrice = itemView.findViewById(R.id.tvCartPrice);
            tvCartQuantity = itemView.findViewById(R.id.tvProductCartQuantity);
            tvCartProductPrice = itemView.findViewById(R.id.tvCartProductPrice);

            clDecrease = itemView.findViewById(R.id.clDecreaseCartQuantity);
            clIncrease = itemView.findViewById(R.id.clIncreaseCartQuantity);
            clDelete = itemView.findViewById(R.id.clDeleteCart);

            clDecrease.setOnClickListener(v -> decreaseCart());

            clIncrease.setOnClickListener(v -> increaseCart());

            clDelete.setOnClickListener(v -> deleteCart());
        }

        public void decreaseCart() {
            int cartNumber = cartList.get(getAdapterPosition()).getQuantity();
            if (cartNumber > 1) {
                cartNumber--;

                cartList.get(getAdapterPosition()).setQuantity(cartNumber);
                updateCartAPI(cartList.get(getAdapterPosition()).getId(), cartNumber);

                cartRecycleInterface.updateCartTotalPrice((-1) * cartList.get(getAdapterPosition()).getProduct().getPrice());
            }

        }

        public void increaseCart() {
            int cartNumber = cartList.get(getAdapterPosition()).getQuantity();
            if (cartNumber <= cartList.get(getAdapterPosition()).getProduct().getQuantity()) {
                cartNumber++;

                cartList.get(getAdapterPosition()).setQuantity(cartNumber);
                updateCartAPI(cartList.get(getAdapterPosition()).getId(), cartNumber);

                cartRecycleInterface.updateCartTotalPrice(cartList.get(getAdapterPosition()).getProduct().getPrice());
            }

        }

        public void deleteCart(){
            CartModel c = cartList.get(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            cartList.remove(c);

            deleteCartAPI(c.getId());

            cartRecycleInterface.updateCartTotalPrice(
                            (-1)
                            * c.getQuantity()
                            * c.getProduct().getPrice());

        }
        public void updateCartAPI(long cartId, int cartNumber) {
            CartAPI.CART_API.updateCartQuantity(cartId, cartNumber).enqueue(new Callback<CartModel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<CartModel> call, @NonNull Response<CartModel> response) {
                    tvCartQuantity.setText(cartNumber + "");
                    tvCartPrice.setText((cartNumber * cartList.get(getAdapterPosition()).getProduct().getPrice()) + "");
//                    notifyItemChanged(getAdapterPosition());
                }

                @Override
                public void onFailure(@NonNull Call<CartModel> call, @NonNull Throwable t) {
                    Log.e("CAll cart api", "Fail");
                }
            });
        }

        public void deleteCartAPI(long cartId){
            CartAPI.CART_API.deleteCart(cartId).enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
//                    if(response.isSuccessful()){
                        Toast.makeText(context, response.body(), Toast.LENGTH_SHORT).show();
//                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    Log.e("CAll Delete Cart API", t.getMessage());
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recycle_cart_test, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(cartList.get(position).getProduct().getProductImages().get(0).getImage()).into(holder.ivProductImg);
        holder.tvProductName.setText(cartList.get(position).getProduct().getProductName());
//        holder.cartNumber = cartList.get(position).getQuantity();
        holder.tvCartProductPrice.setText(cartList.get(position).getProduct().getPrice() + "");
        holder.tvCartQuantity.setText(cartList.get(position).getQuantity() + "");
        holder.tvCartPrice.setText((cartList.get(position).getQuantity() * cartList.get(position).getProduct().getPrice()) + "");
    }

    @Override
    public int getItemCount() {
        if (cartList != null) {
            return cartList.size();
        }
        return 0;
    }
}
