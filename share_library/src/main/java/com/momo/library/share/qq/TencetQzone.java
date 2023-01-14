package com.momo.library.share.qq;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.momo.library.R;
import com.momo.library.share.ShareType;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

import static com.tencent.connect.share.QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;

/**
 * Created by mo on 17-4-20.
 */

public class TencetQzone extends TencetShare {
    private Tencent tencent;

    public TencetQzone(Activity activity) {
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

    public Tencent getTencent() {
        return tencent;
    }

    @Override
    public void share(Activity activity, Bitmap bitmap) {

        /*Bundle params = new Bundle();
        //分享类型
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, SHARE_TO_QZONE_TYPE_IMAGE_TEXT);

        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "标题");//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "摘要");//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://www.baidu.com");//必填
        ArrayList<String> images = new ArrayList<>();
        images.add("http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
        tencent.shareToQzone(activity, params, new IUiListener() {
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
            Log.e("tag", "TencetQzone url is null ---");
            return;
        }

        final Bundle params = new Bundle();

        if (url.endsWith(".png")) {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        } else {
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);

        }

        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePath);
        if (!TextUtils.isEmpty(title)) {

            params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        }
        if (!TextUtils.isEmpty(introduction)) {

            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, introduction);
        }

        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,"tbk");


        if (!TextUtils.isEmpty(url) && !url.endsWith(".png")) {
            if (url.startsWith("www.")) {
                Toast.makeText(activity, "应用分享网址格式错误", Toast.LENGTH_SHORT).show();
                return;
            }
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imagePath);
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        }

        tencent.shareToQQ(activity, params, listener);
    }

    @Override
    public void share(Activity activity, Bitmap bitmap, String url,String title, String introduction) {

        final Bundle params = new Bundle();


    }

    /**
     * 获取应用程序名称
     */
    private String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
