package com.jsyh.buyer.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.jsyh.buyer.data.local.MsgDao;
import com.jsyh.buyer.ui.message.MessageActivity;
import com.jsyh.pushlibrary.MessageModel;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	processCustomMessage(context, bundle);
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

			processCustomMessage(context, bundle);


        	
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            
        	//打开自定义的Activity
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

			try {
				JSONObject jsonObject = new JSONObject(extras);

				if (jsonObject.has("type")) {
					String type = jsonObject.getString("type");
					if (!TextUtils.isEmpty(type)) {
						if ("2".equals(type)) {
							if (jsonObject.has("url") && !TextUtils.isEmpty(jsonObject.getString("url"))) {
								String url = jsonObject.getString("url");
								String message = bundle.getString(JPushInterface.EXTRA_ALERT);
								Bundle extra = new Bundle();
								extra.putString("url", url);
								extra.putString("title", message);
								extra.putInt("type", 2);

								Intent webIntent = new Intent();
								webIntent.putExtras(extra);
								webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
								webIntent.setClassName(context,"com.jsyh.buyer.ui.WebActivity");
								context.startActivity(webIntent);


							}
						}
					}
				}else {
					//跳转到消息详情
					Intent i = new Intent(context, MessageActivity.class);

					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
					context.startActivity(i);
				}


			} catch (JSONException e) {
				e.printStackTrace();
			}




		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {


		MessageModel model = new MessageModel();
		String message = bundle.getString(JPushInterface.EXTRA_ALERT);
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		String currentTime="";


		try {
			if (!TextUtils.isEmpty(extras)) {
                JSONObject jsonObject = new JSONObject(extras);

				if (jsonObject.has("type") && "2".equals(jsonObject.getString("type"))){
					return;
				}

                if (jsonObject.has("currentTime")) {
                     currentTime = jsonObject.getString("currentTime");
					if (!TextUtils.isEmpty(currentTime)) {

//						Date date = new SimpleDateFormat("yyyy/MM/dd",Locale.getDefault()).parse(currentTime);
//						String format = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
//						model.setTime(format);
						model.setTime(currentTime);
					}
                }

				model.setRead(-1);
				model.setContent(message);

				MsgDao dao = new MsgDao(context);
				dao.add(model);

				EventBus.getDefault().post(model);
            }
		} catch (JSONException e) {
			e.printStackTrace();
		}


	}
}
