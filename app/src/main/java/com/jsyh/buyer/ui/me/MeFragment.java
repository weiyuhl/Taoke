package com.jsyh.buyer.ui.me;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.TradeResult;
import com.bumptech.glide.Glide;
import com.jsyh.buyer.BaseFragment;
import com.jsyh.buyer.MainActivity;
import com.jsyh.buyer.R;
import com.jsyh.buyer.adapter.MeRecyclerAdapter;
import com.jsyh.buyer.model.BaseModel;
import com.jsyh.buyer.model.MeMenuModel;
import com.jsyh.buyer.model.PersonModel;
import com.jsyh.buyer.model.ShareAppModel;
import com.jsyh.buyer.ui.feedback.FeedbackActivity;
import com.jsyh.buyer.ui.iview.MeView;
import com.jsyh.buyer.ui.me.kefu.KeFuActivity;
import com.jsyh.buyer.ui.presenter.MePresenter;
import com.jsyh.buyer.utils.ActivityUtils;
import com.jsyh.buyer.utils.AlibcUtils;
import com.jsyh.buyer.utils.ResourcesUtil;
import com.jsyh.buyer.utils.StateBarUitl;
import com.jsyh.buyer.utils.SystemBarHelper;
import com.jsyh.buyer.utils.image.GlideCircleTransform;
import com.jsyh.buyer.widget.decoration.MeDecoration;
import com.momo.library.share.ShareManger;
import com.momo.library.share.ShareType;
import com.momo.library.share.qq.TencetShare;
import com.momo.library.share.sina.SinaShare;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mo on 17-4-1.
 */

