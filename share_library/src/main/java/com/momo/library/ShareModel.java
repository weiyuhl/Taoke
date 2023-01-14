package com.momo.library;

import android.graphics.drawable.Drawable;

import com.momo.library.share.ShareType;

/**
 * Created by mo on 17-4-20.
 */

public class ShareModel {

    private String name;
    private Drawable drawable;

    private ShareType shareType;

    public ShareModel() {
    }

    public ShareModel(String name, Drawable drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    public ShareModel(String name, Drawable drawable, ShareType shareType) {
        this.name = name;
        this.drawable = drawable;
        this.shareType = shareType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public ShareType getShareType() {
        return shareType;
    }

    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
    }
}
