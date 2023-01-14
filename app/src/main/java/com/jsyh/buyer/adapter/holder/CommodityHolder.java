package com.jsyh.buyer.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsyh.buyer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mo on 17-4-1.
 */

public class CommodityHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivGoods)
    public ImageView ivGoods;
    @BindView(R.id.tvGoodsIntroduction)
    public TextView tvGoodsIntroduction;
    @BindView(R.id.tvNowPrice)
    public TextView tvNowPrice;

    @BindView(R.id.tvYuanJia)
    public TextView tvYuanJia;

    @BindView(R.id.tvCouponsSurplus)
    public TextView tvCouponsSurplus;       //剩余优惠券数量


    public CommodityHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }




}
