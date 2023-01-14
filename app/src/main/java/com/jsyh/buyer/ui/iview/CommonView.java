package com.jsyh.buyer.ui.iview;

import android.graphics.Bitmap;

import com.jsyh.buyer.R;
import com.jsyh.buyer.model.GoodsModel;
import com.jsyh.buyer.model.GoogsCategoryModel;

import java.util.List;

/**
 * Created by mo on 17-4-18.
 */

public interface CommonView {

    void onLoadGoods(List<GoodsModel> model);

    void onStartGoods();
    void onLoadGoodsErrorOrComplete();

    void onLoadCategory(List<GoogsCategoryModel> model);

    void onLoadCategoryError();

    void onFindUnreadMsg(boolean msg);


    void onLoadSharePicUlr(String url);

    void onLoadShareBitmap(Bitmap bitmap);

}
