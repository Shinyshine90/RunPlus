package cn.shawn.runplus;

import android.app.Application;

import cn.shawn.map.MapComponentInit;

public class RunPlusApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MapComponentInit.init(this);
    }
}
