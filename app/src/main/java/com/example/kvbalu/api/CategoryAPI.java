package com.example.kvbalu.api;

import com.example.kvbalu.model.CategoryModel;
import com.example.kvbalu.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryAPI {
    RetrofitClient RETROFIT_CLIENT = new RetrofitClient();
    CategoryAPI CATEGORY_API = RETROFIT_CLIENT.getRetrofit().create(CategoryAPI.class);

    @GET("/Category")
    Call<List<CategoryModel>> getAllCategory();

}
