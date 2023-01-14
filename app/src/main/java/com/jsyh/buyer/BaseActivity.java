package com.jsyh.buyer;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jsyh.buyer.utils.DevicesUtil;
import com.jsyh.buyer.widget.LoadDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Created by mo on 17-3-30.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected int mStateColor;

protected LoadDialog mLoadDialog;

    protected boolean isForeground;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getRootView());
        ButterKnife.bind(this);
        mLoadDialog = new LoadDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isForeground = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isForeground =false;
        mLoadDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoadDialog.dismiss();

    }

    protected abstract @LayoutRes int getRootView();




    protected boolean canChangeStateFont() {

        if (Build.VERSION.SDK_INT >= 23|| DevicesUtil.isMIUIOs() || DevicesUtil.isFlymeOs()) {
            return true;
        }
        return false;
    }
    protected float getDIP(@DimenRes int id) {
        return getResources().getDimension(id);
    }

}
