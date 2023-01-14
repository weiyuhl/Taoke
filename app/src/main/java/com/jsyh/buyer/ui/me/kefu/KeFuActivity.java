package com.jsyh.buyer.ui.me.kefu;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jsyh.buyer.BaseActivity;
import com.jsyh.buyer.R;
import com.jsyh.buyer.model.CustomerModel;
import com.jsyh.buyer.model.ShareAppModel;
import com.jsyh.buyer.utils.AppUtils;
import com.jsyh.buyer.utils.PermissionConstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class KeFuActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {


    @BindView(R.id.qq)
    TextView qq;
    @BindView(R.id.wechat)
    TextView wechat;
    @BindView(R.id.phone)
    TextView phone;

    private List<CustomerModel> data;

    private Map<String, String> type = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ShareAppModel shareAppModel = getIntent().getParcelableExtra("data");
        data = shareAppModel.getKefu();

        for (CustomerModel model : data) {
            type.put(model.getType(), model.getValue());
        }

        if (type.containsKey("qq")) {
            qq.setText(type.get("qq"));
        }

        if (type.containsKey("weixin")) {
            wechat.setText(type.get("weixin"));
        }else{
            wechat.setText(type.get("暂无"));

        }

        if (type.containsKey("phone")) {
            phone.setText("客服电话 : "+ type.get("phone"));
        }


    }

    @Override
    protected int getRootView() {
        return R.layout.ke_fu_activity;
    }

    @OnClick({R.id.back, R.id.qq, R.id.wechat, R.id.phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.qq:
                if (AppUtils.checkApkExist(this, "com.tencent.mobileqq")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qq.getText() + "&version=1")));
                } else {
                    Toast.makeText(this, "本机未安装QQ应用", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.wechat:
                break;
            case R.id.phone:
                call(type.get("phone"));

                break;
        }
    }

    @AfterPermissionGranted(PermissionConstance.CALL_PER)
    public void call(String phone) {

        String callPhone = Manifest.permission.CALL_PHONE;
        if (EasyPermissions.hasPermissions(this, callPhone)) {
            Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }else {
            EasyPermissions.requestPermissions(this, PermissionConstance.callRational, PermissionConstance.CALL_CODE, callPhone);
        }



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setRationale(PermissionConstance.callRational)
                    .setTitle("权限申请")
                    .build().show();
        }

    }
}
