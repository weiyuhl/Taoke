package com.momo.library.share.qq;

import android.app.Activity;

import com.momo.library.share.ShareType;
import com.tencent.tauth.IUiListener;

/**
 * Created by mo on 17-4-24.
 */

public abstract class TencetShare extends ShareType {

    protected TencetShare(Activity activity) {
        super(activity);
    }


    public abstract void share(Activity activity,
                               String imagePath,
                               String url,
                               String title,
                               String introduction,
                               IUiListener listener);
}
