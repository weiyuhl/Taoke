package com.jsyh.buyer.ui.iview;

import com.jsyh.buyer.base.BaseView;
import com.jsyh.buyer.model.AdvertisingModel;
import com.jsyh.buyer.model.GoodsModel;
import com.jsyh.buyer.model.GoogsCategoryModel;
import com.jsyh.buyer.ui.presenter.HomePresenter;

import java.util.List;

/**
 *
 *  首页
 * Created by mo on 17-4-10.
 */

public interface HomeView extends BaseView {

    void onLoadAdv(List<AdvertisingModel> models);

    void onLoadGoodsClass(List<GoogsCategoryModel> models);

    void onLoadGoods(int totalSize , List<GoodsModel> models);

    void onStartGoods();

    void onErrorOrComplete();






}
