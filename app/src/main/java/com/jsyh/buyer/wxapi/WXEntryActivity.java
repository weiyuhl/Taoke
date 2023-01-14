package com.jsyh.buyer.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.momo.library.Constants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * Created by Su on 2016/7/29.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {


    private static final String TAG = "wxlogindemo";

    IWXAPI mIWXAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int wxIndentifier = getResources().getIdentifier("wx_app_key", "string", getPackageName());
        if (wxIndentifier < 0) {
            return;
        }

        String strAppkey = getString(wxIndentifier);
        /*if (TextUtils.isEmpty(strAppkey)) {
            return;
        }*/

        mIWXAPI = WXAPIFactory.createWXAPI(this, strAppkey, false);
        mIWXAPI.registerApp(strAppkey);
        mIWXAPI.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mIWXAPI.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        /**
         * 来自登录回调
         */
        if (baseResp instanceof SendAuth.Resp) {
            Log.d(TAG, "onResp: " + getIntent().getExtras().get("_wxapi_sendauth_resp_url"));
            String code = "";

            if (((SendAuth.Resp) baseResp).code != null) {
                code = ((SendAuth.Resp) baseResp).code;
            }
            //baseResp.
            Log.d(TAG, "onResp: code = " + code);
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    Toast.makeText(WXEntryActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Toast.makeText(WXEntryActivity.this, "取消登录", Toast.LENGTH_SHORT).show();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    Toast.makeText(WXEntryActivity.this, "授权被拒绝", Toast.LENGTH_SHORT).show();
                    break;
                default:
//                    Toast.makeText(WXEntryActivity.this, "返回", Toast.LENGTH_SHORT).show();
                    break;
            }

            // startActivity(new Intent(this, MainActivity.class));


        } else if (baseResp instanceof SendMessageToWX.Resp) {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    Toast.makeText(WXEntryActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Toast.makeText(WXEntryActivity.this, "取消分享", Toast.LENGTH_SHORT).show();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    Toast.makeText(WXEntryActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
//                    Toast.makeText(WXEntryActivity.this, "返回qqqqqqq", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        finish();
    }


}
