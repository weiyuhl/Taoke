package com.jsyh.buyer.ui.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jsyh.buyer.base.BasePresenter;
import com.jsyh.buyer.data.RetrofitClient;
import com.jsyh.buyer.model.BaseModel;
import com.jsyh.buyer.model.SharePicModel;
import com.jsyh.buyer.ui.iview.DetailView;
import com.jsyh.buyer.utils.L;

import java.io.IOException;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Created by mo on 17-4-21.
 */

public class DetailPresenter extends BasePresenter{


    private DetailView view;
    public DetailPresenter(DetailView view) {
        super();
        this.view = view;
    }


    public void sharePic(Integer id) {
        RetrofitClient.getInstance().getSharePic(id)
                .flatMap(new Function<BaseModel<SharePicModel>, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(@NonNull BaseModel<SharePicModel> model) throws Exception {

                        view.onLoadSharePicUlr(model.getData().getUrl());
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

                    view.onLoadShareBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    view.onLoadShareBitmap(null);

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

    @Override
    public void unsubscribe() {
        disposable.clear();
    }
}
