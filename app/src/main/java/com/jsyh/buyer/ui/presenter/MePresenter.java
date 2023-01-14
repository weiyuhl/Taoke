package com.jsyh.buyer.ui.presenter;

import android.app.Activity;

import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.login.callback.LogoutCallback;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.jsyh.buyer.base.BasePresenter;
import com.jsyh.buyer.data.RetrofitClient;
import com.jsyh.buyer.data.local.MsgDao;
import com.jsyh.buyer.model.BaseModel;
import com.jsyh.buyer.model.PersonModel;
import com.jsyh.buyer.model.ShareAppModel;
import com.jsyh.buyer.ui.iview.MeView;
import com.jsyh.buyer.ui.me.MeFragment;
import com.jsyh.buyer.utils.L;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by mo on 17-4-19.
 */

public class MePresenter extends BasePresenter {


    private MeView mView;

    private MsgDao dao;


    public MePresenter(MeView view) {
        super();
        mView = view;
        dao = new MsgDao(((MeFragment) view).getContext());
    }



    public void login(Activity activity) {

        AlibcLogin.getInstance().showLogin(activity, new AlibcLoginCallback() {
            @Override
            public void onSuccess() {
                Session session = AlibcLogin.getInstance().getSession();
                mView.onLoginSuccess(new PersonModel(session.nick, session.avatarUrl, session.openId, session.openSid));
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.onLoginFailure(code, msg);
            }
        });


    }



    public void getUserInfo() {
        if (AlibcLogin.getInstance().isLogin()) {
            Session session = AlibcLogin.getInstance().getSession();
            mView.onLoginSuccess(new PersonModel(session.nick, session.avatarUrl, session.openId, session.openSid));
        }
    }
    public void logout(Activity activity) {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();

        alibcLogin.logout(activity, new LogoutCallback() {
            @Override
            public void onSuccess() {
                mView.onLogoutSuccess();
            }

            @Override
            public void onFailure(int code, String msg) {
                mView.onLoginFailure(code, msg);

            }
        });
    }


    public void getShareWithCustomer() {
        RetrofitClient.getInstance()
                .getShareData()
                .subscribe(new Observer<BaseModel<ShareAppModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseModel<ShareAppModel> model) {
                        mView.onShareData(model);

                    }

                    @Override
                    public void onError(Throwable e) {
                        L.d(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void findMessage() {
        mView.onFindUnread(dao.findUnread());
    }

    @Override
    protected void unsubscribe() {

    }
}
