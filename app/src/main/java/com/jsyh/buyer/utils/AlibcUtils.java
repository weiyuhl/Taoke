package com.jsyh.buyer.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.constants.AlibcConstants;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcMyCartsPage;
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mo on 17-4-19.
 */

public class AlibcUtils {

    private int orderType = 0;//订单页面参数，仅在H5方式下有效
    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private Map<String, String> exParams;//yhhpass参数


    public static final int order_all = 0;//全部
    public static final int order_no_pay=1;//待付款
    public static final int order_no_send = 2;//待发货
    public static final int order_no_get=3; //待收货
    public static final int order_no_Evaluation=3; //待评价



    public AlibcUtils() {

        alibcShowParams = new AlibcShowParams(OpenType.Auto, false);

        exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
    }


    public void showOrderByType(Activity activity,int orderType,AlibcTradeCallback alibcTradeCallback) {
        AlibcBasePage alibcBasePage = new AlibcMyOrdersPage(orderType, true);
        AlibcTrade.show(activity, alibcBasePage, alibcShowParams, null, exParams, alibcTradeCallback);
    }

    /**
     * 显示我的购物车
     */
    public void showCart(Activity activity) {

        AlibcBasePage alibcBasePage = new AlibcMyCartsPage();
        AlibcTrade.show(activity, alibcBasePage, alibcShowParams, null, exParams, new DemoTradeCallback(activity));
    }
}
