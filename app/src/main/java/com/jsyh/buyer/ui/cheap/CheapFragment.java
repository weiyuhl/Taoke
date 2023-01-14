package com.jsyh.buyer.ui.cheap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jsyh.buyer.BaseFragment;
import com.jsyh.buyer.MainActivity;
import com.jsyh.buyer.R;
import com.jsyh.buyer.adapter.CheapViewPaperAdapter;
import com.jsyh.buyer.model.GoodsModel;
import com.jsyh.buyer.model.GoogsCategoryModel;
import com.jsyh.buyer.ui.iview.CheapView;
import com.jsyh.buyer.ui.presenter.CheapPresenter;
import com.jsyh.buyer.ui.search.SearchActivity;
import com.jsyh.buyer.utils.ConvertUtils;
import com.jsyh.buyer.utils.StateBarUitl;
import com.jsyh.buyer.utils.SystemBarHelper;
import com.jsyh.buyer.widget.ScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 白菜价
 */

public class CheapFragment extends BaseFragment implements CheapView, ViewPager.OnPageChangeListener ,PagerFragment.LoadStateListener{


    @BindView(R.id.indicator)
    MagicIndicator indicator;

    @BindView(R.id.cheapPager)
    ViewPager mPager;

    @BindView(R.id.barLayout)
    LinearLayout mBarLayout;

    private CheapViewPaperAdapter mAdapter;


    Unbinder unbinder;

    private CheapPresenter mPresenter;


    private List<String> mIndicatorDatas = new ArrayList<>();

    private String selectTabName;


    public CheapFragment() {
    }

    public static CheapFragment newInstance() {

        CheapFragment fragment = new CheapFragment();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStateColor = Color.WHITE;
        mPresenter = new CheapPresenter(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.cheap_fragment, container, false);
            unbinder = ButterKnife.bind(this, rootView);
            initView(rootView);
            uiFirstInit = true;

        }
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
       /* if (isVisibleToUser && uiFirstInit) {
            mPresenter.loadCategory();
        }*/
    }

    @Override
    protected void initView(View view) {


        SystemBarHelper.setHeightAndPadding(getActivity(), mBarLayout);

        initIndicator(getContext());

        mAdapter = new CheapViewPaperAdapter(getFragmentManager());
        mPager.setOffscreenPageLimit(3);
        mPager.setAdapter(mAdapter);


        mPager.addOnPageChangeListener(this);
        mPresenter.loadCategory();
    }

    private void initIndicator(Context context) {
        indicator.setBackgroundColor(Color.WHITE);      //白色背景
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setScrollPivotX(0.25f);

        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mIndicatorDatas.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mIndicatorDatas.get(index));
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(context,R.color.indicatorTextColor));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context,R.color.indicatorTextSelected));
                simplePagerTitleView.setTextSize(15);

                int padding = ConvertUtils.dp2px(getContext(), 8);
                simplePagerTitleView.setPadding(padding, 0, 0, padding);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setYOffset(UIUtil.dip2px(context, 3));
                indicator.setColors(getResources().getColor(R.color.indicatorTextColor));
                return indicator;
            }
        });

        indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicator, mPager);

    }

    @OnClick(R.id.searchLayout)
    void searchClick(View view) {

        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (SystemBarHelper.isMIUI6Later() || SystemBarHelper.isFlyme4Later() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SystemBarHelper.setStatusBarDarkMode(getActivity());
                SystemBarHelper.tintStatusBar(getActivity(), mStateColor, 0);
            } else {
                mStateColor = getResources().getColor(R.color.kitkatStateBarColor);
                SystemBarHelper.tintStatusBar(getActivity(), mStateColor, 0);
            }
        }

        selectTabName = ((MainActivity) getActivity()).getSeletedTabName().toString();
        selectTabByName();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        uiFirstInit =false;
    }

    @Override
    protected void setMessageState(int state) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void selectTabByName() {
        if (TextUtils.isEmpty(selectTabName)) return;

        if (mAdapter.getCount() > 0) {
            for (int i = 0; i < mAdapter.getCount(); i++) {
                if (mAdapter.getPageTitle(i).equals(selectTabName)) {
                    mPager.setCurrentItem(i);
                    selectTabName = null;
                    ((MainActivity) getActivity()).clearSelectTabName();
                    break;
                }
            }
        }
    }

    @OnClick({R.id.cheapPager})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.cheapPager:
                break;
        }
    }


    @OnClick(R.id.searchRight)
    void setSearchRight() {

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    //======================================= view =================================
    @Override
    public void onLoadGoodsCategory(List<GoogsCategoryModel> model) {
        if (model != null && model.size() > 0) {
            mAdapter.setGoodsCategory(model);
            selectTabByName();

            for (GoogsCategoryModel categoryModel : model) {
                mIndicatorDatas.add(categoryModel.getName());
            }

            indicator.getNavigator().notifyDataSetChanged();

        }
    }

    @Override
    public void onLoadCategoryError() {

    }

    @Override
    public void onLoadGoodsByKey(List<GoodsModel> mode) {

    }

    @Override
    public void onLoadGoddsErrorWithComplete() {

    }

    @Override
    public void onFindUnread(boolean msg) {
    }


    @Override
    public void start() {
        mLoadDialog.show();
    }

    @Override
    public void errorOrComplete() {
        mLoadDialog.dismiss();
    }
}
