package cn.shawn.runplus;

import android.app.Application;

import cn.shawn.map.MapComponentInit;
import cn.shawn.map.location.amap.AMapLocationManager;

public class RunPlusApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AMapLocationManager.getInstance().init(this);
        MapComponentInit.init(this);
    }
}
