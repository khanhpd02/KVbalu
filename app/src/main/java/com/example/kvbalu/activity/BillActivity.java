package com.example.kvbalu.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.kvbalu.R;

public class BillActivity extends AppCompatActivity {
    TextView tvOrderId;
    AppCompatButton shoppingBtn, ordersBtn;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_bill);

        anhXa();

        tvOrderId.setText("#" + getIntent().getSerializableExtra("orderId"));
        shoppingBtn.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(BillActivity.this, MainActivity.class));
        });
        ordersBtn.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(BillActivity.this, OrdersActivity.class));
        });
//        shoppingBtn.setOnClickListener(v ->{finish(); startActivity(new Intent(BillActivity.this, MainActivity.class));});
    }

    void anhXa() {
        tvOrderId = findViewById(R.id.tvOrderId);
        shoppingBtn = findViewById(R.id.shoppingBtn);
        ordersBtn = findViewById(R.id.ordersBtn);
    }
}