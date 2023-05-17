package com.example.kvbalu.API;

import com.example.kvbalu.Model.CartModel;
import com.example.kvbalu.Retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CartAPI {
    RetrofitClient RETROFIT_CLIENT = new RetrofitClient();
    CartAPI CART_API = RETROFIT_CLIENT.getRetrofit().create(CartAPI.class);

    @FormUrlEncoded
    @POST("/Cart")
    Call<CartModel> addNewCart(@Field("productId")long productId,
                               @Field("userId")long userId,
                               @Field("quantity")int quantity);
    
    @GET("/Cart/user")
    Call<List<CartModel>> getCartByUserId(@Query("userId") long id);

    @FormUrlEncoded
    @PUT("/Cart/update")
    Call<CartModel> updateCartQuantity(@Field("cartId") Long cartId, @Field("quantity") int quantity);

    @DELETE("/Cart")
    Call<String> deleteCart(@Query("cartId") long cartId);


}
