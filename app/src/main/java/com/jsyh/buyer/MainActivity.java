package com.jsyh.buyer;

import android.Manifest;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.ali.auth.third.ui.context.CallbackContext;
import com.jsyh.buyer.ui.baoyou.BaoYouFragment;
import com.jsyh.buyer.ui.cheap.CheapFragment;
import com.jsyh.buyer.ui.home.HomeFragment;
import com.jsyh.buyer.ui.me.MeFragment;
import com.jsyh.buyer.utils.PermissionConstance;
import com.jsyh.buyer.utils.ResourcesUtil;
import com.jsyh.buyer.utils.SPUtils;
import com.jsyh.buyer.utils.SelectorUtils;
import com.jsyh.buyer.utils.UpdateHelper;
import com.jsyh.buyer.widget.MyFragmentTabHost;
import com.jsyh.pushlibrary.MessageModel;
import com.momo.library.ShareCheckUtils;
import com.momo.library.share.ShareManger;
import com.momo.library.share.ShareType;
import com.momo.library.share.qq.TencetShare;
import com.momo.library.share.sina.SinaShare;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import cn.jpush.android.api.TagAliasCallback;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener, ShareManger.OnShareItemClickListener
        , IWeiboHandler.Response, IUiListener,EasyPermissions.PermissionCallbacks,UpdateHelper.UpdateButtonOkListener {


    @BindView(android.R.id.tabhost)
    MyFragmentTabHost mTabhost;


    private List<Drawable> mTabDrawable = new ArrayList<>();
    private SparseArray<Class> mtabFragments = new SparseArray<>();

    private UpdateHelper updateHelper;


    public static String shareTitle="";
    public static String introduction="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabhost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabhost.getTabWidget().setDividerDrawable(null); // 去掉分割线

        String[] tabs = getResources().getStringArray(R.array.tab_names);

        mTabDrawable.add(SelectorUtils.generatorDrawableState(this, R.drawable.tab_home_normal, R.drawable.tab_home_selected));
        mTabDrawable.add(SelectorUtils.generatorDrawableState(this, R.drawable.tab_bc_normal, R.drawable.tab_bc_selected));
        mTabDrawable.add(SelectorUtils.generatorDrawableState(this, R.drawable.tab_by_normal, R.drawable.tab_by_selected));
        mTabDrawable.add(SelectorUtils.generatorDrawableState(this, R.drawable.tab_me_normal, R.drawable.tab_me_selected));

//        HomeFragment homeFragment = HomeFragment.newInstance();
//        homeFragment.setHomeFragmentCallback(this);
        mtabFragments.put(0, HomeFragment.newInstance().getClass());

        mtabFragments.put(1, CheapFragment.newInstance().getClass());
        mtabFragments.put(2, BaoYouFragment.newInstance().getClass());
        mtabFragments.put(3, MeFragment.newInstance().getClass());

        for (int i = 0; i < tabs.length; i++) {

            ColorStateList colorStateList = SelectorUtils.createColorStateList(this,
                    R.color.tabTextNormal, R.color.tabTextSelect);

            // Tab按钮添加文字和图片
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec("tag" + i).setIndicator(getTabItem(tabs[i],
                    colorStateList,
                    mTabDrawable.get(i)));

            // 添加Fragment
            mTabhost.addTab(tabSpec, mtabFragments.get(i), null);
            // 设置Tab按钮的背景
            //mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.pedo_actionbar_bkg);

        }

        mTabhost.setOnTabChangedListener(this);

        initShare();

         updateHelper = new UpdateHelper(this);
        updateHelper.setUpdateButtonOkListener(this);
        int inid_id = ResourcesUtil.getStringId(this, "inid");
        if (inid_id > 0) {
            String inidStr = getResources().getString(inid_id);
            updateHelper.update(inidStr);
        }

    }

    private View getTabItem(String title, ColorStateList colorStateList, Drawable drawable) {
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_layout_item, null);

        TextView tabItem = (TextView) view.findViewById(R.id.tabItem);
        tabItem.setText(title);
        tabItem.setTextColor(colorStateList);

        tabItem.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

        return view;


    }

    @Override
    public int getRootView() {
        return R.layout.activity_main;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //handleWeiboResponse(intent, this);
        if (shareManger != null && shareManger.getSinaShare() != null) {
            ((SinaShare) shareManger.getSinaShare()).getWeiboShareAPI()
                    .handleWeiboResponse(intent, this);
        }

    }

    //登录须重写onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CallbackContext.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (shareManger != null && shareManger.getSinaShare() != null) {
            ((SinaShare) shareManger.getSinaShare()).getSsoHandler()
                    .authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private ShareManger shareManger;

    private void initShare() {
        shareManger = new ShareManger()
                .initShareDialog(this);

        if (ShareCheckUtils.hasSinaConfig(this)) {
            shareManger.addSina();
        }
        if (ShareCheckUtils.hasWxConfig(this)) {
            shareManger.addWXSceneSession()
                    .addWXTimeLine();
        }
        if (ShareCheckUtils.hasTenceConfig(this)) {
            shareManger.addTencentQQ()
                    .addTencentQzone();
        }

        shareManger.setOnShareItemClickListener(this);

    }

    @Override
    public void onShareItemClickListener(ShareType shareType) {
        shareManger.dismiss();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        if (((MeFragment) getCurrentFragment()).shareData != null) {
            String shareAddress = ((MeFragment) getCurrentFragment()).shareData.getData().getShareAddress();
            String shareImagePath = (String) SPUtils.get(MainActivity.this,"logoPath","");


            if (shareType instanceof TencetShare) {
                ((TencetShare) shareType).share(this, shareImagePath, shareAddress,shareTitle,introduction, this);
            } else {

                shareType.share(this, bitmap, shareAddress,shareTitle,introduction);
            }
        }
    }

    //微信授权
    @Override
    public void onResponse(BaseResponse baseResponse) {
        if (baseResponse != null) {
            switch (baseResponse.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    Toast.makeText(this, "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    Toast.makeText(this,
                            "failed Error Message: " + baseResponse.errMsg,
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    //QQ分享回调

    @Override
    public void onComplete(Object o) {

    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }

    public void showShare() {
        shareManger.showShare();
    }

    /**
     * 得到当前的fragment
     *
     * @return
     */
    public Fragment getCurrentFragment() {

        return getSupportFragmentManager().findFragmentByTag(mTabhost.getCurrentTabTag());

    }

    @Override
    public void onTabChanged(String tabId) {
        if ("tag1".equals(tabId)) {
            // 选中第二个
            mTabhost.setCurrentTab(1);

        }
    }

    private StringBuffer seletedTabName = new StringBuffer();

    public StringBuffer getSeletedTabName() {
        return seletedTabName;
    }

    public void clearSelectTabName() {
        seletedTabName.delete(0, seletedTabName.length());
    }

    public void showCategory(String name) {
        mTabhost.setCurrentTab(1);
        clearSelectTabName();
        seletedTabName.append(name);
    }


    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0:
                    //// 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。

                    break;
                case 6002:
                    // 设置别名失败

                    break;
            }
        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void jpushMsgCallback(MessageModel model) {

        mTabhost.getCurrentTab();
        ((BaseFragment) getCurrentFragment()).setMessageState(model.getRead());

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (updateType == 0) {
            updateHelper.upDate(updateUrl);
        } else {
            updateHelper.downloadApp(updateUrl);

        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("权限申请")
                    .setRationale(PermissionConstance.storageRational)
                    .build().show();
        }
    }

    //升级回调
    @Override
    public void onStartUpdate(int type,String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        update(type, url);
    }

    private int updateType;
    private String updateUrl;
    @AfterPermissionGranted(PermissionConstance.STORAGE_PER)
    public void update(int type,String url) {
        this.updateType = type;
        this.updateUrl = url;

        String storage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (EasyPermissions.hasPermissions(this, storage)) {
            if (type == 0) {
                updateHelper.upDate(url);
            } else {
                updateHelper.downloadApp(url);

            }
        } else {
            EasyPermissions.requestPermissions(this, PermissionConstance.storageRational,
                    PermissionConstance.STORAGE_CODE,storage);
        }
    }

}

