package com.jsyh.buyer.ui.iview;

import com.jsyh.buyer.base.BaseView;
import com.jsyh.buyer.model.GoodsModel;

import java.util.List;

/**
 * 白菜价 page view
 *
 * Created by mo on 17-4-18.
 */

public interface CheapPageView extends BaseView{


    public void onLoadGoodsByKeyword(List<GoodsModel> mode);

    void onStartLoad();

    public void onCategoryErrorOrComplete();


}
