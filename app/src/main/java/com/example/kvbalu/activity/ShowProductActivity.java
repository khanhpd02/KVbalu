package com.example.kvbalu.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvbalu.api.CategoryAPI;
import com.example.kvbalu.api.ProductAPI;
import com.example.kvbalu.adapter.CategoryAdapter;
import com.example.kvbalu.adapter.ProductAdapter;
import com.example.kvbalu.Interface.CategoryRecycleInterface;
import com.example.kvbalu.model.CategoryModel;
import com.example.kvbalu.model.ProductModel;
import com.example.kvbalu.model.UserModel;
import com.example.kvbalu.R;
import com.example.kvbalu.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowProductActivity extends AppCompatActivity implements CategoryRecycleInterface {
    RecyclerView rvCategory, rvShowProduct;

    ImageView userAvatar, ivNoProduct;

    CategoryAdapter categoryAdapter;
    ProductAdapter productAdapter;
    TextView tvUserAction, tvShowAllProduct;
    ConstraintLayout clCart, clOrder, clProfile, clHome;
    EditText searchProductEdit;

    List<CategoryModel> categoryList;
    List<ProductModel> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask

        setContentView(R.layout.activity_show_product);
        anhXa();

        UserModel user = SharedPrefManager.getInstance(this).getUser();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            if (!(user.getAvatar() == null)) {
                //Glide.with(getApplicationContext()).load(user.getAvatar()).into(userAvatar);
            }
        }
        loadCategory();
        showAllProduct();
        showProduct();

        reloadHome();
        goToCart();
        toProfile();
        toOrder();
        searchProduct();
    }

    public void anhXa() {
        userAvatar = findViewById(R.id.ivUserAvatar);
        ivNoProduct = findViewById(R.id.ivNoProduct);

        rvCategory = findViewById(R.id.rvCategory);
        rvShowProduct = findViewById(R.id.rvShowProduct);

        clHome = findViewById(R.id.clHomeAppBar);

        tvUserAction = findViewById(R.id.tvUserAction);
        tvShowAllProduct = findViewById(R.id.tvShowAllProduct);

        searchProductEdit = findViewById(R.id.searchProductEdit);
        clCart = findViewById(R.id.clCartAppBar);
        clOrder = findViewById(R.id.clOrderAppBar);
        clProfile = findViewById(R.id.clProfileAppBar);

        LinearLayoutManager layout = new GridLayoutManager(this, 2);
//        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvShowProduct.setLayoutManager(layout);
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
                    categoryAdapter = new CategoryAdapter(categoryList, ShowProductActivity.this, categoryRecycleInterface);
                    rvCategory.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryModel>> call, @NonNull Throwable t) {
                Log.e("=====", "Call Api Fail");
            }
        });
    }

    public void showAllProduct() {
        tvShowAllProduct.setOnClickListener(v -> ProductAPI.PRODUCT_API.getAllProduct().enqueue(new Callback<List<ProductModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    productList = response.body();
                    productAdapter = new ProductAdapter(productList, ShowProductActivity.this);
                    rvShowProduct.setAdapter(productAdapter);
                    tvUserAction.setText("" + productList.size() + " sản phẩm");

                    ivNoProduct.setVisibility(View.GONE);
//                            rvShowProduct.setBac
                }
                if (productList == null) {
                    ivNoProduct.setVisibility(View.VISIBLE);
//                    ivNoProduct.setBackground(getDrawable(R.drawable.no_product_found));
                    Log.e("=====Product", "Empty");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {

            }
        }));
    }

    public void reloadHome() {
        clHome.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });
    }

    @SuppressLint("SetTextI18n")
    public void showProduct() {
        if (getIntent().getSerializableExtra("categoryId") != null) {
            long categoryId = (long) getIntent().getSerializableExtra("categoryId");
            loadCategoryProduct(categoryId);
            tvUserAction.setText(getIntent().getSerializableExtra("tvUserActionCategoryClick").toString());
            getIntent().removeExtra("tvUserActionCategoryClick");
            getIntent().removeExtra("categoryId");
        } else {
            tvUserAction.setText("Search for \"" + getIntent().getSerializableExtra("searchProduct") + "\"");
            searchProductEdit.setText((CharSequence) getIntent().getSerializableExtra("searchProduct"));
            String name = String.valueOf(searchProductEdit.getText());
            searchProductByName(name);
        }

    }

    public void loadCategoryProduct(long id) {
        ProductAPI.PRODUCT_API.getProductByCategoryId(id).enqueue(new Callback<List<ProductModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
//                if (response.isSuccessful()) {
                productList = response.body();
                productAdapter = new ProductAdapter(productList, ShowProductActivity.this);
                tvUserAction.setText(tvUserAction.getText().toString() + " - " + (productList != null ? productList.size() : 0) + " sản phẩm");
                rvShowProduct.setAdapter(productAdapter);
//                    ivNoProduct.setBackground(null);
                ivNoProduct.setVisibility(View.GONE);
                if (productList == null) {
                    ivNoProduct.setVisibility(View.VISIBLE);
//                    ivNoProduct.setBackground(getDrawable(R.drawable.no_product_found));
                    Log.e("=====Product", "Empty");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCategoryItemClick(int position) {
        tvUserAction.setText("Danh mục - " + categoryList.get(position).getName());
        loadCategoryProduct(categoryList.get(position).getId());
    }

    @SuppressLint("SetTextI18n")
    public void searchProduct() {
        searchProductEdit.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press
                if (!TextUtils.isEmpty(String.valueOf(searchProductEdit.getText()))) {
                    String name = String.valueOf(searchProductEdit.getText());
                    tvUserAction.setText("Từ khóa \"" + name + "\"");
                    searchProductByName(name);
                    closeKeyboard();
                }
                return true;
            }
            return false;
        });
    }

    public void searchProductByName(String name) {
        ProductAPI.PRODUCT_API.getProductByName(name).enqueue(new Callback<List<ProductModel>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
//                if(response.isSuccessful()){
                productList = response.body();
                productAdapter = new ProductAdapter(productList, ShowProductActivity.this);
                tvUserAction.setText(tvUserAction.getText() + " - " + (productList != null ? productList.size() : 0) + "sản phẩm");
                rvShowProduct.setAdapter(productAdapter);

//                    ivNoProduct.setBackground(null);
                ivNoProduct.setVisibility(View.GONE);
                if (productList == null) {
                    ivNoProduct.setVisibility(View.VISIBLE);

//                    ivNoProduct.setBackground(getDrawable(R.drawable.no_product_found));
                    Log.e("=====Product", "Empty");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {

            }
        });
    }

    public void goToCart() {
        clCart.setOnClickListener(v -> startActivity(new Intent(ShowProductActivity.this, CartActivity.class)));
    }
    void toOrder(){
        clOrder.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(ShowProductActivity.this, OrdersActivity.class));
        });
    }
    void toProfile(){
        clProfile.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(ShowProductActivity.this, ProfileActivity.class));
        });
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}