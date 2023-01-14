package com.jsyh.buyer.adapter;

import android.graphics.Paint;
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
 * 白菜价 recycler adapter
 */

public class CheapRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<GoodsModel> datas = new ArrayList<>();

    private OnItemClieck onItemClieck;

    public CheapRecyclerAdapter() {
    }

    public CheapRecyclerAdapter(List<GoodsModel> datas) {
        this.datas = datas;
    }

    public void setDatas(List<GoodsModel> datas) {
        if (datas != null && !datas.isEmpty()) {
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        RecyclerView.ViewHolder holder = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_commodity_item, parent, false);
        holder = new CommodityHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GoodsModel model = datas.get(position);

        CommodityHolder otherHolder = (CommodityHolder) holder;

        otherHolder.tvGoodsIntroduction.setText(model.getTitle());

        otherHolder.tvYuanJia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        otherHolder.tvYuanJia.setText("¥"+model.getOrgPrice());

        otherHolder.tvNowPrice.setText("¥"+model.getPrice());
        Glide.with(otherHolder.itemView.getContext())
                .load(model.getImgUrl())
                .placeholder(R.drawable.placeholder)
                .bitmapTransform(new RoundedCornersTransformation(otherHolder.itemView.getContext(),18,0, RoundedCornersTransformation.CornerType.TOP))
                .error(R.drawable.placeholder)
                .into(otherHolder.ivGoods);


        otherHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClieck != null) {
                    onItemClieck.onGoodsClickListener(model/*.getIid(),model.getCouponsHref()*/);
                }
            }
        });

        otherHolder.tvCouponsSurplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClieck != null) {
                    onItemClieck.onGoodsClickListener(model/*.getGoodsId(),model.getCouponsHref()*/);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    public void setOnItemClieck(OnItemClieck onItemClieck) {
        this.onItemClieck = onItemClieck;
    }

    public interface OnItemClieck{
        void onGoodsClickListener(Parcelable parcelable);

    }


}
