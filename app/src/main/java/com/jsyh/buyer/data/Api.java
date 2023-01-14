package com.jsyh.buyer.data;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by mo on 17-4-7.
 */

public class Api {

    private static volatile Api instance;

    private Api(Context context) {
        int identifier = context.getResources().getIdentifier("api_host", "string", context.getPackageName());
        String url = context.getResources().getString(identifier);

        if (!TextUtils.isEmpty(url) && !url.endsWith("\\")) {
            url += "\\";
            BASE_URL = url;
        }else {
            BASE_URL = url;
        }

        int updateIdentifier = context.getResources().getIdentifier("api_update", "string", context.getPackageName());
        if (updateIdentifier <= 0) {
            Toast.makeText(context, "没有配置升级接口", Toast.LENGTH_SHORT).show();
        } else {
            UPDATE_URL = context.getResources().getString(updateIdentifier);
        }
    }

    public static Api getInstance(Context context) {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new Api(context);
                }
            }
        }
        return instance;
    }

    public static String BASE_URL;
    public static String UPDATE_URL;

    public static int OK = 200;
    public static int FAIL = 500;

    public static String APP_START = "app_start"; //启动图广告
    public static String APP_INDEX = "app_index";//首页广告


    public static String SUCCESS_CODE = "200";
    public static String ERROR_CODE = "0";


}
