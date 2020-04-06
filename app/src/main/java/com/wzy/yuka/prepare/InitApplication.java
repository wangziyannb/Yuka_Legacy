package com.wzy.yuka.prepare;

import android.app.Application;

import com.lzf.easyfloat.EasyFloat;

public class InitApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EasyFloat.init(this, false);
    }

}
