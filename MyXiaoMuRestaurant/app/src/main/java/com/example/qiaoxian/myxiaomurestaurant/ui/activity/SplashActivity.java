package com.example.qiaoxian.myxiaomurestaurant.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.qiaoxian.myxiaomurestaurant.R;

public class SplashActivity extends AppCompatActivity {
    private Handler myHandler = new Handler();
    private Button button;
    private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            goToLogin();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initEvent();
        myHandler.postDelayed(runnable1,3000);
    }

    private void initView() {
        button = (Button)findViewById(R.id.splashPage);
    }

    private void initEvent() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myHandler.removeCallbacks(runnable1);
                goToLogin();
            }
        });
    }

    private void goToLogin(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacks(runnable1);
    }
}
