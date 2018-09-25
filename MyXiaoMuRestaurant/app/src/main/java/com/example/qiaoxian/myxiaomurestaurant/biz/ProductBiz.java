package com.example.qiaoxian.myxiaomurestaurant.biz;

import com.example.qiaoxian.myxiaomurestaurant.bean.Product;
import com.example.qiaoxian.myxiaomurestaurant.config.Config;
import com.example.qiaoxian.myxiaomurestaurant.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

public class ProductBiz {

    public void listByPage(int currentPage, CommonCallback<List<Product>> commonCallback){
        OkHttpUtils.post().url(Config.baseURL+"product_find").addParams("currentPage"
                ,currentPage+"").tag(this).build().execute(commonCallback);
    }

    public void onDestroy(){
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
