package com.example.kvbalu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.kvbalu.R;
import com.example.kvbalu.SharedPrefManager;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask

        setContentView(R.layout.activity_intro);
//         Đặt delay trong 2 giây
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Kết thúc Activity hiện tại
                finish();
                // Tạo Intent để chuyển sang Activity mới
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 2000); // 2000 milliseconds = 2 giây

//        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
//            finish();
//            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
//        }
    }
}