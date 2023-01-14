package com.jsyh.buyer.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jsyh.buyer.R;


public class SingleButtonDialog extends Dialog {
	private Context context;
	private View.OnClickListener listener;
	private String title, ok_text = "确定";
	private TextView tv_ok;
	private TextView mContent;

	private TextView mTitle;

	private TextView mSite;		// 公司网址
	private TextView mCompany;	//公司名字


	public SingleButtonDialog(Context context, String title, View.OnClickListener listener) {
		super(context, R.style.Transparent);
		this.context = context;
		this.listener = listener;
		this.title = title;
		initView();
	}

	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.single_alert_dialog, null);
		mContent = (TextView) layout.findViewById(R.id.tvContent);

		tv_ok = (TextView) layout.findViewById(R.id.tv_ok);
		tv_ok.setText(ok_text);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		setContentView(layout);
		setCancelable(false);
		if (listener == null) {
			tv_ok.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					hide();
				}
			});
		}else {
			tv_ok.setOnClickListener(this.listener);
		}


		mTitle = (TextView) layout.findViewById(R.id.tvTitle);

		mSite = (TextView) layout.findViewById(R.id.tvSite);
		mCompany = (TextView) layout.findViewById(R.id.tvCompany);



	}

	public void show(String title, String content, String site, String company, String ok_text) {
		this.title = title;
		this.ok_text = ok_text;
		mTitle.setText(title);

		mContent.setText(content);
		mSite.setText(site);
		mCompany.setText(company);

		tv_ok.setText(ok_text);
		super.show();
	}
}
