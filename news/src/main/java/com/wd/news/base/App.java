package com.wd.news.base;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;

public class App extends Application {

    public static final String TAG = "TEST_JPUSH";

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);  // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this); //初始化

        JMessageClient.setDebugMode(true);
        JMessageClient.init(this);//初始化SDK
    }
}
