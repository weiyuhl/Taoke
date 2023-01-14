package com.jsyh.buyer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import com.jsyh.buyer.ui.detail.DetailActivity;
import com.jsyh.buyer.ui.detail.NativeDetailActivity;
import com.jsyh.buyer.ui.message.MessageActivity;
import com.jsyh.buyer.ui.settting.SettingActivity;

/**
 * Created by mo on 17-4-19.
 */

public class ActivityUtils {

    /**
     * 商品详情
     * @param activity
     * @param iid
     * @param url
     */
    public static void showDetailView(Activity activity , String imageId, String iid,String url,String title,String introduction) {

        if (TextUtils.isEmpty(url) && TextUtils.isEmpty(iid)) {
            L.e("参数错误"+activity.toString());
            return;
        }
        Intent intent = new Intent(activity, DetailActivity.class);
        Bundle extra = new Bundle();
        extra.putString("imageId", imageId);
        extra.putString("iid", iid);
        extra.putString("url",url);
        extra.putString("title",title);
        extra.putString("introduction",introduction);
        intent.putExtras(extra);
        activity.startActivity(intent);
    }

    /**
     * 商品详情
     * @param activity
     */
    public static void showDetailView(Activity activity , Parcelable parcelable) {

        Intent intent = new Intent(activity, NativeDetailActivity.class);
        Bundle extra = new Bundle();
        extra.putParcelable("data",parcelable);
        intent.putExtras(extra);
        activity.startActivity(intent);
    }

    public static void showMessageView(Context context) {
        Intent intent = new Intent(context, MessageActivity.class);

        context.startActivity(intent);
    }


    public static void showSettingView(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);

        activity.startActivity(intent);
    }
}
