package com.momo.library.share;

import android.app.Activity;
import android.graphics.Bitmap;

/**
 * Created by mo on 17-4-20.
 */

public abstract class ShareType {


    protected ShareType(Activity activity) {

    }

    public abstract void share(Activity activity, Bitmap bitmap);


    public abstract void share(Activity activity, Bitmap bitmap, String url, String title, String introduction);
}
