package com.jsyh.buyer.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.TypedValue;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by mo on 17-3-31.
 */

public class GlideRoundTransform extends BitmapTransformation{

    private static float radius = 0f;


    public GlideRoundTransform(Context context,int radiusDP) {
        this(context);
        radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                radiusDP, context.getResources().getDisplayMetrics());

    }
    public GlideRoundTransform(Context context) {
        super(context);
    }

    public GlideRoundTransform(BitmapPool bitmapPool) {
        super(bitmapPool);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool,toTransform);
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());

        canvas.drawRoundRect(rectF, radius, radius, paint);
        return result;
    }



}
