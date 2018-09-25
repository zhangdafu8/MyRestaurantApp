package com.example.qiaoxian.myxiaomurestaurant.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qiaoxian.myxiaomurestaurant.R;
import com.example.qiaoxian.myxiaomurestaurant.bean.Order;
import com.example.qiaoxian.myxiaomurestaurant.bean.Product;
import com.example.qiaoxian.myxiaomurestaurant.config.Config;
import com.example.qiaoxian.myxiaomurestaurant.utils.T;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailActivity extends BaseActivity {

    private Order mOrder;
    private ImageView mIvImage;
    private TextView mTvTittle;
    private TextView mTvDescription;
    private TextView mTvPrice;
    private static final String KEY_ORDER = "key_order";

    public static void launch(Context context, Order order){
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(KEY_ORDER,order);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        setUpToolbar();
        setTitle("订单详情");

        Intent intent = getIntent();
        if(intent!=null){
            mOrder = (Order) intent.getSerializableExtra(KEY_ORDER);
        }
        if(mOrder == null){
            T.showToast("传递错误");
            finish();
            return;
        }

        initView();

    }

    private void initView() {
        mIvImage = (ImageView)findViewById(R.id.productDetailImage);
        mTvTittle = (TextView)findViewById(R.id.productDetailTitle);
        mTvDescription = (TextView)findViewById(R.id.productDetailDescription);
        mTvPrice = (TextView)findViewById(R.id.productDetailPrice);
        Picasso.with(this).load(Config.baseURL+mOrder.getRestaurant().getIcon())
                .placeholder(R.drawable.pictures_no).into(mIvImage);
        mTvTittle.setText(mOrder.getRestaurant().getName());
//        mTvDescription.setText(mOrder.getRestaurant().getDescription());
        List<Order.ProductVo> ps = mOrder.getPs();
        StringBuilder str = new StringBuilder();
        for(Order.ProductVo p : ps){
            str.append(p.product.getName()).append(p.count).append("\n");
        }
        mTvDescription.setText(str.toString());
        mTvPrice.setText("共消费"+mOrder.getPrice()+"元／份");

    }
}
