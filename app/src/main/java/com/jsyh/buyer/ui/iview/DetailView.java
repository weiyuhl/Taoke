package com.jsyh.buyer.ui.iview;

import android.graphics.Bitmap;

import com.jsyh.buyer.base.BaseView;

import butterknife.BindView;

/**
 * Created by mo on 17-4-21.
 */

public interface DetailView extends BaseView{

    void onLoadSharePicUlr(String url);

    void onLoadShareBitmap(Bitmap bitmap);

}
