package com.jsyh.buyer.ui.iview;

import com.jsyh.buyer.base.BaseView;
import com.jsyh.buyer.model.GoodsModel;
import com.jsyh.buyer.model.GoogsCategoryModel;

import java.util.List;

/**
 * 白菜
 * by mo on 17-4-17.
 */

public interface CheapView extends BaseView {

    void onLoadGoodsCategory(List<GoogsCategoryModel> model);

    void onLoadCategoryError();

    void onLoadGoodsByKey(List<GoodsModel> mode);

    void onLoadGoddsErrorWithComplete();


}
