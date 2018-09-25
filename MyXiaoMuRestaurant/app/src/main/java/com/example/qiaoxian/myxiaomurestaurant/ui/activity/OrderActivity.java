package com.example.qiaoxian.myxiaomurestaurant.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qiaoxian.myxiaomurestaurant.R;
import com.example.qiaoxian.myxiaomurestaurant.UserInformationHolder;
import com.example.qiaoxian.myxiaomurestaurant.adapter.OrderAdapter;
import com.example.qiaoxian.myxiaomurestaurant.bean.Order;
import com.example.qiaoxian.myxiaomurestaurant.bean.User;
import com.example.qiaoxian.myxiaomurestaurant.biz.OrderBiz;
import com.example.qiaoxian.myxiaomurestaurant.net.CommonCallback;
import com.example.qiaoxian.myxiaomurestaurant.ui.view.CircleTransform;
import com.example.qiaoxian.myxiaomurestaurant.ui.view.refresh.SwipeRefresh;
import com.example.qiaoxian.myxiaomurestaurant.ui.view.refresh.SwipeRefreshLayout;
import com.example.qiaoxian.myxiaomurestaurant.utils.T;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.ACTION_MAIN;

public class OrderActivity extends BaseActivity {
    private ImageView imageViewOrder;
    private Button buttonOrder;
    private TextView textViewOrder;
    private SwipeRefreshLayout swipeRefreshLayoutOrder;
    private RecyclerView recyclerViewOrder;
    private OrderAdapter orderAdapter;
    private List<Order> mDatas = new ArrayList<>();
    private OrderBiz orderBiz = new OrderBiz();
    private int mCurrentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();
        initEvent();
        loadDatas();
    }

    private void initEvent() {
        swipeRefreshLayoutOrder.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDatas();
            }
        });
        swipeRefreshLayoutOrder.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadMore();
            }
        });
        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this,ProductListActivity.class);
                startActivityForResult(intent,1001);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1001 && resultCode==RESULT_OK){
            loadDatas();
        }
    }

    private void loadMore() {
        startLoadingProgress();
        orderBiz.listByPage(++mCurrentPage, new CommonCallback<List<Order>>() {
            @Override
            public void onSuccess(List<Order> response) {
                stopLoadingProgress();
                if(response.size()==0){
                    T.showToast("没有订单了");
                    swipeRefreshLayoutOrder.setPullUpRefreshing(false);
                    return;
                }
                T.showToast("加载成功！");
                mDatas.addAll(response);
                orderAdapter.notifyDataSetChanged();
                swipeRefreshLayoutOrder.setPullUpRefreshing(false);
            }

            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mCurrentPage--;
                swipeRefreshLayoutOrder.setPullUpRefreshing(false);
            }
        });
    }


    //when press BACK, the app will not be closed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            try{
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                return true;

            }catch (Exception e){
                //ignore,then run return
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private void loadDatas() {
        startLoadingProgress();
        orderBiz.listByPage(0, new CommonCallback<List<Order>>() {
            @Override
            public void onSuccess(List<Order> response) {
                stopLoadingProgress();
                mCurrentPage =0;
                T.showToast("订单更新成功");
                mDatas.clear();
                mDatas.addAll(response);
                orderAdapter.notifyDataSetChanged();
                if(swipeRefreshLayoutOrder.isRefreshing()){
                    swipeRefreshLayoutOrder.setRefreshing(false);
                }
            }

            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                if(swipeRefreshLayoutOrder.isRefreshing()){
                    swipeRefreshLayoutOrder.setRefreshing(false);
                }

            }
        });
    }

    private void initView() {
        imageViewOrder=(ImageView)findViewById(R.id.myOrderImageView);
        buttonOrder = (Button)findViewById(R.id.myOderButton);
        textViewOrder = (TextView)findViewById(R.id.myOrderUserName);
        swipeRefreshLayoutOrder = (SwipeRefreshLayout)findViewById(R.id.myOrderSwipe);
        recyclerViewOrder = (RecyclerView)findViewById(R.id.myOrderRecycle);

//        User user = UserInformationHolder.getInstance().getmUser();
//        if(user!=null){
//            textViewOrder.setText(user.getName());
//        }else{
//            toLoginActivity();
//            finish();
//            return;
//        }

        swipeRefreshLayoutOrder.setMode(SwipeRefreshLayout.Mode.BOTH);
        swipeRefreshLayoutOrder.setColorSchemeColors(Color.RED,Color.BLACK,Color.GREEN,Color.YELLOW);
        orderAdapter = new OrderAdapter(this,mDatas);

        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrder.setAdapter(orderAdapter);

        //user image
        Picasso.with(this).load(R.drawable.ic_launcher_round).placeholder(R.drawable.pictures_no)
                .transform(new CircleTransform()).into(imageViewOrder);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        orderBiz.onDestroy();
    }
}
