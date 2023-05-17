package com.example.kvbalu.API;

import com.example.kvbalu.Retrofit.RetrofitClient;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileAPI {
    RetrofitClient RETROFIT_CLIENT = new RetrofitClient();
    FileAPI FILE_API = RETROFIT_CLIENT.getRetrofit().create(FileAPI.class);


    @Multipart
    @POST("/files/cloud/upload")
    Call<String> uploadFile(@Part MultipartBody.Part file);
}
