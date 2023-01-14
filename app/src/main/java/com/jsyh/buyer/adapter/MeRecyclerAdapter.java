package com.jsyh.buyer.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsyh.buyer.R;
import com.jsyh.buyer.model.MeMenuModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mo on 17-4-6.
 */

public class MeRecyclerAdapter extends RecyclerView.Adapter<MeRecyclerAdapter.MeMunuHolder>{

    private List<MeMenuModel> datas;

    private Context context;

    private OnItemClick onItemClick;


    public MeRecyclerAdapter(Context context,List<MeMenuModel> datas) {
        this.context = context;
        this.datas = datas;
    }

    public List<MeMenuModel> getDatas() {
        return datas;
    }

    @Override
    public MeMunuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.me_menu_item, parent, false);



        return new MeMunuHolder(view);
    }

    @Override
    public void onBindViewHolder(MeMunuHolder holder, final int position) {
        MeMenuModel model = datas.get(position);

        Drawable drawable = ContextCompat.getDrawable(context, model.drawableId);

        holder.item.setText(model.name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            holder.item.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable,null,null);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.onItemClickListener(position);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return datas!=null?datas.size():0;
    }

    static class MeMunuHolder extends RecyclerView.ViewHolder {

        TextView item;
        public MeMunuHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.meItem);
        }
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick{
        void onItemClickListener(int position);
    }


}
