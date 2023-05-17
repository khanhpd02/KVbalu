package com.example.kvbalu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.kvbalu.R;
import com.example.kvbalu.SharedPrefManager;

public class IntroActivity extends AppCompatActivity {

    ConstraintLayout start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title not the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask

        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        }

        setContentView(R.layout.activity_intro);


        start = findViewById(R.id.startBtn);
        start.setOnClickListener(v -> {
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            finish();
        });

//        loadActivity();
    }
    void loadActivity(){
        new Thread(() -> {
            int n = 0;
            try{
                do {
                    if (n >= 5000){
                        toLogin();
                        return;
                    }
                    Thread.sleep((long) 100);
                    n += 100;
                }while(true);
            }catch (InterruptedException interruptedException) {
                toLogin();
                return;
            }catch (Throwable throwable){
                toLogin();
                return;
            }
        }).start();
    }
    void toLogin(){
        IntroActivity.this.finish();
        Intent intent = new Intent(IntroActivity.this.getApplicationContext(), (Class) LoginActivity.class);
        IntroActivity.this.startActivity(intent);
    }
}