package com.jsyh.buyer.adapter;

import android.graphics.Paint;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jsyh.buyer.R;
import com.jsyh.buyer.adapter.holder.CommodityHolder;
import com.jsyh.buyer.model.GoodsModel;
import com.jsyh.buyer.utils.ActivityUtils;
import com.jsyh.buyer.utils.image.RoundedCornersTransformation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mo on 17-4-1.
 */

public class NativeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<GoodsModel> datas = new ArrayList<>();

    private GoodsModel headData;

    private OnItemClieck mOnItemClieck;

    public NativeRecyclerAdapter() {
    }


    public NativeRecyclerAdapter(List<GoodsModel> datas, GoodsModel headData) {
        if (datas != null) {
            this.datas.addAll(datas);
            this.headData = headData;
        }
    }

    public void setDatas(List<GoodsModel> datas) {
        if (datas != null) {
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public void setHeadData(GoodsModel headData) {
        this.headData = headData;
        if (this.headData != null) {
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 1:

                return new NativeViewHolder(inflater.inflate(R.layout.native_detail_head, parent, false));
            case 2:
                return new CommodityHolder(inflater.inflate(R.layout.base_commodity_item, parent, false));

        }

        return null;


    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return 1;
        } else {

            return 2;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        int type = getItemViewType(position);
        switch (type) {


            case 1:
                NativeViewHolder nativeViewHolder = (NativeViewHolder) holder;
                Glide.with(holder.itemView.getContext()).load(headData.getImgUrl()).into(nativeViewHolder.mImage);

                nativeViewHolder.mDescription.setText(headData.getTitle());
                nativeViewHolder.mPrice.setText("¥" + headData.getPrice());

                nativeViewHolder.mYuanJia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                nativeViewHolder.mYuanJia.setText("¥" + headData.getOrgPrice());

                nativeViewHolder.mSales.setText("销量: " + headData.getVolume());
                nativeViewHolder.mYiLingQuan.setText("已领优惠券: " + headData.getCouponsReceive());
                nativeViewHolder.mShengYu.setText("剩余: " + headData.getCouponsSurplus());


                nativeViewHolder.goLingQuan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClieck != null) {
                            mOnItemClieck.onDetail(null,headData.getGoodsId(), headData.getCouponsHref());
                        }
                    }
                });

                nativeViewHolder.goDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClieck != null) {
                            mOnItemClieck.onDetail(headData.getIid(), headData.getGoodsId(),null);
                        }
                    }
                });

                break;
            case 2:

                CommodityHolder commodityHolder = (CommodityHolder) holder;

                final GoodsModel model = datas.get(position - 1);
                commodityHolder.tvGoodsIntroduction.setText(model.getTitle());

                commodityHolder.tvYuanJia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                commodityHolder.tvYuanJia.setText("¥" + model.getOrgPrice());

                commodityHolder.tvNowPrice.setText("¥" + model.getPrice());
                Glide.with(holder.itemView.getContext())
                        .load(model.getImgUrl())
                        .bitmapTransform(new RoundedCornersTransformation(holder.itemView.getContext(),18,0, RoundedCornersTransformation.CornerType.TOP))
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(commodityHolder.ivGoods);


                commodityHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClieck != null) {
                            mOnItemClieck.onGoodsClickListener(model/*.getIid(), model.getCouponsHref()*/);
                        }
                    }
                });

                commodityHolder.tvCouponsSurplus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClieck != null) {
                            mOnItemClieck.onGoodsClickListener(model/*.getIid(), model.getCouponsHref()*/);
                        }
                    }
                });
                break;
        }


    }

    @Override
    public int getItemCount() {

        return headData == null ? 0 : datas.size() + 1;
    }


    class NativeViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.image)
        public ImageView mImage;

        @BindView(R.id.tvDescription)
        public TextView mDescription;
        @BindView(R.id.tvPrice)
        public TextView mPrice;
        @BindView(R.id.tvYuanJia)
        public TextView mYuanJia;
        @BindView(R.id.tvSales)
        public TextView mSales;
        @BindView(R.id.text2)
        public TextView mYiLingQuan;
        @BindView(R.id.text3)
        public TextView mShengYu;

        @BindView(R.id.goLingQuan)
        public Button goLingQuan;

        @BindView(R.id.goDetail)
        public Button goDetail;

        public NativeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setmOnItemClieck(OnItemClieck mOnItemClieck) {
        this.mOnItemClieck = mOnItemClieck;
    }

    public interface OnItemClieck {
        void onGoodsClickListener(Parcelable parcelable);

        void onDetail(String iid, String imageId,String url);




    }
}
