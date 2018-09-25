package com.example.qiaoxian.myxiaomurestaurant.biz;

import com.example.qiaoxian.myxiaomurestaurant.bean.Order;
import com.example.qiaoxian.myxiaomurestaurant.bean.Product;
import com.example.qiaoxian.myxiaomurestaurant.bean.User;
import com.example.qiaoxian.myxiaomurestaurant.config.Config;
import com.example.qiaoxian.myxiaomurestaurant.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;
import java.util.Map;

public class OrderBiz {

    public void listByPage(int currentPage, CommonCallback<List<Order>> commonCallback){
        OkHttpUtils.post().url(Config.baseURL+"order_find").tag(this).addParams("currentPage",
                currentPage+"").build().execute(commonCallback);
    }

    public void add(Order order,CommonCallback<String> commonCallback){
        StringBuilder sb = new StringBuilder();
        Map<Product,Integer> productMap = order.productMap;
        for(Product p:productMap.keySet()){
            sb.append(p.getId()+"_"+productMap.get(p));
            sb.append("|");
        }
        sb = sb.deleteCharAt(sb.length()-1);


        OkHttpUtils.post().url(Config.baseURL+"order_add")
                .addParams("res_id",order.getRestaurant().getId()+"")
                .addParams("product_str",sb.toString())
                .addParams("count",order.getCount()+"")
                .addParams("price",order.getPrice()+"")
                .tag(this).build().execute(commonCallback);
    }

    public void onDestroy(){
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
