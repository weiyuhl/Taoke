package com.momo.library;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.momo.library.share.ShareManger;

import java.util.ArrayList;
import java.util.List;

/**
 * 分享弹框
 * Created by mo on 17-4-20.
 */

public class ShareDialog extends Dialog{

    private List<ShareModel> shares = new ArrayList<>();
    private ShareAdapter shareAdapter;
    private ShareManger.OnShareItemClickListener onShareItemClickListener;

    public ShareDialog(@NonNull Context context) {
        super(context,R.style.BottomDialog);
        init(context);
    }


    public ShareDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected ShareDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }


    private void init(Context context) {

        RecyclerView recyclerView = new RecyclerView(context);
        RecyclerView.LayoutParams params =
                new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        recyclerView.setLayoutParams(params);
        recyclerView.setBackgroundColor(Color.WHITE);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 4);
        layoutManager.setAutoMeasureEnabled(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(shareAdapter= new ShareAdapter());


        setContentView(recyclerView);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


    }



    public void setShares(List<ShareModel> shares) {
        if (!this.shares.isEmpty()) {
            this.shares.clear();
        }
        this.shares.addAll(shares);
        shareAdapter.notifyDataSetChanged();
    }

    class ShareAdapter extends RecyclerView.Adapter<ShareHolder> {
        @Override
        public ShareHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_item, parent, false);

            return new ShareHolder(view);
        }

        @Override
        public void onBindViewHolder(ShareHolder holder, int position) {
            final ShareModel model = shares.get(position);

            holder.view.setText(model.getName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                holder.view.setCompoundDrawablesRelativeWithIntrinsicBounds(null, model.getDrawable(), null, null);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onShareItemClickListener != null) {
                        onShareItemClickListener.onShareItemClickListener(model.getShareType());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return shares.size();
        }
    }



    class ShareHolder extends RecyclerView.ViewHolder{

        TextView view;
        public ShareHolder(View itemView) {
            super(itemView);
            view = (TextView) itemView.findViewById(R.id.shareItem);
        }
    }

    public void setOnShareItemClickListener(ShareManger.OnShareItemClickListener onShareItemClickListener) {
        this.onShareItemClickListener = onShareItemClickListener;
    }


}
