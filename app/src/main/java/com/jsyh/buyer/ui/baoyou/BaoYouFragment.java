package com.jsyh.buyer.ui.baoyou;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jsyh.buyer.BaseFragment;
import com.jsyh.buyer.R;
import com.jsyh.buyer.adapter.CommodityAdapter;
import com.jsyh.buyer.adapter.diffutil.BaoYouDiffutil;
import com.jsyh.buyer.model.GoodsModel;
import com.jsyh.buyer.model.GoogsCategoryModel;
import com.jsyh.buyer.ui.iview.CommonView;
import com.jsyh.buyer.ui.presenter.CommonPresnter;
import com.jsyh.buyer.ui.search.SearchActivity;
import com.jsyh.buyer.ui.search.SearchResultActivity;
import com.jsyh.buyer.utils.ActivityUtils;
import com.jsyh.buyer.utils.SystemBarHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

/**
 * 9.9包邮
 */

public class BaoYouFragment extends BaseFragment implements CommonView, BGARefreshLayout.BGARefreshLayoutDelegate
,CommodityAdapter.OnItemClieck{


    private Handler handler = new Handler();

    private Unbinder unbinder;

    @BindView(R.id.refresLayout)
    BGARefreshLayout mRefreshLayout;

    @BindView(R.id.baoyouRecycler)
    RecyclerView mRecycler;
    private BGARefreshViewHolder refreshViewHolder;

    @BindView(R.id.barLayout)
    LinearLayout mBarLayout;


    private GridLayoutManager layoutManager;
    private CommodityAdapter mAdapter;

    private CommonPresnter mPresnter;
    private int currentPage = 1;
    private int pageCount = 30;

    private Double endPrice = new Double(9.9);
    private String keyword;


    private boolean volumeAsc;
    private boolean priceAsc;
    private boolean populerAsc;

    private boolean isLoading =true;
    private boolean isRefreshData;
    private String order = null;

    public static BaoYouFragment newInstance() {

        BaoYouFragment fragment = new BaoYouFragment();

        return fragment;
    }

    public static BaoYouFragment newInstance(String keyword) {

        BaoYouFragment fragment = new BaoYouFragment();
        Bundle extra = new Bundle();
        extra.putString("keyword", keyword);

        fragment.setArguments(extra);

        return fragment;
    }

    public BaoYouFragment() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStateColor = Color.WHITE;

        mPresnter = new CommonPresnter(this);


        if (getArguments() != null) {
            endPrice =null;
            keyword =  getArguments().getString("keyword");
        }
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
            rootView = inflater.inflate(R.layout.baoyou_fragment, container, false);
            unbinder = ButterKnife.bind(this, rootView);
            initView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView(View view) {


        SystemBarHelper.setHeightAndPadding(getActivity(), mBarLayout);

        initRecycler();
        initRefreshLayout();

        mPresnter.loadGoods(null,endPrice,null,null,currentPage,pageCount,keyword,null,null,null);

    }


    @OnClick({R.id.tvSales,R.id.tvPrice,R.id.tvPopuler})
    public void viewClick(View view) {


        switch (view.getId()) {
            case R.id.tvSales:
                if (volumeAsc) {
                    volumeAsc =false;
                    order = "volume_desc";
                } else {
                    volumeAsc =true;
                    order = "volume_asc";
                }
                break;

            case R.id.tvPrice:
                if (priceAsc) {
                    priceAsc = false;

                    order = "price_desc";
                } else {
                    priceAsc = true;
                    order = "price_asc";
                }

                break;

            case R.id.tvPopuler:

                if (populerAsc) {
                    populerAsc = false;
                    order = "receive_desc";
                } else {
                    populerAsc = true;
                    order = "receive_asc";
                }
                break;
        }

        mAdapter.clear();
        currentPage = 1;
        mPresnter.loadGoods(null,endPrice,null,null,currentPage,pageCount,keyword,order,null,null);



    }

    private void initRecycler() {
        layoutManager = new GridLayoutManager(getContext(), 2);


        mRecycler.setLayoutManager(layoutManager);

        mAdapter = new CommodityAdapter();
        mAdapter.setmOnItemClieck(this);

        mRecycler.setAdapter(mAdapter);

        mRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();
                int spanSize = params.getSpanSize();
                int spanIndex = params.getSpanIndex();
                if (spanIndex == 0) {

                    outRect.left = (int) getDIP(R.dimen.goodsItemParentPadding);
                    outRect.top = (int) getDIP(R.dimen.goodsItemPadding);
                    outRect.right = (int) getDIP(R.dimen.goodsItemPadding);
                    outRect.bottom = (int) getDIP(R.dimen.goodsItemPadding);

                } else {
                    outRect.left = (int) getDIP(R.dimen.goodsItemPadding);
                    outRect.top = (int) getDIP(R.dimen.goodsItemPadding);
                    outRect.right = (int) getDIP(R.dimen.goodsItemParentPadding);
                    outRect.bottom = (int) getDIP(R.dimen.goodsItemPadding);
                }

            }
        });

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(BaoYouFragment.this.getContext()).resumeRequests();

                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Glide.with(BaoYouFragment.this.getContext()).pauseRequests();

                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {

                }
            }
        });

    }

    private void initRefreshLayout() {
        mRefreshLayout.setDelegate(this);

        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        refreshViewHolder = new BGANormalRefreshViewHolder(getContext(), true);

        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);

    }

    @OnClick(R.id.searchLayout)
    void searchClick(View view) {

        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);

        if (getActivity() instanceof SearchResultActivity) {
            getActivity().finish();
        }
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRefreshLayout.getCurrentRefreshStatus() == BGARefreshLayout.RefreshStatus.REFRESHING) {
            mRefreshLayout.endRefreshing();
        }
    }

    @Override
    protected void setMessageState(int state) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //=============================== view ====================================
    @Override
    public void onLoadGoods(List<GoodsModel> model) {

        if (isRefreshData) {

            mRefreshLayout.endRefreshing();

            DiffUtil.DiffResult diffResult =
                    DiffUtil.calculateDiff(new BaoYouDiffutil(mAdapter.getDatas(), model));

            diffResult.dispatchUpdatesTo(mAdapter);
            mAdapter.getDatas().clear();
            mAdapter.setDatas(model,false);

            return;
        }

        isLoading = false;
        if (mAdapter.getItemCount() > 0 && model != null && model.isEmpty()) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_more), Toast.LENGTH_SHORT).show();
        }
        if (mRefreshLayout.isLoadingMore()) {
            mRefreshLayout.endLoadingMore();
        }
        if (model!=null && !model.isEmpty()) {
            mAdapter.setDatas(model,true);
            currentPage++;
        }
    }

    @Override
    public void onStartGoods() {
        if (mRefreshLayout.getCurrentRefreshStatus() == BGARefreshLayout.RefreshStatus.REFRESHING) {
            return;
        }
        if (!mRefreshLayout.isLoadingMore()) {
            mLoadDialog.show();
        }
    }

    @Override
    public void onLoadGoodsErrorOrComplete() {
        isLoading = false;
        isRefreshData =false;
        mLoadDialog.dismiss();
    }

    @Override
    public void onLoadCategory(List<GoogsCategoryModel> model) {

    }

    @Override
    public void onLoadCategoryError() {

    }

    @Override
    public void onFindUnreadMsg(boolean msg) {

    }

    @Override
    public void onLoadSharePicUlr(String url) {

    }

    @Override
    public void onLoadShareBitmap(Bitmap bitmap) {

    }

    //================================== refresh =============================
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

        isRefreshData = true;
        int refreshCount = pageCount*currentPage;
        mPresnter.loadGoods(null, endPrice, null, null, 1, refreshCount, keyword, order,null,null);

        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mRefreshLayout.endRefreshing();


            }
        }, 300);*/
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {

        mPresnter.loadGoods(null, endPrice, null, null, currentPage, pageCount, keyword, order,null,null);
        return true;
    }

    @Override
    public void onGoodsClickListener(Parcelable parcelable) {
        ActivityUtils.showDetailView(getActivity(),parcelable);
    }
}
