package com.momo.library;

import android.app.Activity;
import android.text.TextUtils;

/**
 * Created by mo on 17-5-9.
 */

public class ShareCheckUtils {


    public static boolean hasSinaConfig(Activity activity) {
        int appkeyIdentifier = activity.getResources().getIdentifier("wb_app_key", "string", activity.getPackageName());
        int redirectIdentifier = activity.getResources().getIdentifier("wb_redirect_url", "string", activity.getPackageName());


        if (appkeyIdentifier <= 0 || redirectIdentifier <= 0) {

            return false;
        }

        String strAppkey = activity.getResources().getString(appkeyIdentifier);
        String strRedirect = activity.getResources().getString(redirectIdentifier);

        if (TextUtils.isEmpty(strAppkey) || TextUtils.isEmpty(strRedirect)) {
            return false;
        }

        return true;
    }

    public static boolean hasWxConfig(Activity activity) {
        int wxIndentifier = activity.getResources().getIdentifier("wx_app_key", "string", activity.getPackageName());
        if (wxIndentifier <= 0) {
            return false;
        }

        String strAppkey = activity.getString(wxIndentifier);
        if (TextUtils.isEmpty(strAppkey)) {
            return false;
        }
        return true;
    }

    public static boolean hasTenceConfig(Activity activity) {
        int tencentIndentifier = activity.getResources().getIdentifier("tencent", "string", activity.getPackageName());
        if (tencentIndentifier <= 0) {
            return false;
        }

        String strTencent = activity.getString(tencentIndentifier);
        if (TextUtils.isEmpty(strTencent)) {
            return false;
        }

        return true;
    }


}
