package com.jsyh.buyer.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsyh.buyer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 上图片 下文字 holder
 */

public class ImageAndTextHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.homeHorizontalItem)
    public
    ImageView imageView;
    @BindView(R.id.textView3)
    public TextView name;

    public ImageAndTextHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }
}
