package com.example.qiaoxian.myxiaomurestaurant.net;

import com.example.qiaoxian.myxiaomurestaurant.utils.GsonUtil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;

public abstract class CommonCallback<T> extends StringCallback {
    Type myType;

    public CommonCallback(){
        Class<? extends CommonCallback> cla = getClass();
        Type genericSuperclass = cla.getGenericSuperclass();
        if(genericSuperclass instanceof Class){
            throw new RuntimeException("Miss Type parameter");
        }
        ParameterizedType parameterizedType = (ParameterizedType)genericSuperclass;
        myType = parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        onError(e);
    }

    @Override
    public void onResponse(String response, int id) {
        try {
            JSONObject myJason = new JSONObject(response);
            int matchCode = myJason.getInt("resultCode");
            if(matchCode == 1){
                String mydata = myJason.getString("data");
                Gson gson = new Gson();
                onSuccess((T) gson.fromJson(mydata,myType));
            }else{
                onError(new RuntimeException(myJason.getString("resultMessage")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            onError(e);
        }

    }

    public abstract void onSuccess(T response);

    public abstract void onError(Exception e);
}
