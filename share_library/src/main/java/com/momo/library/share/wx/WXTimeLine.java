package com.momo.library.share.wx;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.momo.library.Constants;
import com.momo.library.R;
import com.momo.library.share.ShareType;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信朋友圈
 * Created by mo on 17-4-20.
 */

public class WXTimeLine extends ShareType {

    private IWXAPI wxApi;
    //分享场景: 联系人,朋友圈,收藏
    private int mTargetScene = SendMessageToWX.Req.WXSceneTimeline;

    private boolean success = false;    // 注册成功


    public WXTimeLine(Activity activity) {
        super(activity);
        int wxIndentifier = activity.getResources().getIdentifier("wx_app_key", "string", activity.getPackageName());
        if (wxIndentifier <= 0) {
            return;
        }

        String strAppkey = activity.getString(wxIndentifier);
        if (TextUtils.isEmpty(strAppkey)) {
            return;
        }
        wxApi = WXAPIFactory.createWXAPI(activity, strAppkey, true);
        success= wxApi.registerApp(strAppkey);

    }

    @Override
    public void share(Activity activity, Bitmap bitmap) {

        if (wxApi == null) {
            Toast.makeText(activity, "微信配置错误 ", Toast.LENGTH_SHORT).show();
            return;
        }

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = WXShareUtils.getImageObject(bitmap);


        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);

        msg.thumbData = WXShareUtils.bmpToByteArray(scaledBitmap);
        scaledBitmap.recycle();



        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = WXShareUtils.buildTransaction("img");
        req.message = msg;
        req.scene = mTargetScene;
        wxApi.sendReq(req);

    }

    @Override
    public void share(Activity activity, Bitmap bitmap, String url,String title, String introduction) {

        if (wxApi ==null)
            return;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = WXShareUtils.getWebpage(url);
        msg.thumbData = WXShareUtils.bmpToByteArray(bitmap);
        msg.title = title;


        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = WXShareUtils.buildTransaction("webpage");
        req.message = msg;
        msg.description = introduction;
        req.scene = mTargetScene;
        wxApi.sendReq(req);
    }
}
