package com.jsyh.buyer.utils;

import android.util.Log;

import com.jsyh.buyer.BuildConfig;
import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2016/11/14.
 */

public class L {

    public static boolean isPrint = true;
    public static void d(String msg) {
        if (isPrint) {

            Logger.d(msg);
        }
    }

    public static void e(String msg) {
        if (isPrint)
            Logger.e(msg);
    }


    public static void ii(String msg) {
        if (isPrint)
            Log.d("tag", msg);
    }

    public static void dd(String msg) {
        if (isPrint)
            Log.d("tag", msg);
    }

    public static void ee(String msg) {
        if (isPrint)
            Log.d("tag", msg);
    }
}
