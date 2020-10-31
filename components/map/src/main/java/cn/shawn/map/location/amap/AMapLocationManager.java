package cn.shawn.map.location.amap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;

import java.util.HashSet;
import java.util.Set;

public class AMapLocationManager implements IAMapLocationManager, AMapLocationListener {

    private AMapLocationManager() {}

    private static class Holder {
        @SuppressLint("StaticFieldLeak")
        static AMapLocationManager sInstance = new AMapLocationManager();
    }

    public static AMapLocationManager getInstance() {
        return Holder.sInstance;
    }

    private Context mContext;

    private Status mStatus = Status.INIT;

    private AMapLocationHelper mLocationHelper;

    private final Set<AMapLocationListener> mLocationListeners = new HashSet<>();

    public void registerListener(AMapLocationListener listener) {
        synchronized (mLocationListeners) {
            mLocationListeners.add(listener);
        }
    }

    public void unregisterListener(AMapLocationListener listener) {
        synchronized (mLocationListeners) {
            mLocationListeners.remove(listener);
        }
    }

    @Override
    public void init(@NonNull Context context) {
        if (checkStatus(Status.INIT)) {
            return;
        }
        mContext = context.getApplicationContext();
        mLocationHelper = new AMapLocationHelper(mContext, this);
        mLocationHelper.initLocation();
        mStatus = Status.READY;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        synchronized (mLocationListeners) {
            for (AMapLocationListener listener : mLocationListeners) {
                listener.onLocationChanged(aMapLocation);
            }
        }
    }

    @Override
    public void start() {
        if (checkStatus(Status.READY)) {
            return;
        }
        mStatus = Status.RUNNING;
        mLocationHelper.startLocation();
    }

    @Override
    public void stop() {
        if (checkStatus(Status.RUNNING)) {
            return;
        }
        mStatus = Status.READY;
        mLocationHelper.stopLocation();
    }

    @Override
    public void release() {
        mLocationHelper.release();
        mLocationHelper = null;
        mStatus = Status.INIT;
    }

    private boolean checkStatus(Status status) {
        return mStatus != status;
    }

    enum Status {
        INIT,
        READY,
        RUNNING
    }

}
