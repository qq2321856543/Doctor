package com.wd.common.base;

import android.app.Application;
import android.content.Context;

//import com.alibaba.android.arouter.launcher.ARouter;

public class BaseApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
//        ARouter.init(this);
        context = getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
