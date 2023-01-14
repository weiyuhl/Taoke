package com.jsyh.buyer.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;

/**
 * Created by mo on 17-4-6.
 */

public class MeMenuModel {

    public String name;

    public int drawableId;

    public MeMenuModel(String name,  int drawableId) {
        this.name = name;
        this.drawableId = drawableId;
    }
}
