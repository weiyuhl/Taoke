package com.jsyh.buyer.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jsyh.buyer.R;
import com.jsyh.buyer.adapter.holder.CommodityHolder;
import com.jsyh.buyer.adapter.holder.ImageAndTextHolder;
import com.jsyh.buyer.model.AdvertisingModel;
import com.jsyh.buyer.model.GoodsModel;
import com.jsyh.buyer.model.GoogsCategoryModel;
import com.jsyh.buyer.utils.L;
import com.jsyh.buyer.utils.image.GlideCircleTransform;
import com.jsyh.buyer.utils.image.RoundedCornersTransformation;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mo on 17-3-31.
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private Context context;

    public static final int BANNDER = 1;
    public static final int HORIZONTAL = 2;
    public static final int OTHER = 3;



    private List<AdvertisingModel> mAdvertising = new ArrayList<>();
    private List<GoogsCategoryModel> mCategorys = new ArrayList<>();
    private List<GoodsModel> goods = new ArrayList<>();

    private List<Integer> types = new ArrayList<>();    //移动多少type
    private Map<Integer, Integer> positionMap = new HashMap<>();   //


    private OnItemClieck mOnItemClieck;

    public void setOnItemClieck(OnItemClieck onItemClieck) {
        this.mOnItemClieck = onItemClieck;
    }

    public HomeAdapter(Context context) {
        this.context = context;
        initDatas();
    }

    public HomeAdapter(List<AdvertisingModel> mAdvertising, List<GoogsCategoryModel> mCategorys, List<GoodsModel> goods) {
        this.mAdvertising .addAll(mAdvertising) ;
        this.mCategorys.addAll(mCategorys);

        initDatas();
        addGoods(goods);
    }

    private void initDatas() {
        positionMap.put(BANNDER, types.size());
        types.add(BANNDER);     // 始终第一个是banner 类型的

        positionMap.put(HORIZONTAL, types.size());
        types.add(HORIZONTAL);
        types.add(HORIZONTAL);
        types.add(HORIZONTAL);
        types.add(HORIZONTAL);  //四个 商品类型

    }

    public void setAdvertising(List<AdvertisingModel> mAdvertising) {
        if (mAdvertising == null)return;
        this.mAdvertising.clear();
        this.mAdvertising.addAll(mAdvertising);

        notifyDataSetChanged();
    }

    public void setCategorys(List<GoogsCategoryModel> mCategorys) {
        if (mCategorys == null)return;
        this.mCategorys.clear();
        this.mCategorys.addAll(mCategorys);
        notifyDataSetChanged();
    }

    public void setGoods(List<GoodsModel> datas) {
        if (datas == null) {
            return;
        }
        addGoods(datas);
        notifyDataSetChanged();
    }


    private void addGoods(List<GoodsModel> datas) {
        if (datas != null && datas.isEmpty()) {
            return;
        }
        if (goods.isEmpty()) {

            positionMap.put(OTHER, types.size());
        }
        goods.addAll(datas);
        for (int i = 0; i < datas.size(); i++) {
            types.add(OTHER);
        }

    }

    public int getGoodsSize() {
        return goods.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;
        View view  = null;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case BANNDER:
                view = inflater.inflate(R.layout.home_banner_item, parent, false);
                holder = new HomeBannderHolder(view);
                break;

            case HORIZONTAL:
                view = inflater.inflate(R.layout.home_horizontal_item, parent, false);
                holder = new ImageAndTextHolder(view);
                break;

            case OTHER:

                view = inflater.inflate(R.layout.base_commodity_item, parent, false);
                holder = new CommodityHolder(view);
                break;


        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        holder.getItemViewType();

        int realPosition = position - positionMap.get(type);
        if (type == BANNDER) {
            //头部轮播广告
            final List<String> imgesUrl = new ArrayList<>();

            //添加广告数据
            if (mAdvertising != null&& !mAdvertising.isEmpty()) {
                for (final AdvertisingModel model : mAdvertising) {
                    imgesUrl.add(model.getPicUrl());
                    L.dd("-------------"+model.getGoodsId());
                    ((HomeBannderHolder) holder).banner.setOnItemClickListener(new XBanner.OnItemClickListener() {
                        @Override
                        public void onItemClick(XBanner banner, int position) {
                            mOnItemClieck.onBanner(mAdvertising.get(position).getGoodsId(),
                                    mAdvertising.get(position).getUrl(),
                                    mAdvertising.get(position).getTitle(),
                                    mAdvertising.get(position).getContent());

                        }
                    });
                }

                ((HomeBannderHolder) holder).banner.setData(imgesUrl,null);//第二个参数为提示文字资源集合
                ((HomeBannderHolder) holder).banner.setmAdapter(new XBanner.XBannerAdapter() {
                    @Override
                    public void loadBanner(XBanner banner, Object model, View view, int position) {

                        Glide.with(banner.getContext())
                                .load(imgesUrl.get(position))
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.placeholder)
                                .into((ImageView) view);
                    }
                });


            }

        } else if (type == HORIZONTAL) {
            if (!mCategorys.isEmpty()) {

            ImageAndTextHolder type2Holder = (ImageAndTextHolder) holder;

                if (mCategorys.size() >= (realPosition +1)) {
                    final GoogsCategoryModel model = mCategorys.get(realPosition);

                    type2Holder.name.setText(model.getName());

                    Glide.with(type2Holder.itemView.getContext())
                            .load(model.getImg())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .transform(new GlideCircleTransform(type2Holder.itemView.getContext()))
                            .into(type2Holder.imageView);


                    type2Holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnItemClieck != null) {
                                mOnItemClieck.onCategoryClickListener(model.getName());

                            }
                        }
                    });

                }
            }

        }else if (type==OTHER){
            CommodityHolder otherHolder = (CommodityHolder) holder;

            final GoodsModel goodsModel = goods.get(realPosition);

            otherHolder.tvGoodsIntroduction.setText(goodsModel.getTitle());

            otherHolder.tvYuanJia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            otherHolder.tvYuanJia.setText("¥"+goodsModel.getOrgPrice());

            otherHolder.tvNowPrice.setText("¥"+goodsModel.getPrice());
            Glide.with(context)
                    .load(goodsModel.getImgUrl())
                    .bitmapTransform(new RoundedCornersTransformation(otherHolder.itemView.getContext(),18,0, RoundedCornersTransformation.CornerType.TOP))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(otherHolder.ivGoods);

            otherHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClieck != null) {
                        mOnItemClieck.onGoodsClickListener(goodsModel);
                    }
                }
            });

            otherHolder.tvCouponsSurplus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClieck != null) {
                        mOnItemClieck.onGoodsClickListener(goodsModel);
                    }
                }
            });


        }

    }


    @Override
    public int getItemViewType(int position) {
        return types.get(position);
    }



    @Override
    public int getItemCount() {

        return types.size();
    }

    static class HomeBannderHolder extends RecyclerView.ViewHolder {

        XBanner banner;
        public HomeBannderHolder(View itemView) {
            super(itemView);
            banner = (XBanner) itemView.findViewById(R.id.banner);

        }
    }


    public interface OnItemClieck{

        void onBanner(String id,String url,String title,String introduction);

        void onGoodsClickListener(Parcelable parcelable);

        void onCategoryClickListener(String name);
    }


}
