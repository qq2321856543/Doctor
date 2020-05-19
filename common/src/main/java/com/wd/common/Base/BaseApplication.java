package com.wd.common.Base;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

//import com.alibaba.android.arouter.launcher.ARouter;

public class BaseApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //这2个必须要在初始化之前开启。These two lines must be written before init, otherwise these configurations will be //invalid in the init process
        ARouter.openLog();
        ARouter.openDebug();
        //  Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version //needs to be closed, otherwise there is a security risk)
        ARouter.init(this);   //初始化SDK   As early as possible, it is recommended to initialize in the
    }
    public static Context getContext(){
        return context;
    }
}
