package com.example.kvbalu.API;

import com.example.kvbalu.Model.UserModel;
import com.example.kvbalu.Retrofit.RetrofitClient;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserAPI {

    RetrofitClient RETROFIT_CLIENT = new RetrofitClient();
    UserAPI USER_API = RETROFIT_CLIENT.getRetrofit().create(UserAPI.class);

    @FormUrlEncoded
    @POST("/user/login")
    Call<UserModel> login(@Field("userName") String username,
                          @Field("password") String password);

    @FormUrlEncoded
    @POST("/user")
    Call<UserModel> signup(@Field("userName") String username,
                           @Field("email") String email,
                           @Field("phoneNumber") String phoneNumber,
                           @Field("password") String password);

    @GET("/user/check/{username}")
    Call<UserModel> checkExist(@Path("username") String username);

    @FormUrlEncoded
    @POST("/user/change-password")
    Call<String> changePassword(@Field("id") long id,
                                @Field("password") String oldPass,
                                @Field("newPass") String newPass);

    @FormUrlEncoded
    @POST("/user/reset-password")
    Call<String> resetPassword(@Field("id") long id,
                               @Field("newPass") String newPass);

    @PATCH("/user/{id}")
    Call<UserModel> updateUser(@Path("id") long id, @Body UserModel user);

    @FormUrlEncoded
    @POST("/user/forget-password")
    Call<String> getMailCodeFromUsername(@Field("username") String username);

}
