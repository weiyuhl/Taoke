package com.jsyh.buyer.ui.home;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jsyh.buyer.BaseFragment;
import com.jsyh.buyer.MainActivity;
import com.jsyh.buyer.R;
import com.jsyh.buyer.adapter.HomeAdapter;
import com.jsyh.buyer.model.AdvertisingModel;
import com.jsyh.buyer.model.GoodsModel;
import com.jsyh.buyer.model.GoogsCategoryModel;
import com.jsyh.buyer.ui.iview.HomeView;
import com.jsyh.buyer.ui.presenter.HomePresenter;
import com.jsyh.buyer.ui.search.SearchActivity;
import com.jsyh.buyer.utils.ActivityUtils;
import com.jsyh.buyer.utils.ConvertUtils;
import com.jsyh.buyer.utils.StateBarUitl;
import com.jsyh.buyer.utils.SystemBarHelper;
import com.stx.xhb.xbanner.XBanner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;


public class HomeFragment extends BaseFragment implements HomeView
        , HomeAdapter.OnItemClieck
        , BGARefreshLayout.BGARefreshLayoutDelegate
        , BGARefreshLayout.BGARefreshScaleDelegate, BGARefreshLayout.HiddenRefreshAnimatorListener {

    private Handler handler = new Handler();

    @BindView(R.id.refresLayout)
    BGARefreshLayout mRefreshLayout;

    private BGARefreshViewHolder refreshViewHolder;

    @BindView(R.id.homeList)
    RecyclerView mHomeList;
    private GridLayoutManager layoutManager;

    private Unbinder unbinder;

    private HomeAdapter mHomeAdapter;

    @BindView(R.id.barLayout)
    LinearLayout mBarLayout;

    @BindView(R.id.searchLayout)
    LinearLayout mSearchLayout;     // 搜索布局

    @BindView(R.id.searchRight)
    ImageView mMessage;

    private int currentPage = 1;        //当前页
    private int pageCount = 50;
    private int currentDataSize;
    private boolean isMoreData = true;

    private HomePresenter presenter;

    private boolean newMsg;
    private boolean isAlpha = true;


    DiffUtil diffUtil = null;


    public HomeFragment() {
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStateColor = Color.argb(0, 255, 255, 255);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new HomePresenter(this);

        if (getArguments() != null) {

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
            rootView = inflater.inflate(R.layout.home_fragment, container, false);
            initView(rootView);

        }

        return rootView;
    }

    @Override
    protected void setMessageState(int state) {
        if (state == -1) {
            this.newMsg = true;
        }
        if (mMessage != null) {
            setMessageTipImageByState();
        }
    }

    @Override
    protected void initView(View view) {

        unbinder = ButterKnife.bind(this, view);


        SystemBarHelper.immersiveStatusBar(getActivity(), 0);
        SystemBarHelper.setHeightAndPadding(getActivity(), mBarLayout);

        mHomeAdapter = new HomeAdapter(getContext());
        mHomeAdapter.setOnItemClieck(this);

        layoutManager = new GridLayoutManager(getContext(), 4);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type = mHomeList.getAdapter().getItemViewType(position);
                switch (type) {
                    case 1:

                        return 4;
                    case 2:
                        return 1;

                    case 3:

                        return 2;
                }

                return 0;
            }
        });

        mHomeList.addItemDecoration(new RecyclerView.ItemDecoration() {


            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

                GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();
                int spanSize = params.getSpanSize();
                int spanIndex = params.getSpanIndex();
                int viewAdapterPosition = params.getViewAdapterPosition();

                if (spanSize == 4) {
                    //banner
                }
                if (spanSize == 1) {
                    //中间
                    outRect.top = (int) getDIP(R.dimen.homeCategoryTop);
                    outRect.bottom = (int) getDIP(R.dimen.homeCategoryBottom);

                   /* if (spanIndex == 0) {
                        outRect.left = (int) getDIP(R.dimen.homeCategoryLR);
                    } else if (spanIndex == 3) {
                        outRect.right = (int) getDIP(R.dimen.homeCategoryLR);
                    }*/
                }
                if (spanSize == 2) {
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


            }
        });

        mHomeList.setAdapter(mHomeAdapter);
        mHomeList.setLayoutManager(layoutManager);

        mHomeList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(HomeFragment.this.getContext()).resumeRequests();

                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Glide.with(HomeFragment.this.getContext()).pauseRequests();

                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {

                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                View childAt = layoutManager.getChildAt(firstVisibleItemPosition);

                if (childAt != null && ((ViewGroup) childAt) instanceof XBanner) {
                    int height = ((ViewGroup) childAt).getHeight();
                    int scrollOffset = mHomeList.computeVerticalScrollOffset();

                    try {
                        if (scrollOffset <= height) {
                            int p = scrollOffset / (height / 255);
                            if (scrollOffset >= (height / 2)) {
                                isAlpha = false;

                            } else {
                                isAlpha = true;
                            }
                            setMessageTipImageByState();
                            if (p <= 255) {
                                mBarLayout.setBackgroundColor(Color.argb(p, 255, 255, 255));
                                if (canChangeStateFont()) {
                                    mStateColor = Color.argb(p, 255, 255, 255);
                                    StateBarUitl.changeLollipopStateBarColor(getActivity(), mStateColor);
                                    StateBarUitl.setStatusBarFontDark(getActivity(), true);
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        });
        initRefreshLayout();


        presenter.loadAdv();
        presenter.loadCategory();
        presenter.loadGoods(null, null, null, null, currentPage, pageCount, null, null);

    }

    private void initRefreshLayout() {
        mRefreshLayout.setDelegate(this);

        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        refreshViewHolder = new BGANormalRefreshViewHolder(getContext(), true);


        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        mRefreshLayout.setRefreshScaleDelegate(this);
        mRefreshLayout.setRefreshAnimatorListener(this);


    }

    private void setMessageTipImageByState() {

        if (newMsg && isAlpha) {
            //有新消息 ，并且 透明
            mMessage.setImageDrawable(ContextCompat
                    .getDrawable(HomeFragment.this.getContext(), R.drawable.message_white_state));
        } else if (newMsg && !isAlpha) {
            mMessage.setImageDrawable(ContextCompat
                    .getDrawable(HomeFragment.this.getContext(), R.drawable.message_state));
        } else if (!newMsg && isAlpha) {
            mMessage.setImageDrawable(ContextCompat
                    .getDrawable(HomeFragment.this.getContext(), R.drawable.message_white));
        } else if (!newMsg && !isAlpha) {
            mMessage.setImageDrawable(ContextCompat
                    .getDrawable(HomeFragment.this.getContext(), R.drawable.message));
        }


    }

    @OnClick(R.id.searchLayout)
    void searchClick(View view) {

        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
    }


    @OnClick({R.id.searchRight})
    void onViewClick(View view) {
        switch (view.getId()) {

            case R.id.searchRight:
                JPushInterface.clearAllNotifications(getActivity());

                openMessage();
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mHomeList != null && layoutManager != null) {
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            View childAt = layoutManager.getChildAt(firstVisibleItemPosition);

            if (childAt != null && ((ViewGroup) childAt) instanceof XBanner) {
                ((XBanner) childAt).startAutoPlay();
            }
        }

        /*if (canChangeStateFont()) {

            StateBarUitl.changeLollipopStateBarColor(getActivity(), mStateColor);
            StateBarUitl.setStatusBarFontDark(getActivity(), true);
        } else {
            mStateColor = getResources().getColor(R.color.kitkatStateBarColor);

            StateBarUitl.changeStateBarColor(getActivity(), mStateColor);
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (SystemBarHelper.isMIUI6Later() || SystemBarHelper.isFlyme4Later() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SystemBarHelper.setStatusBarDarkMode(getActivity());
                SystemBarHelper.tintStatusBar(getActivity(), mStateColor, 0);
            } else {
                mStateColor = getResources().getColor(R.color.kitkatStateBarColor);
                SystemBarHelper.tintStatusBar(getActivity(), mStateColor, 0);
            }
        }

        presenter.findUnreadMessage();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mHomeList != null && layoutManager != null) {
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            View childAt = layoutManager.getChildAt(firstVisibleItemPosition);

            if (childAt != null && ((ViewGroup) childAt) instanceof XBanner) {
                ((XBanner) childAt).stopAutoPlay();
            }
        }

        presenter.unsubscribe();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onFindUnread(boolean newMsg) {

        this.newMsg = newMsg;
        if (mMessage != null) {
            setMessageTipImageByState();
        }
    }

    @Override
    public void onLoadAdv(List<AdvertisingModel> models) {
        mHomeAdapter.setAdvertising(models);

    }

    @Override
    public void onLoadGoodsClass(List<GoogsCategoryModel> models) {
        mHomeAdapter.setCategorys(models);
    }

    @Override
    public void onLoadGoods(int totalSize, List<GoodsModel> models) {

        if (isMoreData) {
            if (!models.isEmpty()) {
                currentPage++;
                currentDataSize += models.size();
            }
            mHomeAdapter.setGoods(models);
            if (mRefreshLayout.isLoadingMore()) {
                mRefreshLayout.endLoadingMore();
            }
            if (currentDataSize == totalSize && models.isEmpty()) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_more), Toast.LENGTH_SHORT).show();
            }
        }else {
            mHomeAdapter.setGoods(models);
            mRefreshLayout.endRefreshing();
            isMoreData = true;
        }
    }

    @Override
    public void onStartGoods() {
        mLoadDialog.show();
    }

    @Override
    public void onErrorOrComplete() {
        mLoadDialog.dismiss();
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(final BGARefreshLayout refreshLayout) {

        isMoreData = false;
        currentPage = 1;
        pageCount = 50;

        mHomeAdapter = new HomeAdapter(getContext());
        mHomeList.setAdapter(mHomeAdapter);
        presenter.loadAdv();
        presenter.loadCategory();
        presenter.loadGoods(null, null, null, null, currentPage, pageCount, null, null);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {

        presenter.loadGoods(null, null, null, null, currentPage, pageCount, null, null);


        return true;
    }


    @Override
    public void onRefreshScaleChanged(float scale, int moveYDistance) {
        if (moveYDistance > 0) {
            mBarLayout.setVisibility(View.GONE);
        }
    }

    //隐藏下拉刷新动画 过度值
    @Override
    public void offsetTopPadding(int offset) {

        if (refreshViewHolder.getRefreshHeaderViewHeight() == Math.abs(offset)) {
            mBarLayout.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onBanner(String id, String url, String title, String introduction) {
        ActivityUtils.showDetailView(getActivity(), id, null, url, title, introduction);
    }

    //========================recycler item click ============================
    @Override
    public void onGoodsClickListener(Parcelable parcelable) {
        ActivityUtils.showDetailView(getActivity(), parcelable);
    }

    @Override
    public void onCategoryClickListener(String name) {


        ((MainActivity) getActivity()).showCategory(name);

    }


}
