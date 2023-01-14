package com.jsyh.buyer.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsyh.buyer.R;
import com.jsyh.buyer.utils.AppUtils;


public class CustomDialog extends Dialog implements DialogInterface.OnShowListener {
    private Context context;
    private View.OnClickListener left_listener, right_listener;
    private String title, left_text = "取消", right_text = "确定";

    private TextView tv_left;
    private TextView tv_right;
    private TextView title1;

    private LinearLayout layout2;

    private TextView tvContent;
    private RelativeLayout normalLayout;
    private TextView forceUpdateBtn;


    public CustomDialog(Context context, String title, String left_text,
                        String right_text, View.OnClickListener left_listener,
                        View.OnClickListener right_listener) {
        super(context, R.style.mystyle);
        this.context = context;
        this.left_listener = left_listener;
        this.right_listener = right_listener;
        this.title = title;
        if (!TextUtils.isEmpty(left_text)) {
            this.left_text = left_text;
        }
        if (!TextUtils.isEmpty(right_text)) {
            this.right_text = right_text;
        }
        initView();
        this.setOnShowListener(this);
    }

    public CustomDialog(Context context, String title,
                        View.OnClickListener left_listener,
                        View.OnClickListener right_listener) {
        this(context, title, null, null, left_listener, right_listener);
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.alert_dialog, null);
        title1 = (TextView) layout.findViewById(R.id.text);
        title1.setText(title);



        tv_left = (TextView) layout.findViewById(R.id.tv_left);
        tv_left.setText(left_text);
        tv_right = (TextView) layout.findViewById(R.id.tv_right);
        tv_right.setText(right_text);
        getWindow().setLayout(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        setContentView(layout);
        if (left_listener == null) {
            tv_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                }
            });
        } else {
            tv_left.setOnClickListener(left_listener);
        }
        if (right_listener != null) {
            tv_right.setOnClickListener(right_listener);
        }

        layout2 = (LinearLayout) layout.findViewById(R.id.layout2);
        tvContent = (TextView) layout.findViewById(R.id.tvContent);
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());

        normalLayout = (RelativeLayout) layout.findViewById(R.id.rlNormalPanle);
        forceUpdateBtn = (TextView) layout.findViewById(R.id.btnForceUpdate);

        if (right_listener != null) {

            forceUpdateBtn.setOnClickListener(right_listener);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        int screenWidth = AppUtils.screenWidth(getContext());
        int screenHeight = AppUtils.screenHeight(getContext());
        params.width = (screenWidth / 6) * 5;
        params.height = (screenHeight / 5) * 3;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);

    }

    public void setRight_listener(
            View.OnClickListener right_listener) {
        this.right_listener = right_listener;
    }

    public void show(Object tag, String title, String left_text,
                     String right_text) {
        tv_right.setTag(tag);
        tv_left.setTag(tag);
        show(title, left_text, right_text);
    }

    public void show(String title, String left_text, String right_text) {
        this.title = title;
        this.left_text = left_text;
        this.right_text = right_text;
        title1.setText(title);
        ((TextView) findViewById(R.id.tvTitle)).setText(title);

        tv_right.setText(right_text);
        tv_left.setText(left_text);
        if (!((Activity) context).isFinishing())
            super.show();
    }

    public void show(String title, String left_text, String right_text, String content) {
        this.title = title;
        this.left_text = left_text;
        this.right_text = right_text;
//        title1.setText(title);
        title1.setVisibility(View.GONE);
//        ((TextView) layout2.findViewById(R.id.tvTitle)).setText(title);
        ((TextView) findViewById(R.id.tvTitle)).setText(title);
        tv_right.setText(right_text);
        tv_left.setText(left_text);
        tvContent.setText(content);
        layout2.setVisibility(View.VISIBLE);
        if (!((Activity) context).isFinishing())
            super.show();
    }

    public void show2(String content) {
        this.title1.setVisibility(View.GONE);
        normalLayout.setVisibility(View.GONE);

        this.layout2.setVisibility(View.VISIBLE);

        this.forceUpdateBtn.setVisibility(View.VISIBLE);

        tvContent.setText(content);
        super.show();
    }




    public void resetSize() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        int screenWidth = AppUtils.screenWidth(getContext());
        int screenHeight = AppUtils.screenHeight(getContext());
        params.width = (screenWidth / 6) * 5;
        params.height = (screenHeight / 5) * 3;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);

    }


    @Override
    public void onShow(DialogInterface dialog) {
        resetSize();
    }
}
