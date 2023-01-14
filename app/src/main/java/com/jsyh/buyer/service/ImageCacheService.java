package com.jsyh.buyer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.jsyh.buyer.data.RetrofitClient;
import com.jsyh.buyer.model.AdvertisingModel;
import com.jsyh.buyer.model.BaseModel;
import com.jsyh.buyer.utils.L;
import com.jsyh.buyer.utils.MD5;
import com.jsyh.buyer.utils.SPUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ImageCacheService extends Service {


    private String imageCachePath;


    boolean downloading = false;

    @Override
    public void onCreate() {
        super.onCreate();
        imageCachePath = getExternalCacheDir().getAbsolutePath();

    }

    public ImageCacheService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!downloading) {
//            url = intent.getStringExtra("url");
            getAdvUrl();

        }
        return START_NOT_STICKY;
    }


    private void getAdvUrl() {
        RetrofitClient.getInstance()
                .getAdv("app_start")
                .subscribe(new Observer<BaseModel<List<AdvertisingModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseModel<List<AdvertisingModel>> model) {

                        if (model != null && model.getCode() == 500) {
                            //用户隐藏的图片
                            SPUtils.put(ImageCacheService.this, "advName", "");
                            stopSelf();
                            return;
                        }

                        if (model != null && model.getData().size() > 0) {

                            String advName = (String) SPUtils.get(ImageCacheService.this, "advName", "");

                            AdvertisingModel adv = model.getData().get(0);
                            if (TextUtils.isEmpty(adv.getPicUrl())) {
                                stopSelf();
                                return;
                            }
                            if (adv.getPicUrl().endsWith(".png") || adv.getPicUrl().endsWith(".jpg")) {

                                String newAdvName = MD5.getMessageDigest(adv.getPicUrl().getBytes()) + ".png";

                                if (newAdvName.equals(advName)) {
                                    //如果下载地址相同，不更新图片
                                    stopSelf();
                                    return;
                                }

                                new Thread(new ImageCacheRunnable(adv.getPicUrl())).start();

                                String time = model.getData().get(0).getTime();

                                try {
                                    if (!TextUtils.isEmpty(time)) {
                                        long l = Long.parseLong(time);
                                        SPUtils.put(ImageCacheService.this, "adv_time", l);
                                        SPUtils.put(ImageCacheService.this, "adv_url", model.getData().get(0).getUrl());
                                        SPUtils.put(ImageCacheService.this, "adv_title", model.getData().get(0).getTitle());
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();

                                }
                            } else {
                                stopSelf();
                            }

                        } else {
                            stopSelf();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.d(e.toString());
                        stopSelf();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    class ImageCacheRunnable implements Runnable {

        String url;

        public ImageCacheRunnable(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            Call<ResponseBody> call = RetrofitClient.getInstance()
                    .getFixApid()
                    .dowloadFile(url);

            String picName = MD5.getMessageDigest(url.getBytes()) + ".png";
            File file = new File(imageCachePath + File.separator + picName);

            InputStream inputStream = null;
            FileOutputStream fos = null;

            try {
                Response<ResponseBody> response = call.execute();

                if (response != null && response.isSuccessful()) {

                    /*if (!"image".equals(response.body().contentType().type())) {
                        stopSelf();
                        return;
                    }*/

                    if (file.exists()) {
                        file.delete();
                    }

                    inputStream = response.body().byteStream();
                    fos = new FileOutputStream(file);

                    int readLength = -1;
                    byte[] buff = new byte[1024 * 10];
                    downloading = true;
                    while ((readLength = inputStream.read(buff)) != -1) {
                        fos.write(buff, 0, readLength);
                        fos.flush();
                    }
                    SPUtils.put(ImageCacheService.this, "advName", picName);//图片名字

                }


            } catch (IOException e) {
                e.printStackTrace();
                downloading = false;
                if (file.exists()) {
                    file.delete();
                }
                SPUtils.put(ImageCacheService.this, "advName", "");
            } finally {


                try {
                    if (fos != null) {
                        fos.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                downloading = false;
                stopSelf();
            }


        }
    }


}
