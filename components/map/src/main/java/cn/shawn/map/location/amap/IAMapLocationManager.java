package cn.shawn.map.location.amap;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

public interface IAMapLocationManager {

    void init(@NonNull Context context);

    void start();

    void stop();

    void release();
}
