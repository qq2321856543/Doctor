package com.wd.news.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.wd.news.LocalBroadcastManager;
import com.wd.news.MainActivity;
import com.wd.news.util.ExampleUtil;
import com.wd.news.view.TestActivity;

import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
/**
 * 如果不自定义Receiver，则
 * 1）默认用户打开主界面
 * 2）接收不到自定义消息（通知了没有显示）
 */

/**
 * Created by issuser on 2018/3/19.
 */

public class MyReceiver extends BroadcastReceiver {
    public static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
//        Log.e(TAG, "--[MyReceiver]onReceive--" + intent.getAction() + ",extras:" + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e(TAG, "--[MyReceiver] 接收Registration Id :" + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "--[MyReceiver] 接收到推送下来的自定义消息：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);
        }else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())){
            Log.e(TAG,"----接收到推送下来的通知");
            //todo
            int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.e(TAG,"----接收到推送下来的通知ID："+notificationId);
        }else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){
            Log.e(TAG,"----用户点击了通知");
            //点击通知后，打开自定义页面
            Intent intent1 = new Intent(context, TestActivity.class);
            intent1.putExtras(bundle);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent1);
        }else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.e(TAG, "--[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        }else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "--[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
            Log.d(TAG, "--[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    //send message to mainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        //取出自定义消息内容（自定义消息手机上不显示通知）
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.e(TAG,"--message:"+message+"\nextras:"+extras);
        if (MainActivity.isForeground) {
            Intent intent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
            intent.putExtra(MainActivity.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject jsonObject = new JSONObject(extras);
                    if (jsonObject.length() > 0) {
                        intent.putExtra(MainActivity.KEY_EXTRAS, extras);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    //打印所有intent extras 数据
    private String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                Log.e(TAG, "----收到通知");
                sb.append("\nkey:" + key + ",value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
//                Log.e(TAG, "----网络发生变化");
                sb.append("\nkey:" + key + ",value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                Log.e(TAG, "----This message has no Extra data");
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ",value:[" + myKey + "-" + json.optString(myKey) + "]");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }
            } else {
                sb.append("\nkey:" + key + ",value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
