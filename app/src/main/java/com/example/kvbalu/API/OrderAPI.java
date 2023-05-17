package com.example.kvbalu.API;

import com.example.kvbalu.Model.OrderModel;
import com.example.kvbalu.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OrderAPI {
    RetrofitClient RETROFIT_CLIENT = new RetrofitClient();

    OrderAPI ORDER_API = RETROFIT_CLIENT.getRetrofit().create(OrderAPI.class);

    @FormUrlEncoded
    @POST("/Order/add-all")
    Call<OrderModel> addCartToOrder(@Field("userId") long id,
                                    @Field("address") String address,
                                    @Field("phoneNumber") String phone);

    @GET("/Order/id")
    Call<List<OrderModel>> getAllOrderByUserId(@Query("userId") Long id);
}
