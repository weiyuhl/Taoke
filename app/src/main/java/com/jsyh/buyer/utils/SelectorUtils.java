package com.jsyh.buyer.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;


/**
 * Created by Administrator on 2015/11/12.
 */
public class SelectorUtils {
    /**
     * 对TextView设置不同状态时其文字颜色。
     */
    public static ColorStateList createColorStateList(int normal, int checked) {


        int[] colors = new int[] {
                checked,
                checked,
                checked,
                normal };
        int[][] states = new int[4][];
        states[0] = new int[] { android.R.attr.state_pressed };
        states[1] = new int[] { android.R.attr.state_selected};
        states[2] = new int[] { android.R.attr.state_checked};
        states[3] = new int[] {};

        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    public static ColorStateList createColorStateList(Context context, @ColorRes int normal, @ColorRes int checked) {

        ContextCompat.getColor(context, normal);

        int[] colors = new int[] {
                ContextCompat.getColor(context, checked),
                ContextCompat.getColor(context, checked),
                ContextCompat.getColor(context, checked),
                ContextCompat.getColor(context, normal) };
        int[][] states = new int[4][];
        states[0] = new int[] { android.R.attr.state_pressed };
        states[1] = new int[] { android.R.attr.state_selected};
        states[2] = new int[] { android.R.attr.state_checked};
        states[3] = new int[] {};

        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }



    public static StateListDrawable generatorDrawableState(Context context,String norIcon,String checkedIcon) {

        StateListDrawable stateListDrawable = new StateListDrawable();

        Drawable defDrawable = context.getResources().getDrawable(ResourcesUtil.getMipmapId(context, norIcon));
        Drawable checkDrawable = context.getResources().getDrawable(ResourcesUtil.getMipmapId(context, checkedIcon));

        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, checkDrawable);

        stateListDrawable.addState(new int[] { android.R.attr.state_pressed }, checkDrawable);

        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, checkDrawable);

        stateListDrawable.addState(new int[]{}, defDrawable);

        return stateListDrawable;
    }

    public static StateListDrawable generatorDrawableState(Context context,int norIcon,int checkedIcon) {

        StateListDrawable stateListDrawable = new StateListDrawable();


        Drawable defDrawable = ContextCompat.getDrawable(context,norIcon);

        Drawable checkDrawable = ContextCompat.getDrawable(context,checkedIcon);

        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, checkDrawable);

        stateListDrawable.addState(new int[] { android.R.attr.state_pressed }, checkDrawable);

        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, checkDrawable);

        stateListDrawable.addState(new int[]{}, defDrawable);

        return stateListDrawable;
    }



}
