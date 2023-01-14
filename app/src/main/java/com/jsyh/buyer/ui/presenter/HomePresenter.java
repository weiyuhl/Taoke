package com.jsyh.buyer.ui.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.jsyh.buyer.base.BasePresenter;
import com.jsyh.buyer.data.Api;
import com.jsyh.buyer.data.RetrofitClient;
import com.jsyh.buyer.data.local.MsgDao;
import com.jsyh.buyer.model.AdvertisingModel;
import com.jsyh.buyer.model.BaseModel;
import com.jsyh.buyer.model.GoodsListModel;
import com.jsyh.buyer.model.GoogsCategoryModel;
import com.jsyh.buyer.ui.home.HomeFragment;
import com.jsyh.buyer.ui.iview.HomeView;
import com.jsyh.buyer.utils.L;

import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by mo on 17-4-10.
 */

public class HomePresenter extends BasePresenter {

    private HomeView mView;
    private MsgDao dao;


    public HomePresenter(HomeView view) {
        super();
        mView = view;
        dao = new MsgDao(((HomeFragment) view).getContext());
    }


    public void loadAdv() {
        RetrofitClient.getInstance().getAdv(Api.APP_INDEX)
                .subscribe(new Observer<BaseModel<List<AdvertisingModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                        mView.onStartGoods();
                    }

                    @Override
                    public void onNext(BaseModel<List<AdvertisingModel>> model) {
                        if (model.getCode() == Api.OK) {
                            mView.onLoadAdv(model.getData());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e(e.toString());
                        mView.onErrorOrComplete();
                    }

                    @Override
                    public void onComplete() {
                        mView.onErrorOrComplete();
                    }
                });

    }


    public void loadCategory() {
        RetrofitClient.getInstance().getGoodsClass(1)
                .subscribe(new Observer<BaseModel<List<GoogsCategoryModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(BaseModel<List<GoogsCategoryModel>> model) {
                        if (model.getCode() == Api.OK) {
                            if (model.getData().size() >= 4) {

                                mView.onLoadGoodsClass(model.getData().subList(0, 4));
                            }else {
                                mView.onLoadGoodsClass(model.getData());
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e(e.toString());
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
                    }

                    @Override
                    public void onNext(BaseModel<GoodsListModel> model) {
                        if (Api.OK == model.getCode()) {
                            int totalSize =0;
                            if (model.getData()!=null) {
                                if (!TextUtils.isEmpty(model.getData().getTotalResults())) {
                                    totalSize = Integer.parseInt(model.getData().getTotalResults());
                                }
                            }
                            mView.onLoadGoods(totalSize,model.getData().getGoods());
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


    public void findUnreadMessage() {
        mView.onFindUnread(dao.findUnread());
    }



    @Override
    public void unsubscribe() {
        disposable.clear();
    }
}
