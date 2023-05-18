package com.example.kvbalu.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvbalu.API.CartAPI;
import com.example.kvbalu.API.OrderAPI;
import com.example.kvbalu.Adapter.CartSelectPaymentAdapter;
import com.example.kvbalu.Model.CartModel;
import com.example.kvbalu.Model.OrderModel;
import com.example.kvbalu.Object.DeliveryDetail;
import com.example.kvbalu.R;
import com.example.kvbalu.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectPaymentActivity extends AppCompatActivity {
    ConstraintLayout clBackBtn;
    ProgressBar pb;
    DeliveryDetail deliveryDetail = new DeliveryDetail();
    RecyclerView rvCartPayment;
    TextView tvReceiverName, tvReceiverPhone, tvReceiverAddress, tvChangeDeliveryDetail, tvTotalCartItem, tv_totalPrice;
    Button checkoutBtn;
    CartSelectPaymentAdapter cartSelectPaymentAdapter;

    List<CartModel> cartList = new ArrayList<>();
    int totalCartPrice = 0;
    String u = "", p = "", a = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_select_payment);

        anhXa();

        loadDeliveryDetail();
        loadCart();
        checkout();
    }

    void anhXa() {
        tvReceiverName = findViewById(R.id.tvNameRecieve);
        tvReceiverPhone = findViewById(R.id.tvPhoneNumber);
        tvReceiverAddress = findViewById(R.id.tvDeliveryAddress);
        tvChangeDeliveryDetail = findViewById(R.id.tvChangeDeliveryDetail);
        tvTotalCartItem = findViewById(R.id.tvTotalCartItem);
        clBackBtn = findViewById(R.id.clbackBtn);

        clBackBtn.setOnClickListener(v -> onBackPressed());
        tvChangeDeliveryDetail.setOnClickListener(v -> onBackPressed());

        rvCartPayment = findViewById(R.id.rvCartPayment);

        checkoutBtn = findViewById(R.id.checkoutBtn);
        tv_totalPrice = findViewById(R.id.tv_totalPrice);
        pb = findViewById(R.id.pb_selectPayment);
    }

    void loadDeliveryDetail() {
        deliveryDetail = SharedPrefManager.getInstance(SelectPaymentActivity.this)
                .getDeliveryDetail()
                .get((int) getIntent().getSerializableExtra("deliveryDetailPos"));

        Toast.makeText(this, deliveryDetail.getReceiverName(), Toast.LENGTH_SHORT).show();

        u = String.valueOf(deliveryDetail.getReceiverName());
        p = String.valueOf(deliveryDetail.getReceiverPhone());
        a = String.valueOf(deliveryDetail.getReceiverAddress());

        tvReceiverName.setText(u);
        tvReceiverPhone.setText(p);
        tvReceiverAddress.setText(a);

    }

    void loadCart() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCartPayment.setLayoutManager(manager);
        CartAPI.CART_API.getCartByUserId(SharedPrefManager.getInstance(this).getUser().getId()).enqueue(new Callback<List<CartModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<List<CartModel>> call, @NonNull Response<List<CartModel>> response) {
                if (response.isSuccessful()) {
                    cartList = response.body();
                    cartSelectPaymentAdapter = new CartSelectPaymentAdapter(cartList, SelectPaymentActivity.this);
                    rvCartPayment.setAdapter(cartSelectPaymentAdapter);

                    if (cartList != null) {
                        for (CartModel c : cartList) {
                            totalCartPrice += c.getQuantity() * c.getProduct().getPrice();
                        }
                        tv_totalPrice.setText("Tổng: " + totalCartPrice + "đ");
                        tvTotalCartItem.setText(String.valueOf(cartList.size()));
//                        tvTotalCartPrice.setText(String.valueOf(totalCartPrice));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CartModel>> call, @NonNull Throwable t) {
                Log.e("Cart API Error", "Call cart API fail");
            }
        });
    }

    void checkout() {
        checkoutBtn.setOnClickListener(v -> {
            setProgressing(true);

            long id = SharedPrefManager.getInstance(this).getUser().getId();
            OrderAPI.ORDER_API.addCartToOrder(id, a, p).enqueue(new Callback<OrderModel>() {
                @Override
                public void onResponse(@NonNull Call<OrderModel> call, @NonNull Response<OrderModel> response) {
                    if (response.isSuccessful()) {
                        OrderModel orderModel = response.body();
                        if (orderModel != null) {
                            Toast.makeText(SelectPaymentActivity.this, "Đặt hàng thành công! (ID:" + orderModel.getId()+")", Toast.LENGTH_SHORT).show();
                            setProgressing(false);
                            finish();
                            startActivity(new Intent(SelectPaymentActivity.this, BillActivity.class).putExtra("orderId", orderModel.getId()));

                        }
                    } else {
                        Toast.makeText(SelectPaymentActivity.this, "Không thành công !!!", Toast.LENGTH_SHORT).show();
                        setProgressing(false);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<OrderModel> call, @NonNull Throwable t) {
                    Log.e("Call order API", t.getMessage());
                    Toast.makeText(SelectPaymentActivity.this, "Lỗi kết nối với server!!!", Toast.LENGTH_SHORT).show();
                    setProgressing(false);
                }
            });
        });
    }

    void setProgressing(boolean isTrue) {
        if (isTrue) {
            pb.setVisibility(View.VISIBLE);
            checkoutBtn.setVisibility(View.GONE);
        } else {
            pb.setVisibility(View.GONE);
            checkoutBtn.setVisibility(View.VISIBLE);
        }

    }
}