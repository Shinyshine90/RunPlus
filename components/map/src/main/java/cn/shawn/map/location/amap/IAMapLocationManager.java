package cn.shawn.map.location.amap;

import android.content.Context;
import androidx.annotation.NonNull;

import com.amap.api.location.AMapLocationListener;

public interface IAMapLocationManager {

    void init(@NonNull Context context, @NonNull AMapLocationListener listener);

    void start();

    void stop();

    void release();
}
