package cn.shawn.map.location.amap;

import android.content.Context;
import androidx.annotation.NonNull;

public interface IAMapLocationManager {

    void init(@NonNull Context context);

    void start();

    void stop();

    void release();
}
