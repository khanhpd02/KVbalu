package com.example.kvbalu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvbalu.Adapter.OrderItemAdapter;
import com.example.kvbalu.Model.OrderItemModel;
import com.example.kvbalu.Model.OrderModel;
import com.example.kvbalu.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    RecyclerView rvOrderItemList;
    TextView tv_orderID, tv_Soluong, tv_TongDonHang, tv_SDT, tv_DiaChi, tv_NgayTao;
    List<OrderItemModel> orderItemList;
    ConstraintLayout clHome, clOrder, clProfile, clCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_order_detail);


        try {
            bindViews();
            loadData();
            Toast.makeText(OrderDetailActivity.this, "load Data OK", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(OrderDetailActivity.this, "load Data Fail", Toast.LENGTH_SHORT).show();
        }
    }

    private void bindViews() {
        tv_orderID = findViewById(R.id.tv_oderID_orderDetail);
        tv_Soluong = findViewById(R.id.tv_SoLuongSanPham);
        tv_TongDonHang = findViewById(R.id.tv_TongDonHang);
        tv_SDT = findViewById(R.id.tv_SoDienThoai);
        tv_DiaChi = findViewById(R.id.tv_DiaChi);
        tv_NgayTao = findViewById(R.id.tv_NgayTao);
        rvOrderItemList = findViewById(R.id.rv_OrderItemList_orderDetail);

        clHome = findViewById(R.id.clHomeAppBar);
        clOrder = findViewById(R.id.clOrderAppBar);
        clCart = findViewById(R.id.clCartAppBar);
        clProfile = findViewById(R.id.clProfileAppBar);

        clHome.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        clOrder.setOnClickListener(v -> startActivity(new Intent(this, OrdersActivity.class)));
        clCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        clProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
    }

    private void loadData() {
        OrderModel order = (OrderModel) getIntent().getSerializableExtra("order");
        orderItemList = order.getOrderItems();
        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(orderItemList, this);
        LinearLayoutManager layout = new LinearLayoutManager(OrderDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        rvOrderItemList.setLayoutManager(layout);
        rvOrderItemList.setNestedScrollingEnabled(false);
        rvOrderItemList.setAdapter(orderItemAdapter);

        System.out.println(String.valueOf(order.getId()));
        System.out.println(order.getOrderItems().size());
        System.out.println(order.getPhoneNumber());
        System.out.println(order.getAddress());


        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        tv_orderID.setText("OrderID: #"+String.valueOf(order.getId()));
        tv_Soluong.setText(String.valueOf(order.getOrderItems().size()));
        tv_TongDonHang.setText(String.valueOf(order.getTotal())+"đ");
        tv_SDT.setText(order.getPhoneNumber());
        tv_DiaChi.setText(order.getAddress());
        tv_NgayTao.setText(simpleDateFormat.format(order.getCreateAt()));



//        String pattern = "dd-MM-yyyy";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//        tv_orderID.setText(String.valueOf(order.getId()));
//        tv_Soluong.setText(order.getOrderItems().size());
//        tv_SDT.setText(order.getPhoneNumber());
//        tv_DiaChi.setText(order.getAddress());
//        tv_NgayTao.setText(simpleDateFormat.format(order.getCreateAt()));
    }

}