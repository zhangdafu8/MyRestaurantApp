package com.example.qiaoxian.myxiaomurestaurant;

import android.text.TextUtils;

import com.example.qiaoxian.myxiaomurestaurant.bean.User;
import com.example.qiaoxian.myxiaomurestaurant.utils.SPUtils;

public class UserInformationHolder {
    //why cannot work?
    private static UserInformationHolder mUserInformationHolder = new UserInformationHolder();
    private User mUser;
    private static final String KEY_USERNAME = "key_username";

    public static UserInformationHolder getInstance(){
        return mUserInformationHolder;
    }


    public void setmUser(User user){
        mUser = user;
        if(user != null){
            SPUtils.getInstance().put(KEY_USERNAME,user.getName());
        }
    }

    public User getmUser(){
        User u = mUser;
        if(u == null){
            String userName = (String) SPUtils.getInstance().get(KEY_USERNAME,"");
            if(!TextUtils.isEmpty(userName)){
                u = new User();
                u.setName(userName);
            }
        }
        mUser = u;
        return u;
    }


}

