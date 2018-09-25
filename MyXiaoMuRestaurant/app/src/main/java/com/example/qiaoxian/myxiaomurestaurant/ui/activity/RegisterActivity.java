package com.example.qiaoxian.myxiaomurestaurant.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.qiaoxian.myxiaomurestaurant.R;
import com.example.qiaoxian.myxiaomurestaurant.bean.User;
import com.example.qiaoxian.myxiaomurestaurant.biz.UserBiz;
import com.example.qiaoxian.myxiaomurestaurant.net.CommonCallback;
import com.example.qiaoxian.myxiaomurestaurant.utils.T;


public class RegisterActivity extends BaseActivity {
    private EditText editTextPhoneNumber;
    private EditText editTextPassword;
    private EditText editTextRePassword;
    private Button buttonSubmit;

    UserBiz mUserBiz = new UserBiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setUpToolbar();
        initView();
        initEvent();
        setTitle("注册");
    }


    private void initEvent() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = editTextPhoneNumber.getText().toString();
                final String password = editTextPassword.getText().toString();
                String rePassord = editTextRePassword.getText().toString();

                if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
                    T.showToast("账号或密码不能为空");
                    return;
                }

                if(!password.equals(rePassord)){
                    T.showToast("两次输入的密码不一致");
                    return;
                }

                startLoadingProgress();

                mUserBiz.register(username, password, new CommonCallback<User>() {
                    @Override
                    public void onSuccess(User response) {
                        stopLoadingProgress();
                        T.showToast("注册成功，用户名为:"+username);

                        //UserInformationHolder.getInstance().setmUser(user);

                        LoginActivity.launch(RegisterActivity.this,username,password);
                        finish();
                    }

                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }
                });
            }
        });
    }

    private void initView() {
        editTextPhoneNumber = (EditText) findViewById(R.id.register_phoneNumber);
        editTextPassword = (EditText) findViewById(R.id.register_password);
        editTextRePassword = (EditText)findViewById(R.id.register_rePassword);
        buttonSubmit = (Button)findViewById(R.id.register_button);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserBiz.onDestroy();
    }
}
