package com.jsyh.buyer.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;
import java.io.IOException;

/**
 * Created by mo on 17-4-26.
 */

public class AppUtils {


    /**
     * app是否存在
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /*
   * 取得应用的版本号,就是哪个版本,
   */
    public static String longVersionName(Context context) {
        String mVersion = null;
        if (mVersion == null) {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi;
            try {
                pi = pm.getPackageInfo(context.getPackageName(), 0);
                mVersion = pi.versionName;

            } catch (PackageManager.NameNotFoundException e) {
                mVersion = ""; // failed, ignored
            }
        }
        return mVersion;
    }

    /*
     * 取得应用的版本号,就是修改次.
     */
    public static int longVersionCode(Context context) {
        int mVersionCode = 0;
        if (mVersionCode == 0) {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi;
            try {
                pi = pm.getPackageInfo(context.getPackageName(), 0);
                mVersionCode = pi.versionCode;

            } catch (PackageManager.NameNotFoundException e) {
                mVersionCode = 0; // failed, ignored
            }
        }
        return mVersionCode;
    }

    public static String getAppPackageName(Context context) {
        return context.getPackageName();

    }


    // 获取屏幕密度（方法1）
    public static int screenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int screenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = wm.getDefaultDisplay().getHeight();      // 屏幕高（像素，如：800p）
        return screenHeight;
    }

    /**
     * 安装APK
     *
     * @param file
     */
    public static void installAPK(Context context,File file) {

        /*Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.fromFile(t),
                "application/vnd.android.package-archive");
        context.startActivity(i);*/
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
            Uri data = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", file);
            intent.setDataAndType(data, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        context.startActivity(intent);
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
