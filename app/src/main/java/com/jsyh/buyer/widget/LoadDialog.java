package com.jsyh.buyer.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jsyh.buyer.R;
import com.jsyh.buyer.utils.ResourcesUtil;

/**
 * Created by Su on 2016/7/5.
 * 修改FragmentDialog  -> Dialog
 */
public class LoadDialog extends Dialog {

    private String loadingGif;

    private long delayMillis = 3000;

    public LoadDialog(Context context) {
        super(context, android.R.style.Theme_Light_Panel);
        View v = LayoutInflater.from(context).inflate(R.layout.loading_layout, null);
        ImageView loadingImage = (ImageView) v.findViewById(R.id.ivLoading);
//        loadingGif = context.getResources().getString(R.string.load_animation);

        Glide.with(context).load(R.drawable.loading/*ResourcesUtil.getMipmapId(context, loadingGif)*/)
                .asGif().into(loadingImage);
        setCanceledOnTouchOutside(true);
        setContentView(v);
    }
    @Override
    public void show() {
        if (!isShowing()){
            //加载框显示之后，2~3秒自动关闭
            super.show();
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            },delayMillis);*/
        }
    }

    @Override
    public void dismiss() {
        if (isShowing())
            super.dismiss();
    }


}
