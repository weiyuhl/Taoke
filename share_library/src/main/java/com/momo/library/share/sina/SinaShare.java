package com.momo.library.share.sina;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.momo.library.Constants;
import com.momo.library.R;
import com.momo.library.share.ShareType;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.Utility;

/**
 * Created by mo on 17-4-20.
 *
 *
 * 实现分享当前Activity必须准备以上四个步骤:
 *(1)修改AndroidManifest.xml
 *
 *      为当前分享所在页面的Activity添加接收消息
 *      intent-filter,声明Action为:com.sina.weibo.sdk.action.ACTION_SDK_REQ_ACTIVITY
 *
 *
 *
 *(2)Activity实现IWeiboHandler.Response接又
 *
 *      在分享当前页面实现WeiboHandler.Response
 *
 *
 *(3)Activity处理IWeiboHandler.Response的方法
 *
 *
 *
 *(4)Activity处理onNewIntent方法
 *
 *(5)授权回调
 *
 *      SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
 *
 *
 */

public class SinaShare extends ShareType {

    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;
    private IWeiboShareAPI weiboShareAPI;

    private WeiboMultiMessage weiboMessage;

    public SinaShare(Activity activity) {
        super(activity);
        int appkeyIdentifier = activity.getResources().getIdentifier("wb_app_key", "string", activity.getPackageName());
        int redirectIdentifier = activity.getResources().getIdentifier("wb_redirect_url", "string", activity.getPackageName());


        if (appkeyIdentifier <= 0 || redirectIdentifier <= 0) {

            return;
        }

        String strAppkey = activity.getResources().getString(appkeyIdentifier);
        String strRedirect = activity.getResources().getString(redirectIdentifier);

        if (TextUtils.isEmpty(strAppkey)||TextUtils.isEmpty(strRedirect)) {
            return;
        }

        mAuthInfo = new AuthInfo(activity, strAppkey, strRedirect, Constants.SCOPE);
        mSsoHandler = new SsoHandler(activity, mAuthInfo);
        weiboShareAPI = WeiboShareSDK.createWeiboAPI(activity, strAppkey);
    }

    public IWeiboShareAPI getWeiboShareAPI() {
        return  weiboShareAPI;
    }

    public SsoHandler getSsoHandler() {
        return mSsoHandler;
    }

    @Override
    public void share(final Activity activity,Bitmap bitmap) {

        if (!(activity instanceof IWeiboHandler.Response) ){
            Toast.makeText(activity, "Activity没有实现IWeiboHandler.Response接口", Toast.LENGTH_SHORT).show();
            return;
        }
        if (weiboShareAPI == null) {
            Toast.makeText(activity, "微博配置错误", Toast.LENGTH_SHORT).show();
            return;
        }

        //1.注册到微博
        weiboShareAPI.registerApp();

        // 2. 初始化微博的分享消息
         weiboMessage = new WeiboMultiMessage();

        weiboMessage.mediaObject = getImageObject(bitmap);

        // 3. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        mAccessToken = AccessTokenKeeper.readAccessToken(activity);
        String token = "";
        if (mAccessToken != null) {
            token = mAccessToken.getToken();
        }

        weiboShareAPI.sendRequest(activity, request, mAuthInfo, token, new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                Oauth2AccessToken oauth2AccessToken = Oauth2AccessToken.parseAccessToken(bundle);
                if (oauth2AccessToken.isSessionValid()) {

                    //授权成功
                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                    AccessTokenKeeper.writeAccessToken(activity.getApplicationContext(), newToken);
                } else {
                    // 以下几种情况，您会收到 Code：
                    // 1. 当您未在平台上注册的应用程序的包名与签名时；
                    // 2. 当您注册的应用程序包名与签名不正确时；
                    // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                    String code = bundle.getString("code");

                }

            }

            @Override
            public void onWeiboException(WeiboException e) {
                Log.d("tag", e.toString());

            }

            @Override
            public void onCancel() {

            }
        });

    }


    @Override
    public void share(final Activity activity, Bitmap bitmap, String url,String title, String introduction) {
        if (!(activity instanceof IWeiboHandler.Response) ){
            Toast.makeText(activity, "Activity没有实现IWeiboHandler.Response接口", Toast.LENGTH_SHORT).show();
            return;
        }

        if (weiboShareAPI == null) {
            Toast.makeText(activity, "微博配置错误", Toast.LENGTH_SHORT).show();
            return;
        }

        //1.注册到微博
        weiboShareAPI.registerApp();

        // 2. 初始化微博的分享消息
        weiboMessage = new WeiboMultiMessage();

        weiboMessage.imageObject = getImageObject(bitmap);
//        weiboMessage.textObject = getTextObject(url);
        weiboMessage.mediaObject = getWebpageObj(bitmap, url,title);

        // 3. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        mAccessToken = AccessTokenKeeper.readAccessToken(activity);
        String token = "";
        if (mAccessToken != null) {
            token = mAccessToken.getToken();
        }

        weiboShareAPI.sendRequest(activity, request, mAuthInfo, token, new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                Oauth2AccessToken oauth2AccessToken = Oauth2AccessToken.parseAccessToken(bundle);
                if (oauth2AccessToken.isSessionValid()) {

                    //授权成功
                    Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                    AccessTokenKeeper.writeAccessToken(activity.getApplicationContext(), newToken);
                } else {
                    // 以下几种情况，您会收到 Code：
                    // 1. 当您未在平台上注册的应用程序的包名与签名时；
                    // 2. 当您注册的应用程序包名与签名不正确时；
                    // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                    String code = bundle.getString("code");

                }

            }

            @Override
            public void onWeiboException(WeiboException e) {
                Log.d("tag", e.toString());

            }

            @Override
            public void onCancel() {

            }
        });
    }

    private TextObject getTextObject(String text) {

        if (TextUtils.isEmpty(text))return null;

        //文本
        TextObject textObject = new TextObject();
        textObject.text = text;

        return textObject;
    }

    private ImageObject getImageObject(Bitmap bitmap) {
        ImageObject imageObject = new ImageObject();
        //设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        imageObject.setImageObject(bitmap);

        return imageObject;
    }
    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj(Bitmap bitmap,String url,String title) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = "";

//        Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo);
//        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = url;
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;
    }




}