public class MeFragment extends BaseFragment implements MeView, AlibcTradeCallback
        , MeRecyclerAdapter.OnItemClick {


    private MePresenter mePresenter;

    @BindView(R.id.stateBarHeight)
    View mStateBarHeight;

    @BindView(R.id.meHeadLayout)
    RelativeLayout mHeadLayout;

    @BindView(R.id.meRecycler)
    RecyclerView mRecycler;

    @BindView(R.id.loginAndNick)
    TextView loginAndNick;      //登录 名称

    @BindView(R.id.headImage)
    ImageView headImage;

    private MeRecyclerAdapter mAdapter;
    private List<MeMenuModel> data = new ArrayList<>();
    private AlibcUtils alibcUtils;


    private boolean hasUnread;  ///有未读消息


    public BaseModel<ShareAppModel> shareData;

    private AlertDialog alertDialog;

    public MeFragment() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mStateColor = ContextCompat.getColor(context, R.color.meHeadTopColor);

        alertDialog = new AlertDialog.Builder(getContext())
                .setMessage("是否注销淘宝")
                .setPositiveButton("注销", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mePresenter.logout(getActivity());
                    }
                })
                .setNegativeButton("取消", null)
                .create();


        if (AlibcLogin.getInstance().isLogin()) {

            data.add(new MeMenuModel("注销淘宝", R.drawable.me_taobao));
        } else {

            data.add(new MeMenuModel("淘宝授权", R.drawable.me_taobao));
        }
        data.add(new MeMenuModel("淘宝购物车", R.drawable.me_cat));
        data.add(new MeMenuModel("关注", R.drawable.me_follow));
        data.add(new MeMenuModel("设置", R.drawable.me_setting));
        data.add(new MeMenuModel("客服", R.drawable._me_cs));
        data.add(new MeMenuModel("意见反馈", R.drawable.me_feedback));
        mAdapter = new MeRecyclerAdapter(context, data);
        mAdapter.setOnItemClick(this);

        mePresenter = new MePresenter(this);

        alibcUtils = new AlibcUtils();

    }

    public static MeFragment newInstance() {

        MeFragment fragment = new MeFragment();

        return fragment;
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

        SystemBarHelper.setHeightAndPadding(getActivity(), mStateBarHeight);


        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setAutoMeasureEnabled(true);

        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(mAdapter);


        mRecycler.addItemDecoration(new MeDecoration(ContextCompat.getColor(getContext(), R.color.meDiverColor)));
        initData();
        mePresenter.getShareWithCustomer();

    }

    @Override
    protected void setMessageState(int state) {

    }

    void initData() {
        if (AlibcLogin.getInstance().isLogin()) {
            mePresenter.getUserInfo();
        }
    }

    @OnClick({R.id.loginAndNick, R.id.noPayLayout, R.id.noSendLayout, R.id.noGetLayout, R.id.allOrderLayout})
    public void onViewClicked(View view) {

        if (!AlibcLogin.getInstance().isLogin()) {
            //没有登录
            mePresenter.login(getActivity());
            return;
        }
        switch (view.getId()) {

            case R.id.noPayLayout:
                showOrderByType(AlibcUtils.order_no_pay);
                break;
            case R.id.noSendLayout:
                showOrderByType(AlibcUtils.order_no_send);
                break;
            case R.id.noGetLayout:
                showOrderByType(AlibcUtils.order_no_get);
                break;
            case R.id.allOrderLayout:
                showOrderByType(AlibcUtils.order_all);
                break;
        }
    }


    @OnClick(R.id.openMsg)
    void onOpenMesgView(View view) {
        if (shareData != null) {
            if (ResourcesUtil.getBooleanId(getContext(), "has_share") > 0) {
                if (getResources().getBoolean(ResourcesUtil.getBooleanId(getContext(), "has_share"))) {

                    ((MainActivity) getActivity()).showShare();
                } else {
                    Toast.makeText(getContext(), R.string.has_share_tips, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (SystemBarHelper.isMIUI6Later() || SystemBarHelper.isFlyme4Later() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SystemBarHelper.setStatusBarDarkMode(getActivity());
                SystemBarHelper.tintStatusBar(getActivity(), mStateColor, 0);
            } else {
                mStateColor = getResources().getColor(R.color.kitkatStateBarColor);
                SystemBarHelper.tintStatusBar(getActivity(), mStateColor, 0);
            }
        }

        mePresenter.findMessage();
    }

    private void showOrderByType(int orderType) {
        alibcUtils.showOrderByType(getActivity(), orderType, this);
    }


    //======================================view=======================

    @Override
    public void onLoginSuccess(PersonModel model) {

        mAdapter.getDatas().get(0).name = "注销淘宝";
        mAdapter.notifyDataSetChanged();

        loginAndNick.setText(model.getNick());  // 设置昵称
        loginAndNick.setEnabled(false); //登录成功不能在点击

        Glide.with(this).load(model.getAvatarUrl())
                .transform(new GlideCircleTransform(getContext()))
                .into(headImage);
        Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLoginFailure(int code, String msg) {

        Toast.makeText(getActivity(), "登录失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLogoutSuccess() {
        Toast.makeText(getActivity(), "已退出", Toast.LENGTH_SHORT).show();

        mAdapter.getDatas().get(0).name = "淘宝授权";
        mAdapter.notifyDataSetChanged();

        loginAndNick.setText("登录注销");
        Glide.with(this).load(R.drawable.me_head).into(headImage);

    }

    @Override
    public void onLogoutFailure(int code, String msg) {

    }

    @Override
    public void onShareData(BaseModel<ShareAppModel> model) {
        shareData = model;
        MainActivity.introduction = model.getData().getShareIntroduce();
        MainActivity.shareTitle = model.getData().getShareTitle();
    }

    //======================trade回调===================================
    @Override
    public void onTradeSuccess(TradeResult tradeResult) {

    }

    @Override
    public void onFailure(int i, String s) {

    }

    @Override
    public void onItemClickListener(int position) {
        switch (position) {

            case 0:
                if (!AlibcLogin.getInstance().isLogin()) {

                    mePresenter.login(getActivity());
                } else {
                    alertDialog.show();
                }
                break;
            case 1:
                //购物车
                if (AlibcLogin.getInstance().isLogin()) {

                    alibcUtils.showCart(getActivity());
                } else {
                    mePresenter.login(getActivity());

                }
                break;

            case 2:
                Toast.makeText(getActivity(), "开发中", Toast.LENGTH_SHORT).show();
                break;

            case 3:
                // 设置

                ActivityUtils.showSettingView(getActivity());

                break;

            case 4:
                if (shareData == null || shareData.getData() == null || shareData.getData().getKefu().isEmpty()) {
                    return;
                }
                Intent kefuIntent = new Intent(getActivity(), KeFuActivity.class);
                kefuIntent.putExtra("data", shareData.getData());
                startActivity(kefuIntent);

                break;
            case 5:
                if (AlibcLogin.getInstance().isLogin()) {

                    //意见反馈
                    Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                    startActivity(intent);
                } else {
                    mePresenter.login(getActivity());
                }


                break;
        }
    }


    @Override
    public void onFindUnread(boolean msg) {
        hasUnread = msg;


    }


}
