package com.jsyh.buyer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsyh.buyer.R;
import com.jsyh.pushlibrary.MessageModel;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mo on 17-4-19.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {



    private List<MessageModel> data = new ArrayList<>();


    public void setData(List<MessageModel> data) {
        if (data != null) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        data.clear();

        notifyDataSetChanged();
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);

        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        MessageModel model = data.get(position);
//
        holder.time.setText(model.getTime());
        holder.content.setText(model.getContent());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MessageHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView)
        TextView time;
        @BindView(R.id.tvMessageContent)
        TextView content;

        public MessageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

