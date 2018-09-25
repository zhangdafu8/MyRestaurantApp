package com.example.qiaoxian.myxiaomurestaurant.ui.activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.qiaoxian.myxiaomurestaurant.R;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中");
    }

    protected void setUpToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    protected void stopLoadingProgress() {
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();

        }
    }

    protected void startLoadingProgress() {
        progressDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLoadingProgress();
        progressDialog = null;
    }

    protected void toLoginActivity() {
    }
}
