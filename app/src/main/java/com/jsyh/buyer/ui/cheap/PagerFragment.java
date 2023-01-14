package com.jsyh.buyer.ui.cheap;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jsyh.buyer.BaseFragment;
import com.jsyh.buyer.R;
import com.jsyh.buyer.adapter.CheapRecyclerAdapter;
import com.jsyh.buyer.model.GoodsModel;
import com.jsyh.buyer.ui.iview.CheapPageView;
import com.jsyh.buyer.ui.presenter.CheapPresenter;
import com.jsyh.buyer.utils.ActivityUtils;
import com.jsyh.buyer.utils.L;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

/**
 * Created by mo on 17-4-1.
 */

public class PagerFragment extends BaseFragment implements CheapPageView, BGARefreshLayout.BGARefreshLayoutDelegate
,CheapRecyclerAdapter.OnItemClieck{

    private CheapPresenter mPresenter;

    //查询关键字
    public static final String GOODS_CLASS = "goodClass";
    private String keyword;
    private int goodsClass;

    @BindView(R.id.refresLayout)
    BGARefreshLayout mRefreshLayout;

    @BindView(R.id.baseRecyclerView)
    RecyclerView mRecycler;
    Unbinder unbinder;

    private GridLayoutManager mLayoutManager;

    private CheapRecyclerAdapter mAdapter;

    private int currentPage = 1;
    private int pageSize = 20;
    private BGARefreshViewHolder refreshViewHolder;


    public static PagerFragment newInstance(int goodsClass) {
        PagerFragment fragment = new PagerFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(GOODS_CLASS,goodsClass);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = new CheapPresenter(this);
        if (getArguments() != null) {

            goodsClass = getArguments().getInt(GOODS_CLASS);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.base_recycler_layout, container, false);
        uiFirstInit=true;

        initView(view);
        initRefreshLayout();

        return view;
    }
    private void initRefreshLayout() {
        mRefreshLayout.setDelegate(this);

        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        refreshViewHolder = new BGANormalRefreshViewHolder(getContext(), true);

        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        mRefreshLayout.setPullDownRefreshEnable(false);

    }


    @Override
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);

        mLayoutManager = new GridLayoutManager(getContext(), 2);

        mRecycler.setLayoutManager(mLayoutManager);


        mRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();

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
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(PagerFragment.this.getContext()).resumeRequests();

                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Glide.with(PagerFragment.this.getContext()).pauseRequests();

                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {

                }
            }
        });


        mAdapter = new CheapRecyclerAdapter();
        mAdapter.setOnItemClieck(this);

        mRecycler.setAdapter(mAdapter);
        mPresenter.loadGoods(null,null,goodsClass,null,currentPage,pageSize,null,null);

    }




    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        lazyFetchDataIfPrepared();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        uiFirstInit =false;

    }


   /* private void lazyFetchDataIfPrepared(){
        if (getUserVisibleHint() && uiFirstInit) {
            mPresenter.loadGoods(null,null,goodsClass,null,currentPage,pageSize,null,null);
        }
    }*/
    @Override
    protected void setMessageState(int state) {

    }


    @OnClick(R.id.baseRecyclerView)
    public void onViewClicked() {
    }



    @Override
    public void onLoadGoodsByKeyword(List<GoodsModel> model) {

        if (mAdapter.getItemCount() > 0 && model != null && model.isEmpty()) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_more), Toast.LENGTH_SHORT).show();
        }
        if (mRefreshLayout != null && mRefreshLayout.isLoadingMore()) {
            mRefreshLayout.endLoadingMore();
        }
        if (model!=null && !model.isEmpty()) {
            mAdapter.setDatas(model);
            currentPage++;
        }

    }

    @Override
    public void onStartLoad() {
        if (getUserVisibleHint() && !mRefreshLayout.isLoadingMore()) {

            mLoadDialog.show();
        }
        if (listener != null) {
            listener.start();

        }
    }

    @Override
    public void onCategoryErrorOrComplete() {
        mLoadDialog.dismiss();
        if (listener != null) {
            listener.errorOrComplete();
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        mPresenter.loadGoods(null,null,goodsClass,null,currentPage,pageSize,null,null);
        return true;
    }

    @Override
    public void onGoodsClickListener(Parcelable parcelablel) {
//        ActivityUtils.showDetailView(getActivity(),id,null, url);
        ActivityUtils.showDetailView(getActivity(), parcelablel);


    }

    @Override
    public void onFindUnread(boolean msg) {

    }

    private LoadStateListener listener;

    public void setListener(LoadStateListener listener) {
        this.listener = listener;
    }

    public interface LoadStateListener{
         void start();
        void errorOrComplete();

    }
}
