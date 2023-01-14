package com.momo.library.share.wx;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.momo.library.Constants;
import com.momo.library.share.ShareManger;
import com.momo.library.share.ShareType;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * 微信实例化 类,
 * Created by mo on 17-4-20.
 */

public class WXShareUtils {

    private boolean isRegist = false;
    private IWXAPI wxApi;
    //分享场景: 联系人,朋友圈,收藏
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;


    private static volatile WXShareUtils instance;

    //定义一个共有的静态方法，返回该类型实例
    public static WXShareUtils getInstance() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (ShareManger.class) {
                //未初始化，则初始instance变量
                if (instance == null) {
                    instance = new WXShareUtils();

                }
            }
        }
        return instance;
    }


    private WXShareUtils() {

    }




    public IWXAPI getWxApi() {
        if (wxApi == null && !isRegist) {
            return null;
        }
        return wxApi;
    }


    public static WXImageObject getImageObject(Bitmap bitmap) {


        WXImageObject imageObject = new WXImageObject(bitmap);

        return imageObject;


    }

    public static WXWebpageObject getWebpage(String url) {
        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl=url;
        return webpageObject;
    }


    /**
     * 把网络资源图片转化成bitmap
     *
     * @param url 网络资源图片
     * @return Bitmap
     */
    public static Bitmap GetLocalOrNetBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new URL(url).openStream(), 1024);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, 1024);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmapFromByte(byte[] data) {

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;

    }


    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    //bitmap对象转化为byte数组

    public static byte[] bmpToByteArray(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 缩放图片
     *
     * @param src       源图片
     * @param newWidth  新宽度
     * @param newHeight 新高度
     * @param recycle   是否回收
     * @return 缩放后的图片
     */
    public static Bitmap scale(Bitmap src, int newWidth, int newHeight, boolean recycle) {
        if (src == null) return null;
        Bitmap ret = Bitmap.createScaledBitmap(src, newWidth, newHeight, true);
        if (recycle && !src.isRecycled()) src.recycle();
        return ret;
    }



    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
