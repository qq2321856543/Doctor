package com.wd.news.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.wd.news.R;
import com.wd.news.view.ConversationDetailActivity;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.NotificationClickEvent;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        JMessageClient.registerEventReceiver(BaseActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(BaseActivity.this);
    }

    public void onEvent(NotificationClickEvent event) {
        Intent notificationIntent = new Intent(this, ConversationDetailActivity.class);
        notificationIntent.putExtra("name", event.getMessage().getFromName());
        Log.e("onEvent","----"+event.getMessage().getFromName());
        this.startActivity(notificationIntent);// 跳转到指定页面
    }
}
