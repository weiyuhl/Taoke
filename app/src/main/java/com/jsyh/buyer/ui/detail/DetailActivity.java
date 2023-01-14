package com.jsyh.buyer.ui.detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.constants.AlibcConstants;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.AlibcTaokeParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.jsyh.buyer.BaseActivity;
import com.jsyh.buyer.MainActivity;
import com.jsyh.buyer.R;
import com.jsyh.buyer.ui.iview.DetailView;
import com.jsyh.buyer.ui.presenter.DetailPresenter;
import com.jsyh.buyer.utils.DemoTradeCallback;
import com.jsyh.buyer.utils.L;
import com.jsyh.buyer.utils.ResourcesUtil;
import com.jsyh.buyer.utils.SPUtils;
import com.jsyh.buyer.utils.SystemBarHelper;
import com.momo.library.ShareCheckUtils;
import com.momo.library.share.ShareManger;
import com.momo.library.share.ShareType;
import com.momo.library.share.qq.TencetShare;
import com.momo.library.share.sina.SinaShare;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.MD5;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class DetailActivity extends BaseActivity implements DetailView, ShareManger.OnShareItemClickListener
        , IWeiboHandler.Response, IUiListener {


    @BindView(R.id.detailWebView)
    WebView mWebView;

    private Map<String, String> exParams = new HashMap<>();

    private String imageId; //商品id
    private String iid;

    private String url;

    private ShareManger shareManger;

    private DetailPresenter mPresenter;

    private String mSharePicUrl;
    private Bitmap mShareBitmap;
    private String mSharePicPath;
    private String mShareTitle;
    private String mShareIntroduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new DetailPresenter(this);

        initAlibc();

        initShare();


    }

    private void initAlibc() {

        int identifier = getResources().getIdentifier("ali_mm", "string", getPackageName());
        if (identifier <= 0) {
            Toast.makeText(this, "阿里配置没有填写错误", Toast.LENGTH_SHORT).show();
            finish();
            return;

        }
        String ali_mm = getResources().getString(identifier);
        if (TextUtils.isEmpty(ali_mm)) {
            Toast.makeText(this, "阿里妈妈id是空的", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mWebView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        Bundle extras = getIntent().getExtras();
        imageId = extras.getString("imageId");


        iid = extras.getString("iid");
        url = extras.getString("url") /*+ "nowake=1"*/;

        mShareIntroduction = extras.getString("introduction");
        mShareTitle = extras.getString("title");

        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改

        AlibcTaokeParams alibcTaokeParams = new AlibcTaokeParams(ali_mm, "", ""); // 若非淘客taokeParams设置为null即可

        AlibcShowParams alibcShowParams = new AlibcShowParams(OpenType.H5, false);
        if (TextUtils.isEmpty(iid)) {

//            if (!TextUtils.isEmpty(url) && !url.startsWith("https://uland.taobao.com")) {
//                WebSettings websettings = mWebView.getSettings();
//                websettings.setSupportZoom(true);
//                websettings.setBuiltInZoomControls(true);
//                websettings.setDisplayZoomControls(false);
//                mWebView.setWebViewClient(new DetailWebClient());
//                mWebView.loadUrl(url);
//
//            }else{

                url += "&nowake=1";
                AlibcTrade.show(this, mWebView, new DetailWebClient(), null, new AlibcPage(url), alibcShowParams, alibcTaokeParams, exParams, new DemoTradeCallback(this));
//            }

        } else {

            AlibcBasePage alibcBasePage = new AlibcDetailPage(iid);
            AlibcTrade.show(this, mWebView, new DetailWebClient(), null, alibcBasePage, alibcShowParams, alibcTaokeParams, exParams, new DemoTradeCallback(this));
        }

        initSharePic();
    }

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

    private void initSharePic() {

        if (TextUtils.isEmpty(imageId)) {
            return;
        }
        int i = 0;
        try {
            i = Integer.parseInt(imageId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        mPresenter.sharePic(i);
    }

    @Override
    protected int getRootView() {
        return R.layout.detail_activity;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AlibcTradeSDK.destory();
        mPresenter.unsubscribe();
    }

    @OnClick({R.id.detailBack, R.id.detailGo, R.id.detailRefresh, R.id.detailShare, R.id.detailClose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.detailBack:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }

                break;
            case R.id.detailGo:
                if (mWebView.canGoForward()) {
                    mWebView.goForward();
                }

                break;
            case R.id.detailRefresh:
                mWebView.reload();

                break;
            case R.id.detailShare:

                if (ResourcesUtil.getBooleanId(this, "has_share") > 0) {
                    if (getResources().getBoolean(ResourcesUtil.getBooleanId(this, "has_share"))) {

                        shareManger.showShare();
                    } else {
                        Toast.makeText(this, R.string.has_share_tips, Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.detailClose:
                finish();
                break;
        }
    }

    @Override
    public void onShareItemClickListener(ShareType shareType) {
        shareManger.dismiss();
        String shareImagePath = (String) SPUtils.get(this,"logoPath","");
        if (shareType instanceof TencetShare) {
            if (TextUtils.isEmpty(imageId)) {
                ((TencetShare) shareType).share(this, shareImagePath, url, mShareTitle, mShareIntroduction, this);
            } else {

                ((TencetShare) shareType).share(this, mSharePicPath, mSharePicUrl, mShareTitle, mShareIntroduction, this);
            }
        } else {
            if (TextUtils.isEmpty(imageId)) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                shareType.share(this, bitmap, url, mShareTitle, mShareIntroduction);
            } else {
                if (mShareBitmap != null) {

                    shareType.share(this, mShareBitmap);
                }
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

    // ================== view ===================

    @Override
    public void onLoadSharePicUlr(String url) {
        mSharePicUrl = new String(url);

    }

    @Override
    public void onLoadShareBitmap(Bitmap bitmap) {
        final Bitmap tempBitmap = bitmap;
        new Thread(){
            @Override
            public void run() {
                if (tempBitmap != null) {

                    mShareBitmap = Bitmap.createBitmap(tempBitmap);
                    try {
                        String md5Url = MD5.hexdigest(mSharePicUrl);
                        String imagePath = getExternalCacheDir().getAbsolutePath();
                        File file = new File(imagePath + File.separator + md5Url + ".png");
                        mSharePicPath = file.getAbsolutePath();
                        if (file.exists()) {
                            return;
                        }
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedOutputStream bos = new BufferedOutputStream(fos);
                        tempBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                        bos.flush();
                        bos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
        }.start();
    }


    @Override
    public void onFindUnread(boolean msg) {

    }


}
