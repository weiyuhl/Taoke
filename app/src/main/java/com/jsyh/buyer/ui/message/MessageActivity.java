package com.jsyh.buyer.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.jsyh.buyer.BaseActivity;
import com.jsyh.buyer.BaseFragment;
import com.jsyh.buyer.R;
import com.jsyh.buyer.adapter.MessageAdapter;
import com.jsyh.buyer.data.local.MsgDao;
import com.jsyh.pushlibrary.MessageModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class MessageActivity extends BaseActivity {

    @BindView(R.id.messageRecycler)
    RecyclerView recyclerView;


    private MessageAdapter mAdapter;
    private LinearLayoutManager layoutManager;


    private MsgDao dao;
    private List<MessageModel> datas = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = new MsgDao(this);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new MessageAdapter();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        loadMessage();
    }

    @Override
    protected int getRootView() {
        return R.layout.message_activity;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        JPushInterface.clearAllNotifications(this);
    }

    @OnClick(R.id.back)
    public void onBack(View view) {
        finish();
    }


    @OnClick(R.id.clearAll)
    void clearMessage() {
        JPushInterface.clearAllNotifications(this);
        if (dao.deletAll() >0) {
            mAdapter.clearData();
        }
    }

    private void loadMessage() {
        List<MessageModel> temp = dao.findAll();
        if (temp != null) {
            datas.addAll(temp);
        }
        mAdapter.setData(datas);

        dao.update(datas);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void jpushMsgCallback(MessageModel model) {

        MsgDao dao = new MsgDao(this);
        model.setRead(0);
        dao.add(model);
        List<MessageModel> temp = new ArrayList<>();

        temp.add(model);

        mAdapter.setData(temp);
        JPushInterface.clearAllNotifications(this);



    }



}
