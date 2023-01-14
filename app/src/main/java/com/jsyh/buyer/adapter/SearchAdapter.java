package com.jsyh.buyer.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsyh.buyer.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索
 * Created by mo on 17-4-18.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    private List<String> datas = new ArrayList<>();

    private OnItemClickLinstener itemClickLinstener;

    public void setItemClickLinstener(OnItemClickLinstener itemClickLinstener) {
        this.itemClickLinstener = itemClickLinstener;
    }

    public SearchAdapter() {
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void setDatas(String name) {
        this.datas.add(name);
        notifyDataSetChanged();
    }

    public void clear() {
        this.datas.clear();
        notifyDataSetChanged();
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);


        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {


        final String keyword = datas.get(position);
        holder.tvName.setText(keyword);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickLinstener != null) {
                    itemClickLinstener.onIntemClickListener(keyword);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class SearchHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;


        public SearchHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickLinstener{
        void onIntemClickListener(String keyword);
    }


}
