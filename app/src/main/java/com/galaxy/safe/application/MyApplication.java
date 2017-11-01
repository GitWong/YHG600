package com.galaxy.safe.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Dell on 2016/6/16.
 */
public class MyApplication extends Application {
//    RefWatcher install;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
      /*  if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        install = LeakCanary.install(this);*/
    }


}
