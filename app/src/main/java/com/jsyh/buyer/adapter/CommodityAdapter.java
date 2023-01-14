package com.jsyh.buyer.adapter;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.jsyh.buyer.R;
import com.jsyh.buyer.adapter.holder.CommodityHolder;
import com.jsyh.buyer.model.GoodsModel;
import com.jsyh.buyer.utils.image.RoundedCornersTransformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on 17-4-1.
 */

public class CommodityAdapter extends RecyclerView.Adapter<CommodityHolder>{


    private List<GoodsModel> datas = new ArrayList<>();

    private OnItemClieck mOnItemClieck;

    public CommodityAdapter() {
    }


    public CommodityAdapter(List<GoodsModel> datas) {
        if (datas != null) {
            this.datas.addAll(datas);
        }
    }

    public void setDatas(List<GoodsModel> datas,boolean notify) {
        if (datas != null) {
            this.datas.addAll(datas);
            if (notify) {

                notifyDataSetChanged();
            }
        }
    }

    public void clear() {
        if (!datas.isEmpty()) {
            datas.clear();
            notifyDataSetChanged();
        }
    }

    public List<GoodsModel> getDatas() {
        return datas;
    }

    @Override
    public CommodityHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_commodity_item, parent, false);



        return new CommodityHolder(view);
    }

    @Override
    public void onBindViewHolder(CommodityHolder holder, int position) {

        final GoodsModel model = datas.get(position);

        holder.tvGoodsIntroduction.setText(model.getTitle());

        holder.tvYuanJia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.tvYuanJia.setText("¥"+model.getOrgPrice());

        holder.tvNowPrice.setText("¥"+model.getPrice());
        Glide.with(holder.itemView.getContext())
                .load(model.getImgUrl())
                .bitmapTransform(new RoundedCornersTransformation(holder.itemView.getContext(),18,0, RoundedCornersTransformation.CornerType.TOP))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.ivGoods);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClieck != null) {
                    mOnItemClieck.onGoodsClickListener(model);
                }
            }
        });

        holder.tvCouponsSurplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClieck != null) {
                    mOnItemClieck.onGoodsClickListener(model);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(CommodityHolder holder, int position, List<Object> payloads) {
        if (payloads != null && payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        }else {

            Bundle bundle = (Bundle) payloads.get(0);

            String price = bundle.getString("price");
            String imageUrl = bundle.getString("imageUrl");
            String orgPrice = bundle.getString("orgPrice");

            holder.tvYuanJia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvYuanJia.setText("¥"+orgPrice);

            holder.tvNowPrice.setText("¥"+price);
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .bitmapTransform(new RoundedCornersTransformation(holder.itemView.getContext(),18,0, RoundedCornersTransformation.CornerType.TOP))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(holder.ivGoods);

        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setmOnItemClieck(OnItemClieck mOnItemClieck) {
        this.mOnItemClieck = mOnItemClieck;
    }

    public interface OnItemClieck{
        void onGoodsClickListener(Parcelable parcelable);

    }
}
