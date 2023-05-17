package com.example.kvbalu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.example.kvbalu.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    TextView toLoginPageBtn;
    Button signUp;
    EditText username, email, phone, pass, re_pass;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_signup);

        anhXa();
        toLoginPageBtn.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
//                finish();
        });

        signUp.setOnClickListener(v -> signUP());
    }
    public void anhXa(){
        toLoginPageBtn = findViewById(R.id.btn_loginHere);
        signUp = findViewById(R.id.btn_signup);

        username = findViewById(R.id.et_username_signup);
        email = findViewById(R.id.et_email_signup);
        phone = findViewById(R.id.et_phonenumber_signup);
        pass = findViewById(R.id.et_password_signup);
        re_pass = findViewById(R.id.et_passwordRepeat_signup);
    }

    public void signUP(){
        String un = String.valueOf(username.getText());
        String em = String.valueOf(email.getText());
        String ph = String.valueOf(phone.getText());
        String p = String.valueOf(pass.getText());
        String rp = String.valueOf(re_pass.getText());

        if(TextUtils.isEmpty(un)){
            username.setError("Please enter your username");
            username.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(em)){
            email.setError("Please enter your email");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(em).matches()){
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(ph)){
            phone.setError("Please enter your Phone Number");
            phone.requestFocus();
            return;
        }
        if(ph.length() != 10){
            phone.setError("Please enter a valid Phone Number");
            phone.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(p)){
            pass.setError("Please enter your password");
            pass.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(rp)){
            re_pass.setError("Please enter your re-password");
            re_pass.requestFocus();
            return;
        }
        if(!(p.trim().equals(rp.trim()))){
            re_pass.setError("Password confirm incorrect");
            re_pass.requestFocus();
            return;
        }

        UserAPI.USER_API.checkExist(un).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                if(!response.isSuccessful()){
                    UserAPI.USER_API.signup(un,em,ph,p).enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                            if(response.isSuccessful()){
                                userModel = response.body();
                                Log.e("=====", "Sign Up Success");
                                Toast.makeText(SignupActivity.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(userModel);

                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                finish();

                            }else{
                                Log.e("=====", "Sign Up Fail");
                                Toast.makeText(SignupActivity.this, "Sign Up Fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                            Log.e("=====", "Call Api Error");
                        }
                    });
                }else{
                    Log.e("=====", "Sign Up Fail - Username Exist");
                    Toast.makeText(SignupActivity.this, "Sign Up Fail - Username Exist", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {

            }
        });

    }
}