package com.example.kvbalu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.kvbalu.Activity.LoginActivity;
import com.example.kvbalu.Model.UserModel;
import com.example.kvbalu.Object.DeliveryDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    public SharedPrefManager(Context context) {
        mCtx = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context){
        if (mInstance == null){
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    private static final String SHARE_PREF_NAME = "userLogin";
    private static final String SHARE_PREF_ADDRESS = "deliveryAddress";

    private static final String KEY_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";

    private static final String KEY_IMAGES = "avatar";



    public void userLogin(UserModel user){
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARE_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_ID, (int) user.getId());
        editor.putString(KEY_USERNAME, user.getUserName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_PHONE, user.getPhoneNumber());
        editor.putString(KEY_IMAGES, user.getAvatar());
        editor.apply();
    }
    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    public UserModel getUser(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME, Context.MODE_PRIVATE);
        return new UserModel(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_NAME,null),
                sharedPreferences.getString(KEY_EMAIL,null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(KEY_IMAGES,null)
        );
    }
    public void userLogout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
    public void deleteDeliveryList(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_ADDRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    public static void set(String key, String value) {
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARE_PREF_ADDRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        set(key, json);
    }



    public List<DeliveryDetail> getDeliveryDetail(){
        List<DeliveryDetail> arrayItems = new ArrayList<>();
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARE_PREF_ADDRESS, Context.MODE_PRIVATE);
        String serializedObject = sharedPreferences.getString("DeliveryList", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<DeliveryDetail>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }

        return arrayItems;
    }
}
