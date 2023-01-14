package com.jsyh.buyer.ui.presenter;

import android.text.TextUtils;

import com.jsyh.buyer.base.BasePresenter;
import com.jsyh.buyer.data.Api;
import com.jsyh.buyer.data.RetrofitClient;
import com.jsyh.buyer.data.local.MsgDao;
import com.jsyh.buyer.model.BaseModel;
import com.jsyh.buyer.model.GoodsListModel;
import com.jsyh.buyer.model.GoogsCategoryModel;
import com.jsyh.buyer.ui.cheap.CheapFragment;
import com.jsyh.buyer.ui.iview.CheapPageView;
import com.jsyh.buyer.ui.iview.CheapView;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by mo on 17-4-17.
 */

public class CheapPresenter extends BasePresenter {

    private MsgDao dao;
    private CheapView mView;

    private CheapPageView mPageView;

    public CheapPresenter(CheapView view) {
        super();
        mView = view;
        dao = new MsgDao(((CheapFragment) view).getContext());

    }

    public CheapPresenter(CheapPageView pageView) {
        super();
        this.mPageView = pageView;
    }

    public void loadCategory() {
        RetrofitClient.getInstance().getGoodsClass(null)
                .subscribe(new Observer<BaseModel<List<GoogsCategoryModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(BaseModel<List<GoogsCategoryModel>> model) {
                        if (model.getCode() == Api.OK) {

                            mView.onLoadGoodsCategory(model.getData());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void loadGoods(Double startPrice,
                          Double endPrice,
                          Integer goodsClass,
                          Integer isRecommend,
                          Integer page,
                          Integer pageCount,
                          String keyword,
                          String order) {
        RetrofitClient.getInstance().getGoods(startPrice, endPrice, goodsClass, isRecommend, page, pageCount, keyword, order,null,null)
                .subscribe(new Observer<BaseModel<GoodsListModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                        mPageView.onStartLoad();
                    }

                    @Override
                    public void onNext(BaseModel<GoodsListModel> model) {
                        if (Api.OK == model.getCode()) {
                            int totalSize = 0;
                            if (model.getData() != null) {
                                if (!TextUtils.isEmpty(model.getData().getTotalResults())) {
                                    totalSize = Integer.parseInt(model.getData().getTotalResults());
                                }
                            }
                            if (mView != null) {

                                mView.onLoadGoodsByKey(model.getData().getGoods());
                            }

                            if (mPageView != null) {
                                mPageView.onLoadGoodsByKeyword(model.getData().getGoods());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mPageView.onCategoryErrorOrComplete();
                    }

                    @Override
                    public void onComplete() {
                        mPageView.onCategoryErrorOrComplete();
                    }
                });

    }

    public void findUnreadMessage() {
       mView.onFindUnread(dao.findUnread());

    }


    @Override
    protected void unsubscribe() {

    }
}
