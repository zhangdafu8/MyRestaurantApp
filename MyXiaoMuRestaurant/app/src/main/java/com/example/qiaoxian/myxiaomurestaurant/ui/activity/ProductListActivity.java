package com.example.qiaoxian.myxiaomurestaurant.ui.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.qiaoxian.myxiaomurestaurant.R;
import com.example.qiaoxian.myxiaomurestaurant.adapter.ProductListAdapter;
import com.example.qiaoxian.myxiaomurestaurant.bean.Order;
import com.example.qiaoxian.myxiaomurestaurant.bean.Product;
import com.example.qiaoxian.myxiaomurestaurant.biz.OrderBiz;
import com.example.qiaoxian.myxiaomurestaurant.biz.ProductBiz;
import com.example.qiaoxian.myxiaomurestaurant.net.CommonCallback;
import com.example.qiaoxian.myxiaomurestaurant.ui.view.refresh.SwipeRefresh;
import com.example.qiaoxian.myxiaomurestaurant.ui.view.refresh.SwipeRefreshLayout;
import com.example.qiaoxian.myxiaomurestaurant.utils.T;
import com.example.qiaoxian.myxiaomurestaurant.vo.ProductItem;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends BaseActivity {

    private SwipeRefreshLayout swipeRefreshLayout1;
    private RecyclerView recyclerView1;
    private TextView textView1;
    private Button button1;
    private ProductBiz mProdcutBiz = new ProductBiz();
    private ProductListAdapter mAdapter;
    private List<ProductItem> mDatas = new ArrayList<>();
    private int mCurrentPage;
    private float mTotalPrice;
    private int mTotoalCount;
    private OrderBiz mOrderBiz = new OrderBiz();
    private Order mOrder = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        setUpToolbar();
        setTitle("订单");
        initView();
        initEvent();
        loadDatas();
    }

    private void initView() {
        swipeRefreshLayout1 = (SwipeRefreshLayout)findViewById(R.id.productSwipe);
        recyclerView1 = (RecyclerView)findViewById(R.id.productRecycle);
        textView1 = (TextView)findViewById(R.id.productCount);
        button1 = (Button)findViewById(R.id.productPay);
        swipeRefreshLayout1.setMode(SwipeRefreshLayout.Mode.BOTH);
        swipeRefreshLayout1.setColorSchemeColors(Color.RED,Color.BLACK,Color.GREEN,Color.YELLOW);

        mAdapter = new ProductListAdapter(this,mDatas);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView1.setAdapter(mAdapter);

    }

    private void initEvent() {
        swipeRefreshLayout1.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadMore();
            }
        });
        swipeRefreshLayout1.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDatas();
            }
        });

        mAdapter.setOnProductListener(new ProductListAdapter.OnProductListener() {
            @Override
            public void onProductAdd(ProductItem productItem) {
                mTotoalCount++;
                textView1.setText("数量:"+mTotoalCount);
                mTotalPrice += productItem.getPrice();
                if(mTotoalCount == 0){
                    mTotalPrice =0;
                }

                button1.setText(mTotalPrice+"元，立即支付");
                mOrder.addProduct(productItem);
            }

            @Override
            public void onProductSub(ProductItem productItem) {
                mTotoalCount--;
                textView1.setText("数量:"+mTotoalCount);
                mTotalPrice -= productItem.getPrice();
                button1.setText(mTotalPrice+"元，立即支付");
                mOrder.removeProduct(productItem);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(mTotoalCount<=0){
                    T.showToast("您还没有选择菜品!");
                    return;
                }

                mOrder.setCount(mTotoalCount);
                mOrder.setPrice(mTotalPrice);
                mOrder.setRestaurant(mDatas.get(0).getRestaurant());
                startLoadingProgress();


                mOrderBiz.add(mOrder, new CommonCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        stopLoadingProgress();
                        T.showToast("订单支付成功！");
                        setResult(RESULT_OK);
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

    private void loadDatas() {
        startLoadingProgress();

        mProdcutBiz.listByPage(0, new CommonCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> response) {
                stopLoadingProgress();
                swipeRefreshLayout1.setRefreshing(false);
                mCurrentPage = 0;
                mDatas.clear();
                for(Product p : response){
                    mDatas.add(new ProductItem(p));
                }
                mAdapter.notifyDataSetChanged();
                //clear data
                mTotalPrice = 0;
                mTotoalCount = 0;
                textView1.setText("数量:"+mTotoalCount);
                button1.setText(mTotalPrice+"元，立即支付");

            }

            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                swipeRefreshLayout1.setRefreshing(false);
            }
        });

    }

    private void loadMore() {
        startLoadingProgress();

        mProdcutBiz.listByPage(++mCurrentPage, new CommonCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> response) {
                stopLoadingProgress();
                swipeRefreshLayout1.setPullUpRefreshing(false);
                if(response.size()==0){
                    T.showToast("没有更多");
                    return;
                }
                T.showToast("又找到"+response.size()+"道菜");
                for(Product p : response){
                    mDatas.add(new ProductItem(p));
                }
                mAdapter.notifyDataSetChanged();


            }

            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mCurrentPage--;
                swipeRefreshLayout1.setPullUpRefreshing(false);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProdcutBiz.onDestroy();
    }
}
