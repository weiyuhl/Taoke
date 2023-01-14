package com.jsyh.buyer.ui.feedback;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.jsyh.buyer.BaseActivity;
import com.jsyh.buyer.R;
import com.jsyh.buyer.data.RetrofitClient;
import com.jsyh.buyer.model.BaseModel;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.content)
    EditText mContent;

    private String mContentString;

    private Disposable mDisposable = new CompositeDisposable();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getRootView() {
        return R.layout.feedback_activity;
    }

    @OnClick(R.id.submit)
    public void onViewClicked() {

        mContentString = mContent.getText().toString();
        if (TextUtils.isEmpty(mContentString)) {
            Toast.makeText(this, "请输入返回内容", Toast.LENGTH_SHORT).show();

            return;
        }
        if (mContentString.length() > 300) {
            Toast.makeText(this, "请输入300字符以内", Toast.LENGTH_SHORT).show();
            return;
        }
        submit(mContentString);

    }

    @OnClick(R.id.back)
    public void back(View view) {
        finish();
    }



    public void submit(String description) {

        String userName = "";
        if (AlibcLogin.getInstance().isLogin()) {
            userName = AlibcLogin.getInstance().getSession().nick;
        }
        RetrofitClient.getInstance().getFixApid()
                .feedback(description, userName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(BaseModel baseModel) {
                        if (baseModel != null && baseModel.getCode() == 200) {
                            Toast.makeText(FeedbackActivity.this, "意见反馈成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }




}
