package com.example.kvbalu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kvbalu.API.CategoryAPI;
import com.example.kvbalu.API.ProductAPI;
import com.example.kvbalu.Adapter.CategoryAdapter;
import com.example.kvbalu.Adapter.ProductAdapter;
import com.example.kvbalu.Interface.CategoryRecycleInterface;
import com.example.kvbalu.Model.CategoryModel;
import com.example.kvbalu.Model.ProductModel;
import com.example.kvbalu.Model.UserModel;
import com.example.kvbalu.R;
import com.example.kvbalu.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CategoryRecycleInterface {
    RecyclerView rvCategory;
    RecyclerView rvPopularProduct, rvLatestProduct;

    ImageView userAvatar;
    EditText searchProductEdit;

    ConstraintLayout clCart, clOrder, clProfile, appBarHomeBtn;

    CategoryAdapter categoryAdapter;
    ProductAdapter productAdapter;
    List<CategoryModel> categoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_main);

        anhXa();

        UserModel user = SharedPrefManager.getInstance(this).getUser();
//        Log.e("User Avatar", user.getAvatar());
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            if (!(user.getAvatar() == null)) {
                //Glide.with(getApplicationContext()).load(user.getAvatar()).into(userAvatar);
            }
        }

        loadCategory();
        loadPopularProduct();
        loadLatestProduct();

        searchProduct();

        backToHome();
        goToCart();

        clOrder.setOnClickListener(v -> {
//            finish();
            startActivity(new Intent(MainActivity.this, OrdersActivity.class));
        });
        clProfile.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });
    }

    public void anhXa() {
        userAvatar = findViewById(R.id.ivUserAvatar);

        rvCategory = findViewById(R.id.rvCategory);
        rvPopularProduct = findViewById(R.id.rvPopularProduct);
        rvLatestProduct = findViewById(R.id.rvLatestProduct);

        appBarHomeBtn = findViewById(R.id.clHomeAppBar);
        clCart = findViewById(R.id.clCartAppBar);
        clOrder = findViewById(R.id.clOrderAppBar);
        clProfile = findViewById(R.id.clProfileAppBar);
        searchProductEdit = findViewById(R.id.searchProductEdit);
    }

    public void loadCategory() {
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvCategory.setLayoutManager(layout);
        CategoryRecycleInterface categoryRecycleInterface = this;

        CategoryAPI.CATEGORY_API.getAllCategory().enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryModel>> call, @NonNull Response<List<CategoryModel>> response) {
                if (response.isSuccessful()) {
                    categoryList = response.body();
                    categoryAdapter = new CategoryAdapter(categoryList, MainActivity.this, categoryRecycleInterface);
                    rvCategory.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryModel>> call, @NonNull Throwable t) {
                Log.e("--loadCategory", "API onFailure");
            }
        });
    }

    public void loadPopularProduct() {

        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager layout = new GridLayoutManager(this, 2);
        rvPopularProduct.setLayoutManager(layout);
        ProductAPI.PRODUCT_API.getTop10BestSelling().enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    List<ProductModel> productModels = response.body();
                    productAdapter = new ProductAdapter(productModels, MainActivity.this);
                    rvPopularProduct.setAdapter(productAdapter);
                } else {
                    Log.e("=====Load Popular", "Null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {

            }
        });
    }

    public void loadLatestProduct() {
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager layout = new GridLayoutManager(this, 2);
        rvLatestProduct.setLayoutManager(layout);
        ProductAPI.PRODUCT_API.getTop10Newest().enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    List<ProductModel> productModels = response.body();
                    productAdapter = new ProductAdapter(productModels, MainActivity.this);
                    rvLatestProduct.setAdapter(productAdapter);
                } else {
                    Log.e("=====Load Latest", "Null");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {

            }
        });
    }

    public void backToHome() {
        appBarHomeBtn.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });
    }

    @Override
    public void onCategoryItemClick(int position) {
        if (!(getApplicationContext().toString().contains("ShowProductActivity"))) {
            Intent intent = new Intent(MainActivity.this, ShowProductActivity.class);
            intent.putExtra("tvUserActionCategoryClick", "Category - " + categoryList.get(position).getName());
            intent.putExtra("categoryId", categoryList.get(position).getId());
            startActivity(intent);
        }
        Toast.makeText(MainActivity.this, "Category - " + categoryList.get(position).getName(), Toast.LENGTH_LONG).show();
    }

    public void searchProduct() {
        searchProductEdit.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press
                if (!TextUtils.isEmpty(String.valueOf(searchProductEdit.getText()))) {
                    Intent intent = new Intent(MainActivity.this, ShowProductActivity.class);
                    intent.putExtra("searchProduct", String.valueOf(searchProductEdit.getText()));
                    startActivity(intent);
                }

                return true;
            }
            return false;
        });
    }

    public void goToCart() {
        clCart.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
    }
}