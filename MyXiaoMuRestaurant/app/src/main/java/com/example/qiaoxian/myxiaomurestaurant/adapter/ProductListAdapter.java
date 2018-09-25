package com.example.qiaoxian.myxiaomurestaurant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qiaoxian.myxiaomurestaurant.R;
import com.example.qiaoxian.myxiaomurestaurant.config.Config;
import com.example.qiaoxian.myxiaomurestaurant.ui.activity.ProductDetailActivity;
import com.example.qiaoxian.myxiaomurestaurant.utils.T;
import com.example.qiaoxian.myxiaomurestaurant.vo.ProductItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListItemViewHolder> {
    private Context mContext;
    private List<ProductItem> mProductItems;
    private LayoutInflater mLayoutInflater;

    public ProductListAdapter(Context context,List<ProductItem> productItems){
        mContext = context;
        mProductItems = productItems;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ProductListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = mLayoutInflater.inflate(R.layout.item_product_list,viewGroup,false);
        return new ProductListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListItemViewHolder productListItemViewHolder, int i) {
        ProductItem productItem = mProductItems.get(i);
        Picasso.with(mContext).load(Config.baseURL+productItem.getIcon())
                .placeholder(R.drawable.pictures_no).into(productListItemViewHolder.mIvImage);
        productListItemViewHolder.mTvName.setText(productItem.getName());
        productListItemViewHolder.mTvCount.setText(productItem.count+"");
        productListItemViewHolder.mTvLabel.setText(productItem.getLabel());
        productListItemViewHolder.mTvPrice.setText(productItem.getPrice()+"元／份");
    }

    @Override
    public int getItemCount() {
        return mProductItems.size();
    }

    public interface OnProductListener{
        void onProductAdd(ProductItem productItem);

        void onProductSub(ProductItem productItem);
    }

    private OnProductListener mOnProductListener;

    public void setOnProductListener(OnProductListener onProductListener){
        mOnProductListener = onProductListener;
    }

    class ProductListItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView mIvImage;
        public TextView mTvName;
        public TextView mTvLabel;
        public TextView mTvPrice;
        public ImageView mIvAdd;
        public ImageView mIvSub;
        public TextView mTvCount;

        public ProductListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvImage = (ImageView)itemView.findViewById(R.id.PLAImage);
            mTvName = (TextView) itemView.findViewById(R.id.PLARestaurantName);
            mTvLabel = (TextView) itemView.findViewById(R.id.PLALabel);
            mTvPrice = (TextView) itemView.findViewById(R.id.PLAPrice);
            mIvAdd = (ImageView)itemView.findViewById(R.id.PLAAdd);
            mIvSub = (ImageView)itemView.findViewById(R.id.PLASub);
            mTvCount = (TextView)itemView.findViewById(R.id.PLAAddSubNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductDetailActivity.launch(mContext,mProductItems.get(getLayoutPosition()));

                }
            });

            mIvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getLayoutPosition();
                    ProductItem productItem = mProductItems.get(pos);
                    productItem.count +=1;
                    mTvCount.setText(productItem.count+"");
                    //call back to activity
                    if(mOnProductListener!=null){
                        mOnProductListener.onProductAdd(productItem);
                    }

                }
            });

            mIvSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getLayoutPosition();
                    ProductItem productItem = mProductItems.get(pos);
                    if(productItem.count<=0){
                        T.showToast("不能小于0");
                        return;
                    }
                    productItem.count -=1;
                    mTvCount.setText(productItem.count+"");
                    //call back to activity
                    if(mOnProductListener!=null){
                        mOnProductListener.onProductSub(productItem);
                    }
                }
            });



        }

    }
}
