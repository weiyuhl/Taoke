package com.jsyh.buyer.utils;

import android.os.Build;
import android.text.TextUtils;

/**
 * Created by mo on 17-4-5.
 */

public class DevicesUtil {

    /**
     * 手机厂商名字
     * @return
     */
    public static String getManufacturer() {
        String brand = Build.BRAND;
        String manufacturer = Build.MANUFACTURER;
       return manufacturer;

    }

    /**
     * 小米os
     * @return
     */
    public static boolean isMIUIOs() {
        if (!TextUtils.isEmpty(getManufacturer()) && "Xiaomi".equalsIgnoreCase(getManufacturer())) {

            return true;
        }
        return false;

    }

    /**
     * 魅族os
     * @return
     */
    public static boolean isFlymeOs() {
        if (!TextUtils.isEmpty(getManufacturer()) && "Flyme".equalsIgnoreCase(getManufacturer())) {

            return true;
        }
        return false;

    }

}
