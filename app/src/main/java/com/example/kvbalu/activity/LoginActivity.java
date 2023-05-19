package com.example.kvbalu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kvbalu.api.UserAPI;
import com.example.kvbalu.model.UserModel;
import com.example.kvbalu.R;
import com.example.kvbalu.Retrofit.RetrofitClient;
import com.example.kvbalu.common.SharedPrefManager;
import com.example.kvbalu.RetrofitClient;
import com.example.kvbalu.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView toSignUpPageBtn, tvForgetPass;
    EditText username;
    EditText password;
    Button loginBtn;
//    UserAPI userAPI;


    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        Toast.makeText(this, RetrofitClient.HOST, Toast.LENGTH_LONG).show();

        setContentView(R.layout.activity_login);
        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        loginBtn = findViewById(R.id.btn_login);
        tvForgetPass = findViewById(R.id.btn_forgetPassword);
        toSignUpPageBtn = findViewById(R.id.btn_signupHere);

        toSignUpPageBtn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
//                finish();
        });
        loginBtn.setOnClickListener(v -> login());
        tvForgetPass.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class)));

//
    }

    public void login() {
        String u = String.valueOf(username.getText());
        String p = String.valueOf(password.getText());
        if (TextUtils.isEmpty(u)) {
            username.setError("Please enter your username");
            username.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(p)) {
            password.setError("Please enter your password");
            password.requestFocus();
            return;
        }
        UserAPI.USER_API.login(u, p).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                if (response.isSuccessful()) {
                    userModel = response.body();
                    if (userModel != null) {
                        Toast.makeText(LoginActivity.this, userModel.getEmail(), Toast.LENGTH_SHORT).show();
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(userModel);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                } else {
                    Log.e("======", "login fail");
                    Toast.makeText(LoginActivity.this, "Wrong username or password !!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                Log.e("======", "call api fail");
            }
        });
    }


}