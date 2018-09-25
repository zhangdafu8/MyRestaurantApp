package com.example.qiaoxian.myxiaomurestaurant.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qiaoxian.myxiaomurestaurant.R;
import com.example.qiaoxian.myxiaomurestaurant.UserInformationHolder;
import com.example.qiaoxian.myxiaomurestaurant.bean.User;
import com.example.qiaoxian.myxiaomurestaurant.biz.UserBiz;
import com.example.qiaoxian.myxiaomurestaurant.net.CommonCallback;
import com.example.qiaoxian.myxiaomurestaurant.utils.T;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;


public class LoginActivity extends BaseActivity {
    private Button buttonLogin;
    private EditText editTextAccount;
    private EditText editTextPassword;
    private TextView textViewRegister;
    private MyOnClickListener myOnClickListener;
    private UserBiz muserBiz = new UserBiz();
    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_PASSWORD = "key_password";

    @Override
    protected void onResume() {
        super.onResume();
        CookieJarImpl cookieJar = (CookieJarImpl) OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
        cookieJar.getCookieStore().removeAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();
        initIntent(getIntent());
    }

    private void initEvent() {
        buttonLogin.setOnClickListener(myOnClickListener);
        textViewRegister.setOnClickListener(myOnClickListener);
    }

    private void initView() {
        buttonLogin = (Button)findViewById(R.id.login_login);
        editTextAccount = (EditText)findViewById(R.id.login_account);
        editTextPassword = (EditText)findViewById(R.id.login_password);
        textViewRegister = (TextView)findViewById(R.id.login_register);
        myOnClickListener = new MyOnClickListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initIntent(intent);
    }

    private void initIntent(Intent intent) {
        if(intent == null) {
            return;
        }
        String usernameInitIntent = intent.getStringExtra(KEY_USERNAME);
        String passwordInitIntent = intent.getStringExtra(KEY_PASSWORD);
        if(TextUtils.isEmpty(usernameInitIntent)||TextUtils.isEmpty(passwordInitIntent)){
            return;
        }
        editTextAccount.setText(usernameInitIntent);
        editTextPassword.setText(passwordInitIntent);
    }

    public static void launch(Context context, String name, String password) {
        Intent intent = new Intent(context,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(KEY_USERNAME,name);
        intent.putExtra(KEY_PASSWORD,password);
        context.startActivity(intent);
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.login_login:
                    String username = editTextAccount.getText().toString();
                    String password = editTextPassword.getText().toString();

                    if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
                        T.showToast("账号或密码不能为空");
                        return;
                    }

                    startLoadingProgress();

                    muserBiz.login(username, password, new CommonCallback<User>() {
                        @Override
                        public void onSuccess(User user) {
                            stopLoadingProgress();
                            T.showToast("登陆成功");

                            //UserInformationHolder.getInstance().setmUser(user);

                            Intent intent1 = new Intent(LoginActivity.
                                    this,OrderActivity.class);
                            startActivity(intent1);
                            finish();
                        }

                        @Override
                        public void onError(Exception e) {
                            stopLoadingProgress();
                            T.showToast(e.getMessage());
                        }
                    });
                    break;
                case R.id.login_register:
                    Intent intent2 = new Intent(LoginActivity.
                            this,RegisterActivity.class);
                    startActivity(intent2);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        muserBiz.onDestroy();
    }
}