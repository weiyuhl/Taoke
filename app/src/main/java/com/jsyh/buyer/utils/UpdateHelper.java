package com.jsyh.buyer.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.jsyh.buyer.R;
import com.jsyh.buyer.data.Api;
import com.jsyh.buyer.data.RetrofitClient;
import com.jsyh.buyer.model.BaseModel;
import com.jsyh.buyer.model.UpdataModel;
import com.jsyh.buyer.service.UpdateService;
import com.jsyh.buyer.widget.dialog.CustomDialog;
import com.jsyh.buyer.widget.dialog.SingleButtonDialog;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by mo on 17-4-28.
 */

public class UpdateHelper {

    private Activity activity;

    private CustomDialog customDialog = null;
    private SingleButtonDialog singleButtonDialog = null;


    public UpdateHelper(Activity activity) {
        this.activity = activity;


    }


    public void update(String inid) {
        if (TextUtils.isEmpty(inid)) {
            return;
        }

        RetrofitClient.getInstance()
                .update(Api.UPDATE_URL ,inid,AppUtils.longVersionCode(activity) + "")

                .subscribe(new Observer<BaseModel<UpdataModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseModel<UpdataModel> model) {
                        update(model);
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e(e.toString());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void update(final BaseModel<UpdataModel> model){

        //1.应用过期 或者 被禁用了
        if (TextUtils.equals(model.getCode()+"", Api.ERROR_CODE)) {
            singleButtonDialog = new SingleButtonDialog(activity, model.getMsg(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleButtonDialog.hide();


                }
            });
            singleButtonDialog.show(model.getData().getTitle(), model.getMsg(), model.getData().getSite(), model.getData().getSign(), " 确定");
            return;
        }
        //2.判断版本
        if (AppUtils.longVersionCode(activity) < model.getData().getVNo()) {
            if (!TextUtils.isEmpty(model.getData().getForce()) && !"2".equals(model.getData().getForce())){
                customDialog = new CustomDialog(activity, "title", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.hide();
                        if (updateButtonOkListener != null) {
                            updateButtonOkListener.onStartUpdate(0,"http://192.168.0.121:9000/tbk.apk");
                        }else {

                            upDate("http://192.168.0.121:9000/tbk.apk");
                        }
                    }
                });
                if ("1".equals(model.getData().getForce())) {

                    customDialog.setCanceledOnTouchOutside(false);        //点击窗口意外不能关闭
                    customDialog.setCancelable(false);        //不能按返回键关闭
                    customDialog.show2(model.getMsg());
                    return;
                }

                customDialog.show("发现最新版本", "稍后再说", "马上更新",model.getMsg());
                return;
            }
            //1、在wifi情况下，后台直接下载
            final String versionCode = "updateVersionCode";
            if (NetworkUtils.isNetworkAvailable(activity) == 1) {
                //首先判断本地是否有更新包
                final File updateFile = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/" + "tbk.apk");

                if (updateFile.exists() && ((model.getData().getVNo() + "").equals(SPUtils.get(activity, versionCode,"")))) {
                    //存在弹出更新提示，提示更新
                    customDialog = new CustomDialog(activity, "title",
                            null,
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    customDialog.hide();
                                    AppUtils.installAPK(activity,updateFile);
                                }
                            });

                    if (!TextUtils.isEmpty(model.getData().getForce()) && "1".equals(model.getData().getForce())) {

                        customDialog.setCanceledOnTouchOutside(false);        //点击窗口意外不能关闭
                        customDialog.setCancelable(false);        //不能按返回键关闭
                        customDialog.show2(model.getMsg());
                        return;
                    }
                    customDialog.show("发现最新版本", "稍后再说", "马上更新",model.getMsg());
                } else {
                    //不存在直接进入后台下载
                    if (updateButtonOkListener != null) {
                        updateButtonOkListener.onStartUpdate(-1,model.getData().getPath());
                    }else {

                        downloadApp(model.getData().getPath());
                    }
                }
            } else {
                //2、在网络情况下,提示有新版本，用户选择是下载还是下次
                //首先判断本地是否有更新包
                customDialog = new CustomDialog(activity, "title",
                        null,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.hide();
                                final File updateFile = new File(Environment.getExternalStorageDirectory()
                                        .getAbsolutePath() + "/" + "tbk.apk");
                                if (updateFile.exists() && ((model.getData().getVNo() + "").equals(SPUtils.get(activity, versionCode, "")))) {
                                    AppUtils.installAPK(activity,updateFile);
                                } else {
                                    if (updateButtonOkListener != null) {
                                        updateButtonOkListener.onStartUpdate(0,model.getData().getPath());
                                    }else {

                                    upDate(model.getData().getPath());
                                    }
//                                            SPUtils.put(context, versionCode, model.getData().getV_no()+"");
                                }
                                //installAPK(updateFile);
                            }
                        });

                if (!TextUtils.isEmpty(model.getData().getForce()) && "1".equals(model.getData().getForce())) {

                    customDialog.setCanceledOnTouchOutside(false);        //点击窗口意外不能关闭
                    customDialog.setCancelable(false);        //不能按返回键关闭
                    customDialog.show2(model.getMsg());
                    return;
                }
                customDialog.show("发现最新版本", "稍后再说", "马上更新",model.getMsg());
            }
        } else {
            //没有升级包，删掉updata.app
            File updateFile = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + "tbk.apk");
            if (updateFile.exists()) {
                updateFile.delete();
            }
        }


    }

    // 下载并安装apk
    public void upDate(String url) {

        if (PermissionChecker.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "请开启存储权限!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
           startService(url,true);


        } else {
            // CommonUtil.showToast(context, "没有sdcard，请安装上在试");
        }
    }

    //后台下载apk，没有通知栏通知
    public void downloadApp(String url) {
        if (PermissionChecker.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                startService(url,false);


            } else {
                // CommonUtil.showToast(context, "没有sdcard，请安装上在试");
            }
        } else {
            //没有权限
            /*EasyPermissions.requestPermissions(null, "需要手机开启存储权限",
                    PermissionsUtils.WRITE_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE);*/
            Toast.makeText(activity, "请开启存储权限!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean canUpdate(long newVersionCode) {
        int currentCode = AppUtils.longVersionCode(activity);

        if (newVersionCode > currentCode) {
            return true;
        }
        return false;
    }




    private void startService(String url,boolean notify) {
        Intent intent = new Intent(activity, UpdateService.class);

        intent.putExtra("url", url);
        intent.putExtra("notify", notify);

        activity.startService(intent);
    }

    private UpdateButtonOkListener updateButtonOkListener;

    public void setUpdateButtonOkListener(UpdateButtonOkListener updateButtonOkListener) {
        this.updateButtonOkListener = updateButtonOkListener;
    }

    //0,有提示框，-1没有提示框
    public interface UpdateButtonOkListener{
        void onStartUpdate(int type,String url);
    }


}
