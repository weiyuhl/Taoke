package com.jsyh.buyer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.jsyh.buyer.model.GoogsCategoryModel;
import com.jsyh.buyer.ui.cheap.PagerFragment;

import java.util.List;

/**
 * 白菜价 ViewPager 的adapter
 */

public class CheapViewPaperAdapter extends FragmentStatePagerAdapter {



    private List<GoogsCategoryModel> mGoodsCategory;

    public CheapViewPaperAdapter(FragmentManager fm) {
        super(fm);
    }

    public CheapViewPaperAdapter(FragmentManager fm, List<GoogsCategoryModel> goodsCategory) {
        super(fm);

        this.mGoodsCategory = goodsCategory;
    }

    public void setGoodsCategory(List<GoogsCategoryModel> goodsCategory) {
        this.mGoodsCategory = goodsCategory;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return  PagerFragment.newInstance(mGoodsCategory.get(position).getClassId());
    }

    @Override
    public int getCount() {
        return mGoodsCategory != null ? mGoodsCategory.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mGoodsCategory.get(position).getName();
//        return super.getPageTitle(position);
    }

    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }


}
