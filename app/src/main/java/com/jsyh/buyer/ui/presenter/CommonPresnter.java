package com.jsyh.buyer.ui.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.jsyh.buyer.base.BasePresenter;
import com.jsyh.buyer.data.Api;
import com.jsyh.buyer.data.RetrofitClient;
import com.jsyh.buyer.model.BaseModel;
import com.jsyh.buyer.model.GoodsListModel;
import com.jsyh.buyer.model.GoogsCategoryModel;
import com.jsyh.buyer.model.SharePicModel;
import com.jsyh.buyer.ui.iview.CommonView;
import com.jsyh.buyer.utils.L;

import java.io.IOException;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Created by mo on 17-4-18.
 */

public class CommonPresnter extends BasePresenter {

    private CommonView mView;


    public CommonPresnter(CommonView mView) {
        super();
        this.mView = mView;

    }

    public CommonPresnter(Context context, CommonView view) {
        this(view);
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
                            mView.onLoadCategory(model.getData());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onLoadCategoryError();
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
                          String order,
                          String goodsId,
                          Integer like) {
        RetrofitClient.getInstance().getGoods(startPrice, endPrice, goodsClass, isRecommend, page, pageCount, keyword, order
        ,goodsId,like)
                .subscribe(new Observer<BaseModel<GoodsListModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                        mView.onStartGoods();
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
                            mView.onLoadGoods(model.getData().getGoods());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onLoadGoodsErrorOrComplete();
                    }

                    @Override
                    public void onComplete() {
                        mView.onLoadGoodsErrorOrComplete();
                    }
                });

    }



    public void sharePic(Integer id) {

        RetrofitClient.getInstance().getSharePic(id)
                .flatMap(new Function<BaseModel<SharePicModel>, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(@NonNull BaseModel<SharePicModel> model) throws Exception {

                        mView.onLoadSharePicUlr(model.getData().getUrl());
                        return RetrofitClient.getInstance().getSharePicByte(model.getData().getUrl());
                    }
                }).subscribe(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    byte[] bytes = responseBody.bytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    mView.onLoadShareBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    mView.onLoadShareBitmap(null);

                }
            }

            @Override
            public void onError(Throwable e) {
                L.d("------ onError");
            }

            @Override
            public void onComplete() {
                L.d("------ onComplete");
            }
        });
    }
    public void findUnreadMessage() {
//        mView.onFindUnreadMsg(dao.findUnread());

    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }


}
