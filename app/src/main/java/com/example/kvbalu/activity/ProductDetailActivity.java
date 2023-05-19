package com.example.kvbalu.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.kvbalu.api.CartAPI;
import com.example.kvbalu.api.ProductAPI;
import com.example.kvbalu.adapter.ProductImageAdapter;
import com.example.kvbalu.model.CartModel;
import com.example.kvbalu.model.ProductImageModel;
import com.example.kvbalu.model.ProductModel;
import com.example.kvbalu.R;
import com.example.kvbalu.common.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    ViewPager vpProductImage;
    CircleIndicator ciProductImage;
    ProductImageAdapter productImageAdapter;
    TextView  tvMoTaSanPham,tvProductName, tvProductPrice, tvProductCartNumber; //tvProductSold, tvProductQuantity
    ConstraintLayout clBackBtn, clDecrease, clIncrease;
    AppCompatButton addToCartBtn;
    int productNumCart = 1;
    long productId;
    ProductModel product;
    List<ProductImageModel> productImageList = new ArrayList<>();
    Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_product_detail);


        anhXa();

        clBackBtn.setOnClickListener(v -> onBackPressed());

        productId = (long) getIntent().getSerializableExtra("productId");
        Log.e("ProductId", String.valueOf(productId));
        getProduct(productId);
        getIntent().removeExtra("productId");


        decreaseClick();
        increaseClick();
    }

    public void anhXa() {
        vpProductImage = findViewById(R.id.vpProductImage);
        ciProductImage = findViewById(R.id.ciProductImage);

        clBackBtn = findViewById(R.id.clbackBtn);

        tvProductName = findViewById(R.id.tvProductNameDetail);
        tvProductPrice = findViewById(R.id.tvProductPriceDetail);
        /*tvProductSold = findViewById(R.id.tvProductSold);
        tvProductQuantity = findViewById(R.id.tvProductQuantity);*/
        tvMoTaSanPham= findViewById(R.id.textView9);
        tvProductCartNumber = findViewById(R.id.tvProductNumberAppBar);
        clDecrease = findViewById(R.id.clDecreaseProductAppBar);
        clIncrease = findViewById(R.id.clIncreaseProductAppBar);

        addToCartBtn = findViewById(R.id.addToCartBtn);


    }

    public void getProduct(long productId) {
        ProductAPI.PRODUCT_API.getProductById(productId).enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(@NonNull Call<ProductModel> call, @NonNull Response<ProductModel> response) {
                if (response.isSuccessful()) {
                    product = response.body();
                    if (product != null) {
                        tvProductName.setText(product.getProductName());
                        tvProductPrice.setText(String.valueOf(product.getPrice()));
                        tvMoTaSanPham.setText(product.getDescription());
                       /* tvProductSold.setText(String.valueOf(product.getSold()));
                        tvProductQuantity.setText(String.valueOf(product.getQuantity()));*/
                        productImageList = product.getProductImages();
                        loadProductImageSlide(product);
                        productNumCart = Integer.parseInt(String.valueOf(tvProductCartNumber.getText()));
                        changeAddToCartBtn(productNumCart);

                    } else {
                        Log.e("Empty Product", "true");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductModel> call, @NonNull Throwable t) {
            }
        });
    }

    public void loadProductImageSlide(ProductModel product) {
//        Log.e("=====", product.getProductName());
        productImageAdapter = new ProductImageAdapter(this, product.getProductImages());
        vpProductImage.setAdapter(productImageAdapter);

        ciProductImage.setViewPager(vpProductImage);
        productImageAdapter.registerDataSetObserver(ciProductImage.getDataSetObserver());

        autoSlideProductImage();

    }

    public void autoSlideProductImage() {
        if (productImageList == null || productImageList.isEmpty() || vpProductImage == null) {
            return;
        }
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    int currentImg = vpProductImage.getCurrentItem();
//                        Log.e("Current Img", String.valueOf(currentImg));
                    int totalImg = productImageList.size() - 1;
                    if (currentImg < totalImg) {
                        currentImg++;
                        vpProductImage.setCurrentItem(currentImg);
                    } else {
                        vpProductImage.setCurrentItem(0);
                    }
//                        Log.e("Current Img", String.valueOf(currentImg));

                });
            }
        }, 500, 3000);
    }

    public void decreaseClick() {
        clDecrease.setOnClickListener(v -> {
            if (productNumCart > 0) {
                productNumCart--;
                tvProductCartNumber.setText(String.valueOf(productNumCart));
            }
            changeAddToCartBtn(productNumCart);
        });
    }

    public void increaseClick() {
        clIncrease.setOnClickListener(v -> {
            if (productNumCart <= product.getQuantity()) {
                productNumCart++;
                tvProductCartNumber.setText(String.valueOf(productNumCart));
            }
            changeAddToCartBtn(productNumCart);
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void changeAddToCartBtn(int productNum) {

        if (productNum <= 0) {
            addToCartBtn.setBackgroundDrawable(getDrawable(R.drawable.black_circle_gray_inner));
            addToCartBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_cart_btn_icon, 0, R.drawable.icons8_white_double_right_100, 0);
            addToCartBtn.setOnClickListener(v -> Toast.makeText(ProductDetailActivity.this, "Bằng 0 sao add được!", Toast.LENGTH_SHORT).show());
            clDecrease.setBackground(getDrawable(R.drawable.subtract_btn));
        } else {
            addToCartBtn.setBackgroundDrawable(getDrawable(R.drawable.add_cart_backround));
            addToCartBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.add_cart_btn_icon, 0, R.drawable.icons8_white_double_right_100, 0);
            addToCartBtn.setOnClickListener(v -> addToCart());
            clDecrease.setBackground(getDrawable(R.drawable.subtract_btn));
        }
    }

    public void addToCart() {
        long userId = SharedPrefManager.getInstance(this).getUser().getId();
        CartAPI.CART_API.addNewCart(product.getId(), userId, productNumCart).enqueue(new Callback<CartModel>() {
            @Override
            public void onResponse(@NonNull Call<CartModel> call, @NonNull Response<CartModel> response) {
                if (response.isSuccessful()) {
                    CartModel cartModel = response.body();
                    if (cartModel != null) {
                        Log.e("Cart ID - ", String.valueOf(cartModel.getId()));
                        Toast.makeText(ProductDetailActivity.this, "Đã thêm " +
                                cartModel.getQuantity() + " sản phẩm \"" +
                                cartModel.getProduct().getProductName() +
                                "\" vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<CartModel> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}