package com.jsyh.buyer.adapter.diffutil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import com.jsyh.buyer.model.GoodsModel;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */

public class BaoYouDiffutil  extends DiffUtil.Callback {
    private List<GoodsModel> oldGoods;
    private List<GoodsModel> newGoods;

    public BaoYouDiffutil(List<GoodsModel> oldGoods, List<GoodsModel> newGoods) {
        this.oldGoods = oldGoods;
        this.newGoods = newGoods;
    }

    @Override
    public int getOldListSize() {
        return oldGoods == null?0:oldGoods.size();
    }

    @Override
    public int getNewListSize() {
        return newGoods == null?0:newGoods.size();
    }

    //这个是用来判断是否是一个对象的
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

        GoodsModel oldModel = oldGoods.get(oldItemPosition);
        GoodsModel newModel = newGoods.get(newItemPosition);
        if (TextUtils.isEmpty(oldModel.getGoodsId()) || TextUtils.isEmpty(newModel.getGoodsId())) {
            return false;
        }
        if (oldModel.getGoodsId().equals(newModel.getGoodsId())) {
            return true;
        }
        return false;
    }

    //这个是用来判断相同对象的内容是否相同
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        GoodsModel oldModel = oldGoods.get(oldItemPosition);
        GoodsModel newModel = newGoods.get(newItemPosition);
        try {
            if (oldModel.getPrice().equals(newModel.getPrice())
                    && oldModel.getImgUrl().equals(newModel.getImgUrl())
                    &&oldModel.getOrgPrice().equals(newModel.getOrgPrice())) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return false;
    }

    //找出其中的不同
    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        GoodsModel oldModel = oldGoods.get(oldItemPosition);
        GoodsModel newModel = newGoods.get(newItemPosition);
        Bundle diff = new Bundle();

        if (!oldModel.getPrice().equals(newModel.getPrice())) {
            diff.putString("price", newModel.getPrice());
        }
        if (!oldModel.getImgUrl().equals(newModel.getImgUrl())) {
            diff.putString("imageUrl", newModel.getImgUrl());
        }

        if (!oldModel.getOrgPrice().equals(newModel.getOrgPrice())) {
            diff.putString("orgPrice", newModel.getOrgPrice());
        }



        return diff;
    }
}
