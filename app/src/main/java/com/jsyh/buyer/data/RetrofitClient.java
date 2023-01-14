package com.jsyh.buyer.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jsyh.buyer.model.AdvertisingModel;
import com.jsyh.buyer.model.BaseModel;
import com.jsyh.buyer.model.GoodsListModel;
import com.jsyh.buyer.model.GoogsCategoryModel;
import com.jsyh.buyer.model.ShareAppModel;
import com.jsyh.buyer.model.SharePicModel;
import com.jsyh.buyer.model.UpdataModel;
import com.jsyh.buyer.utils.L;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mo on 17-4-7.
 */

public class RetrofitClient {

    private Retrofit mRetrofit;
    private OkHttpClient mOkClient;

    private static volatile RetrofitClient instance;

    private RetrofitClient() {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();


        if (L.isPrint) {
            builder.addInterceptor(interceptor);
        }
        mOkClient = builder.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkClient)
                .build();
    }


    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }


    public <T> T createApi(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }


    public ServiceApi getFixApid() {
        return mRetrofit.create(ServiceApi.class);
    }


    public Observable<BaseModel<UpdataModel>> update(String downloadUrl,String appid,String version) {

        Observable<BaseModel<UpdataModel>> observable =  /*RetrofitClient.getInstance().*/getFixApid().updataApp(downloadUrl,appid,version)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());


        return observable;


    }

    public Observable<BaseModel<List<AdvertisingModel>>> getAdv(String type) {
        Observable<BaseModel<List<AdvertisingModel>>> observable = /*RetrofitClient.getInstance().*/getFixApid()
                .getAdv(type)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        return observable;
    }

    public Observable<BaseModel<List<GoogsCategoryModel>>> getGoodsClass(Integer recommend) {
        Observable<BaseModel<List<GoogsCategoryModel>>> observable = getFixApid().getGoodsClass(recommend)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        return observable;
    }


    public Observable<BaseModel<GoodsListModel>> getGoods(Double startPrice,
                                                          Double endPrice,
                                                          Integer goodsClass,
                                                          Integer isRecommend,
                                                          Integer page,
                                                          Integer pageCount,
                                                          String keyword,
                                                          String order,
                                                          String goodsId,
                                                          Integer like) {
        Observable<BaseModel<GoodsListModel>> observable = getFixApid().getGoods(startPrice,
                endPrice, goodsClass, isRecommend, page, pageCount, keyword, order,goodsId,like)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        return observable;
    }

    public Observable<BaseModel<SharePicModel>> getSharePic(Integer goodsId) {
        Observable<BaseModel<SharePicModel>> sharePicObservable = getFixApid().getSharePicUrl(goodsId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io());

        return sharePicObservable;
    }


    public Observable<ResponseBody> getSharePicByte(String url) {

        Observable<ResponseBody> observable = getFixApid().getSharePicByte(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        return observable;


    }

 /*   public Observable<ResponseBody> getApk(String url) {
        Observable<ResponseBody> observable = getFixApid().getApk(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


        return observable;

    }*/


    public void handleObservale(Observable observable) {

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<BaseModel<ShareAppModel>> getShareData() {

        Observable<BaseModel<ShareAppModel>> observable = getFixApid().getShareData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }


}
