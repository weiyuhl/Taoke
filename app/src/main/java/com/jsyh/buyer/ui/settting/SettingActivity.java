package com.jsyh.buyer.ui.settting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsyh.buyer.BaseActivity;
import com.jsyh.buyer.R;
import com.jsyh.buyer.ui.about.AboutActivity;
import com.jsyh.buyer.utils.GlideCacheUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mo on 17-4-25.
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.cacheSize)
    TextView mCacheText;

    @BindView(R.id.back)
    ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initCache();
    }


    private void initCache() {
        String cacheSize = GlideCacheUtil.getInstance().getCacheSize(this);

        if ("0.0Byte".equals(cacheSize)) {
            return;
        }

        mCacheText.setText(cacheSize);
    }

    @Override
    protected int getRootView() {
        return R.layout.setting_layout;
    }

    @OnClick({R.id.about, R.id.clearCache,R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);

                break;
            case R.id.clearCache:
                GlideCacheUtil.getInstance().clearImageDiskCache(this);
                mCacheText.setText("");

                break;



            case R.id.back:
                finish();
                break;
        }
    }




}
