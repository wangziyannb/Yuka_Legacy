package com.wzy.yuka;

import android.app.Application;

import com.lzf.easyfloat.EasyFloat;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EasyFloat.init(this, false);
    }
}
