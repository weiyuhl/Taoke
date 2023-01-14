package com.jsyh.buyer.widget.decoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by mo on 17-4-6.
 */

public class MeDecoration extends RecyclerView.ItemDecoration {


    private int color;

    public MeDecoration(int color) {
        this.color = color;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        super.onDraw(c, parent, state);
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }


    private void drawHorizontal(Canvas canvas, RecyclerView parent) {


        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {

            final View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();


            final int left = child.getLeft();
            final int top = child.getHeight()+child.getTop();

            final int right = child.getRight() + Math.round(ViewCompat.getTranslationX(child));
            int bottom = top + 1;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(color);


            if (i < 3) {
                canvas.drawRect(left, top, right, bottom, paint);

            }

        }

    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {

            final View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int left = child.getRight();
            int top = child.getTop();
            int right = left + 1;
            int bottom = child.getBottom();


            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

            paint.setColor(color);
            canvas.drawRect(left, top, right, bottom, paint);
        }

    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        int index = params.getSpanIndex();
        int position = params.getViewLayoutPosition();

        if (position < 3) {
            if (index < 2) {
                outRect.right = 1;
                outRect.bottom = 1;
            }
            if (index == 2) {
                outRect.bottom = 1;
            }

        } else {
            if (index < 2) {
                outRect.right = 1;
            }
        }
    }


}
