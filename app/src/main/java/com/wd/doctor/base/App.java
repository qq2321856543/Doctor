package com.wd.doctor.base;


import android.support.multidex.MultiDex;

import com.wd.common.Base.BaseApplication;

public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
