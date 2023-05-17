package com.example.kvbalu.API;

import com.example.kvbalu.Model.CategoryModel;
import com.example.kvbalu.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryAPI {
    RetrofitClient RETROFIT_CLIENT = new RetrofitClient();
    CategoryAPI CATEGORY_API = RETROFIT_CLIENT.getRetrofit().create(CategoryAPI.class);

    @GET("/Category")
    Call<List<CategoryModel>> getAllCategory();

}
