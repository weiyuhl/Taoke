package com.jsyh.buyer.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jsyh.buyer.MainActivity;
import com.jsyh.buyer.R;
import com.jsyh.buyer.ui.WebActivity;
import com.jsyh.buyer.service.ImageCacheService;
import com.jsyh.buyer.utils.NetworkUtils;
import com.jsyh.buyer.utils.SPUtils;
import com.jsyh.buyer.utils.SystemBarHelper;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splashImage)
    ImageView splashImage;
    @BindView(R.id.delyTime)
    TextView mTextTime;

    //广告时间
    private long time = 3;


    String imageCache;
    private Unbinder unbinder;

    private Observable<Long> observable;
    private Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_activity);
        SystemBarHelper.immersiveStatusBar(this, 0);


        unbinder = ButterKnife.bind(this);
        String adbName = (String) SPUtils.get(this,"advName","");

        File file = null;
        if (!TextUtils.isEmpty(adbName)) {
            imageCache = getExternalCacheDir().getAbsolutePath()+File.separator+adbName;

            file = new File(imageCache);
        }


        time = (long) SPUtils.get(this, "adv_time", new Long(3));

        if (file != null &&
                file.exists()
                && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Glide.with(this).load(file).centerCrop().into(splashImage);
            splashImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String advUrl = (String) SPUtils.get(SplashActivity.this, "adv_url", "");
                    if (!TextUtils.isEmpty(advUrl)) {
                        disposable.dispose();

                        String advTitle = (String) SPUtils.get(SplashActivity.this, "adv_title", "");
                        Bundle extra = new Bundle();
                        extra.putString("url", advUrl);
                        extra.putString("title", advTitle);
                        extra.putInt("type", WebActivity.adb_type);
                        Intent webIntent = new Intent(SplashActivity.this, WebActivity.class);
                        webIntent.putExtras(extra);
                        startActivity(webIntent);
                        finish();
                    }
                }
            });


        }

        //下载图片并判断是否更新图片
        if (NetworkUtils.isNetworkAvailable(this) != 0) {

            final Intent intent = new Intent(this, ImageCacheService.class);

            startService(intent);
        }


        forword();
    }


   /* @Override
    protected int getRootView() {
        return R.layout.splash_activity;
    }*/


    private void forword() {

        observable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(time)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return time - aLong.intValue();
                    }
                });


        observable.subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable =d;
            }

            @Override
            public void onNext(Long aLong) {
                if (aLong != null) {

                    mTextTime.setText(aLong + "秒");
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
