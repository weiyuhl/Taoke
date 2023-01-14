package com.jsyh.buyer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.jsyh.buyer.R;
import com.jsyh.buyer.data.RetrofitClient;
import com.jsyh.buyer.utils.AppUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class UpdateService extends Service {


    private String url;

    private NotificationManager notifiManager;
    private NotificationCompat.Builder builder;
    private Notification notification;

    private boolean notify =false;

    private int notify_id = 100;
    private int progress;


    public static final int start = 1;
    public static final int ok = 2;
    public static final int downloading = 3;

    public static final int download_exception = 4;

    private boolean isDownloading = false;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (!notify) {
                return;
            }
            switch (msg.what) {
                case start:
                    notifiManager.notify(notify_id, notification = builder.build());
                    isDownloading = true;
                    break;
                case ok:
                    notifiManager.cancel(notify_id);
                    isDownloading = false;

                    AppUtils.installAPK(UpdateService.this,new File(((String) msg.obj)));
                    break;

                case downloading:
                    isDownloading = true;
                    Log.d("tag", "" + msg.arg1);
                    builder.setProgress(100, msg.arg1, false);
                    builder.setContentText("已经下载 " + msg.arg1 + "%");
                    notification = builder.build();
                    notifiManager.notify(notify_id,notification);
                    break;
                case download_exception:
                    isDownloading = false;
                    notifiManager.cancel(notify_id);
                    break;
            }
        }
    };

    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        notifiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        builder = new android.support.v7.app.NotificationCompat.Builder(getApplicationContext());

        notification = builder.setTicker("开始下载")
                .setContentText("已经下载0%")
                .setContentTitle("下载最新应用")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(0)
                .setProgress(100, 0, false)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(false)
                .build();

        notifiManager.notify(notify_id, notification = builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isDownloading) {
            url = intent.getStringExtra("url");
            notify = intent.getBooleanExtra("notify", false);
            new Thread(new DownloadRunnable()).start();
        }

        return START_NOT_STICKY;
    }

    class DownloadRunnable implements Runnable {

        @Override
        public void run() {
            Call<ResponseBody> call = RetrofitClient.getInstance().getFixApid()
                    .getApk(url);
            String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(absolutePath + File.separator + "tbk.apk");
            if (file.exists()) {
                file.delete();
            }

            Response<ResponseBody> response;

            FileOutputStream fos = null;
            InputStream is = null;
            try {
                response = call.execute();
                if (response.isSuccessful()) {

                    handler.sendEmptyMessage(start);

                    long fileSize = 0;
                    int length = 0;
                    byte[] buff = new byte[1024 *10];
                    long downloadCount = 0;

                    fileSize = response.body().contentLength();

                    is = response.body().byteStream();

                    fos = new FileOutputStream(file);

                    while ((length = is.read(buff)) != -1) {
                        fos.write(buff, 0, length);
                        fos.flush();


                        downloadCount += length;
                        int tempProgress = (int) ((downloadCount * 100) / fileSize);
                        if (tempProgress > progress) {
                            progress = tempProgress;
                            Message message = handler.obtainMessage();
                            message.what = downloading;
                            message.arg1 = progress;
                            handler.sendMessage(message);
                        }

                    }
                    Message message = handler.obtainMessage();
                    message.what = ok;
                    message.arg1=100;
                    message.obj=file.getAbsolutePath();
                    handler.sendMessage(message);

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(download_exception);
            } catch (IOException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(download_exception);
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}



