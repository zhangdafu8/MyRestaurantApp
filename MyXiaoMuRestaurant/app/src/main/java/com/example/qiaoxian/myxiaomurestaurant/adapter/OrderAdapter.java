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
import com.example.qiaoxian.myxiaomurestaurant.bean.Order;
import com.example.qiaoxian.myxiaomurestaurant.config.Config;
import com.example.qiaoxian.myxiaomurestaurant.ui.activity.OrderActivity;
import com.example.qiaoxian.myxiaomurestaurant.ui.activity.OrderDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> mDatas;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public OrderAdapter(Context context, List<Order> datas){
        mDatas = datas;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = layoutInflater.inflate(R.layout.item_order_list,viewGroup,false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i) {
        Order order = mDatas.get(i);
        Picasso.with(mContext).load(Config.baseURL+order.getRestaurant().getIcon())
                .placeholder(R.drawable.pictures_no).into(orderViewHolder.itemOrderImage);
        if(order.getPs().size()>0){
            orderViewHolder.itemOrderLabel.setText(order.getPs().get(0).product.getName()+
                    "等"+order.getCount()+"件商品");
        }else{
            orderViewHolder.itemOrderLabel.setText("无消费");
        }
        orderViewHolder.itemOrderName.setText(order.getRestaurant().getName());
        orderViewHolder.itemOrderPrice.setText("共消费"+order.getPrice()+"元");
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder{
        public ImageView itemOrderImage;
        public TextView itemOrderName;
        public TextView itemOrderLabel;
        public TextView itemOrderPrice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OrderDetailActivity.launch(mContext,mDatas.get(getLayoutPosition()));
                }
            });

            itemOrderImage = (ImageView)itemView.findViewById(R.id.itemOrderImage);
            itemOrderName = (TextView)itemView.findViewById(R.id.itemOrderRestaurantName);
            itemOrderLabel = (TextView)itemView.findViewById(R.id.itemOrderLabel);
            itemOrderPrice = (TextView)itemView.findViewById(R.id.itemOrderPrice);
        }
    }
}
