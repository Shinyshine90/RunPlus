package cn.shawn.runplus.manager.callback;

import com.amap.api.location.AMapLocation;

public interface LocationProvider {

    void onLocation(AMapLocation latLng);

    void registerLocationCallback(LocationCallback callback);

    void unregisterLocationCallback(LocationCallback callback);
}
