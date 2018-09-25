package com.example.qiaoxian.myxiaomurestaurant.biz;

import com.example.qiaoxian.myxiaomurestaurant.bean.User;
import com.example.qiaoxian.myxiaomurestaurant.config.Config;
import com.example.qiaoxian.myxiaomurestaurant.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

public class UserBiz {

    public void login(String username, String password, CommonCallback<User> commonCallback){
        OkHttpUtils.post().url(Config.baseURL+"user_login").tag(this).
                addParams("username",username).addParams("password",password).
                build().execute(commonCallback);

    }

    public void register(String username, String password, CommonCallback<User> commonCallback){
        OkHttpUtils.post().url(Config.baseURL+"user_register").tag(this).
                addParams("username",username).addParams("password",password).
                build().execute(commonCallback);

    }

    public void onDestroy(){
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
