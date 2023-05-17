package com.example.kvbalu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kvbalu.API.UserAPI;
import com.example.kvbalu.Model.UserModel;
import com.example.kvbalu.R;
import com.example.kvbalu.Retrofit.RetrofitClient;
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

    EditText et_host;
    Button btn_changeHost;

    UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            Toast.makeText(this, "Xin chào - " + SharedPrefManager.getInstance(this).getUser().getName(), Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        setContentView(R.layout.activity_login);
        changeHOST();
        et_host.setText(RetrofitClient.HOST);
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

    private void changeHOST() {
        et_host = findViewById(R.id.et_host);
        btn_changeHost = findViewById(R.id.btn_changeHost);
        btn_changeHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    RetrofitClient.HOST = et_host.getText().toString();
                    Toast.makeText(LoginActivity.this, "SUCCESS - Máy chủ: " + et_host.getText().toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}