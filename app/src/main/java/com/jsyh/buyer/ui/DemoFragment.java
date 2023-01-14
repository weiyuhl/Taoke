package com.jsyh.buyer.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsyh.buyer.BaseFragment;
import com.jsyh.buyer.R;
import com.jsyh.buyer.utils.StateBarUitl;

import butterknife.ButterKnife;

/**
 * Created by mo on 17-5-3.
 */

public class DemoFragment extends BaseFragment{



    public static DemoFragment newInstance(){

        DemoFragment fragment = new DemoFragment();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStateColor = ContextCompat.getColor(context, R.color.meHeadTopColor);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.me_fragment_layout, container, false);
            ButterKnife.bind(this, rootView);
            initView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setMessageState(int state) {

    }

    @Override
    public void onResume() {
        super.onResume();
//            StateBarUitl.changeStateBarColor(getActivity(), mStateColor);
    }
}
