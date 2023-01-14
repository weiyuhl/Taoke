package com.jsyh.buyer.ui.about;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jsyh.buyer.BaseActivity;
import com.jsyh.buyer.R;
import com.jsyh.buyer.utils.AppUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.tvVersionNum)
    TextView versionNum;

    @BindView(R.id.tvAppName)
    TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int identifier = getResources().getIdentifier("my_app_name", "string", getPackageName());
        if (identifier > 0) {
            String appNameStr = getResources().getString(identifier);
            appName.setText(appNameStr);
        }

        String longVersionName = AppUtils.longVersionName(this);
        versionNum.setText("版本号" + longVersionName);
    }

    @Override
    protected int getRootView() {
        return R.layout.about_activity;
    }


    @OnClick(R.id.back)
    void onBack() {
        finish();
    }
}
