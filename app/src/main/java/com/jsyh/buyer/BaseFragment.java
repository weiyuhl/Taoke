package com.jsyh.buyer;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsyh.buyer.ui.detail.DetailActivity;
import com.jsyh.buyer.ui.message.MessageActivity;
import com.jsyh.buyer.utils.ActivityUtils;
import com.jsyh.buyer.utils.ConvertUtils;
import com.jsyh.buyer.utils.DevicesUtil;
import com.jsyh.buyer.widget.LoadDialog;

import butterknife.OnClick;

/**
 * Created by mo on 17-3-30.
 */

public abstract class BaseFragment extends Fragment {

    protected View rootView;

    protected int mStateColor;
    protected LoadDialog mLoadDialog;

    protected boolean uiVisible;
    protected boolean uiFirstInit;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLoadDialog = new LoadDialog(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    protected abstract void initView(View view);






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mLoadDialog != null) {
            mLoadDialog.dismiss();
        }
    }


    protected boolean canChangeStateFont() {

        if (Build.VERSION.SDK_INT >= 23 || DevicesUtil.isMIUIOs() || DevicesUtil.isFlymeOs()) {
            return true;
        }
        return false;
    }

    protected float getDIP(@DimenRes int id) {

        return getResources().getDimensionPixelSize(id);
    }


    protected void openMessage() {
        ActivityUtils.showMessageView(getContext());

    }


    //设置消息状态
    protected abstract void setMessageState(int state);


}
