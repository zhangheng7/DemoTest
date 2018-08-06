package com.example.administrator.demotest.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * @author zhangheng
 * @date 2018/7/13
 */

public class Myapp extends Application {
    @Override
    public void onCreate() {
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate();
    }
}
