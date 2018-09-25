package com.example.qiaoxian.myxiaomurestaurant.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qiaoxian.myxiaomurestaurant.R;
import com.example.qiaoxian.myxiaomurestaurant.bean.Product;
import com.example.qiaoxian.myxiaomurestaurant.config.Config;
import com.example.qiaoxian.myxiaomurestaurant.utils.T;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends BaseActivity {

    private Product mProduct;
    private ImageView mIvImage;
    private TextView mTvTittle;
    private TextView mTvDescription;
    private TextView mTvPrice;
    private static final String KEY_PRODUCT = "key_product";

    public static void launch(Context context, Product product){
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(KEY_PRODUCT,product);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        setUpToolbar();
        setTitle("详情");

        Intent intent = getIntent();
        if(intent!=null){
            mProduct = (Product)intent.getSerializableExtra(KEY_PRODUCT);
        }
        if(intent == null){
            T.showToast("传递错误");
            return;
        }

        initView();

    }

    private void initView() {
        mIvImage = (ImageView)findViewById(R.id.productDetailImage);
        mTvTittle = (TextView)findViewById(R.id.productDetailTitle);
        mTvDescription = (TextView)findViewById(R.id.productDetailDescription);
        mTvPrice = (TextView)findViewById(R.id.productDetailPrice);
        Picasso.with(this).load(Config.baseURL+mProduct.getIcon())
                .placeholder(R.drawable.pictures_no).into(mIvImage);
        mTvTittle.setText(mProduct.getName());
        mTvDescription.setText(mProduct.getDescription());
        mTvPrice.setText(mProduct.getPrice()+"元／份");

    }
}
