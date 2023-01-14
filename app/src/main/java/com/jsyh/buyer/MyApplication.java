package com.jsyh.buyer;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.jsyh.buyer.data.Api;
import com.jsyh.buyer.utils.L;
import com.jsyh.buyer.utils.ResourcesUtil;
import com.jsyh.buyer.utils.SPUtils;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by mo on 17-4-10.
 */

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public void onCreate() {
        super.onCreate();

        initBugly();


        if (getResources().getIdentifier("api_host", "string", getPackageName()) <=0) {
            System.exit(0);
        }


        Api.getInstance(getApplicationContext());

        if (ResourcesUtil.getBooleanId(getApplicationContext(), "has_jpush") > 0) {

            if (getResources().getBoolean(ResourcesUtil.getBooleanId(getApplicationContext(), "has_jpush"))) {

                //极光实例化
                JPushInterface.setDebugMode(true);
                JPushInterface.init(this);
            }
        }

        Logger.init("tag");

        initShareLauncher();

        //电商SDK初始化
        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                AlibcTradeSDK.setForceH5(true);

                // 是否使用支付宝
                AlibcTradeSDK.setShouldUseAlipay(true);


            }

            @Override
            public void onFailure(int code, String msg) {
//                Toast.makeText(MyApplication.this, "初始化失败,错误码="+code+" / 错误消息="+msg, Toast.LENGTH_SHORT).show();
                L.e("初始化失败,错误码=" + code + " / 错误消息=" + msg);
            }
        });
    }

    private void initBugly() {
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());

        strategy.setAppVersion(getResources().getString(R.string.inside_version_code));
        strategy.setAppPackageName(getPackageName());
        CrashReport.initCrashReport(getApplicationContext(), "5936ffd0e0", false,strategy);
    }


    public void initShareLauncher() {

        String cachePath;
        cachePath = (String) SPUtils.get(this, "logoPath", "");
        if (!TextUtils.isEmpty(cachePath)) {
            File file = new File(cachePath);
            if (file.exists()) {

                //已经存在
                return;
            }
        }

        cachePath = getExternalCacheDir().getAbsolutePath();
        if (TextUtils.isEmpty(cachePath)) {
            cachePath = getCacheDir().getPath();
        }


        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        options.outfile = cachePath;

        Tiny.getInstance().source(R.mipmap.ic_launcher).asFile().withOptions(options).compress(new FileCallback() {
            @Override
            public void callback(boolean isSuccess, String outfile) {
                Log.e("tag", outfile);
                SPUtils.put(MyApplication.this,"logoPath",outfile);
            }
        });

    }
    /*
       * 保存文件
       */
    private static void saveFile(Bitmap bm, String path) throws IOException {

        File myCaptureFile = new File(path );
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }

}
