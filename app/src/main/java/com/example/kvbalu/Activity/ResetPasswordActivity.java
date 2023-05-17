package com.example.kvbalu.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.kvbalu.API.UserAPI;
import com.example.kvbalu.Model.UserModel;
import com.example.kvbalu.R;
import com.github.gongw.VerifyCodeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    ConstraintLayout clCheckUsername, clCheckCode, clResetPass;
    EditText etUsername, etNewPass, etConfirmPass;
    TextView tvUsernameErr, tvCodeErr, tvGoBack;
    VerifyCodeView etCode;
    ProgressDialog mProgressDialog;
    String code = "";
    UserModel user;
    AppCompatButton appCompatCheckUsername, appCompatCheckCode, appCompatChangePasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask
        setContentView(R.layout.activity_reset_password);

        anhXa();
        mProgressDialog = new ProgressDialog(ResetPasswordActivity.this);
        mProgressDialog.setMessage("Please wait ...");
        mProgressDialog.setCancelable(false);
        tvGoBack.setOnClickListener(v->onBackPressed());
        appCompatCheckUsername.setOnClickListener(v -> checkUser());
        appCompatCheckCode.setOnClickListener(v -> checkCode());
        appCompatChangePasswordBtn.setOnClickListener(v -> resetPassword());

    }

    void anhXa() {
        clCheckUsername = findViewById(R.id.clCheckUsername);
        clCheckCode = findViewById(R.id.clCheckCode);
        clResetPass = findViewById(R.id.clResetPass);

        tvUsernameErr = findViewById(R.id.tvError);
        tvCodeErr = findViewById(R.id.tvCodeError);
        tvGoBack = findViewById(R.id.tvGoBack);
        etUsername = findViewById(R.id.etUsername);
        etCode = findViewById(R.id.etCode);
        etNewPass = findViewById(R.id.etNewPassword);
        etConfirmPass = findViewById(R.id.etConfirmPassword);

        appCompatCheckUsername = findViewById(R.id.appCompatCheckUsernameBtn);
        appCompatCheckCode = findViewById(R.id.appCompatCheckCodeBtn);
        appCompatChangePasswordBtn = findViewById(R.id.appCompatChangePasswordBtn);
    }

    @SuppressLint("SetTextI18n")
    void checkUser() {
        String un = String.valueOf(etUsername.getText());
        if (TextUtils.isEmpty(un)) {
            tvUsernameErr.setText("This field cannot be empty!");
            tvUsernameErr.setVisibility(View.VISIBLE);
        } else {
            mProgressDialog.show();
            UserAPI.USER_API.checkExist(un).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()) {
                        user = response.body();
                        if (user != null) {
                            UserAPI.USER_API.getMailCodeFromUsername(un).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.isSuccessful()) {
                                        code = response.body();
                                        if (!TextUtils.isEmpty(code)) {
                                            clCheckUsername.setVisibility(View.GONE);
                                            clCheckCode.setVisibility(View.VISIBLE);
                                        } else {
                                            tvUsernameErr.setVisibility(View.GONE);
                                        }
                                        mProgressDialog.dismiss();

                                    } else {
                                        mProgressDialog.dismiss();
                                        tvUsernameErr.setText("Username is not exist!");
                                        tvUsernameErr.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    mProgressDialog.dismiss();
                                    tvUsernameErr.setText("Something went wrong!");
                                    tvUsernameErr.setVisibility(View.VISIBLE);
                                }
                            });

                        }
                    } else {
                        mProgressDialog.dismiss();
                        tvUsernameErr.setText("Username is not exist!");
                        tvUsernameErr.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    mProgressDialog.dismiss();
                }
            });
        }
    }

    void checkCode() {
        String c = etCode.getVcText();
        if (TextUtils.isEmpty(c)) {
            tvCodeErr.setText("This field cannot be empty!");
            tvCodeErr.setVisibility(View.VISIBLE);
        } else if (c.length() != etCode.getVcTextCount()) {
            tvCodeErr.setText("This code is not valid!");
            tvCodeErr.setVisibility(View.VISIBLE);
        } else if (!c.equals(code)) {
            tvCodeErr.setText("This code is not correct!");
            tvCodeErr.setVisibility(View.VISIBLE);
        } else {
            tvCodeErr.setVisibility(View.GONE);
            clResetPass.setVisibility(View.VISIBLE);
            clCheckCode.setVisibility(View.GONE);
        }
    }

    void resetPassword() {
        String np = String.valueOf(etNewPass.getText());
        String cfp = String.valueOf(etConfirmPass.getText());

        if (!TextUtils.isEmpty(np) && !TextUtils.isEmpty(cfp)) {
            if (!np.equals(cfp)) {
                etConfirmPass.setText(null);
                etConfirmPass.setError("Confirm Password not match!!!");
                etConfirmPass.requestFocus();
            } else {
                long id = user.getId();
                UserAPI.USER_API.resetPassword(id, np).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                            if (response.code() == 200) {
                                user.setPassword(np);
                            }
                            finish();
                            startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                    }
                });
            }
        } else {
            String err = "This field cannot be blank!!!";
            if (TextUtils.isEmpty(cfp)) {
                etConfirmPass.requestFocus();
                etConfirmPass.setError(err);
            }
            if (TextUtils.isEmpty(np)) {
                etNewPass.requestFocus();
                etNewPass.setError(err);
            }
        }
    }

}