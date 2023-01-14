package com.momo.library.share.qq;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

/**
 * Created by mo on 17-4-20.
 */

public class TencenQ extends TencetShare {


    private Tencent tencent;

    public TencenQ(Activity activity) {
        super(activity);
        int tencentIndentifier = activity.getResources().getIdentifier("tencent", "string", activity.getPackageName());
        if (tencentIndentifier <= 0) {
            return;
        }

        String strTencent = activity.getString(tencentIndentifier);
        if (TextUtils.isEmpty(strTencent)) {
            return;
        }
        tencent = Tencent.createInstance(strTencent, activity.getApplicationContext());
    }

    //注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_SUMMARY不能全为空，最少必须有一个是有值的。

    @Override
    public void share(Activity activity, Bitmap bitmap) {
       /* final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
//        params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
//        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  "要分享的摘要");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "http://www.qq.com/news/1.html");
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "测试应用222222");
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  "其他附加功能");
        tencent.shareToQQ(activity, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {

            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });*/
    }

    public void share(Activity activity,
                      String imagePath,
                      String url,
                      String title,
                      String introduction,
                      IUiListener listener) {

        if (tencent == null) {
            Toast.makeText(activity, "QQ分享配置错误", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(url)) {
            Log.e("tag", "TencetQ url is null ---");
            return;
        }
        final Bundle params = new Bundle();

        if (url.endsWith(".png")) {

            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        } else {

            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        }

        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePath);

        if (TextUtils.isEmpty(title)) {
            title = "";
        }
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  introduction);
        if (!TextUtils.isEmpty(url) && !url.endsWith(".png")) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imagePath);
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        }


        tencent.shareToQQ(activity, params, listener);
    }

    @Override
    public void share(Activity activity, Bitmap bitmap, String url,String title, String introduction) {
        final Bundle params = new Bundle();

    }
}
