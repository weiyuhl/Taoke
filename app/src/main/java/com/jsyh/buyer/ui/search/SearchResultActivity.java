package com.jsyh.buyer.ui.search;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.jsyh.buyer.BaseActivity;
import com.jsyh.buyer.R;
import com.jsyh.buyer.data.local.MsgDao;
import com.jsyh.buyer.model.GoodsModel;
import com.jsyh.buyer.model.GoogsCategoryModel;
import com.jsyh.buyer.ui.baoyou.BaoYouFragment;
import com.jsyh.buyer.ui.iview.CommonView;
import com.jsyh.pushlibrary.MessageModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class SearchResultActivity extends BaseActivity implements CommonView{


    private String keyword;

   /* @BindView(R.id.recyclerView)
    RecyclerView mRecycler;
    @BindView(R.id.refresLayout)
    BGARefreshLayout mRefresLayout;

    private CommonPresnter mPresenter;
    private CommodityAdapter mAdapter;
    private GridLayoutManager layoutManager;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        keyword = getIntent().getStringExtra("keyword");

//        mPresenter.loadGoods(null,null,null,null,null,null,keyword,null);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content,BaoYouFragment.newInstance(keyword),"search")
                .commit();


    }

    @Override
    protected int getRootView() {
        return R.layout.search_result_activity;
    }

    //============================view=============================
    @Override
    public void onLoadGoods(List<GoodsModel> model) {
//        mAdapter.setDatas(model);
    }

    @Override
    public void onStartGoods() {

    }

    @Override
    public void onLoadGoodsErrorOrComplete() {

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


}
