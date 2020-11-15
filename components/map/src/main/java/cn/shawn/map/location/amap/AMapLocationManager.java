package cn.shawn.map.location.amap;

import android.content.Context;
import androidx.annotation.NonNull;
import com.amap.api.location.AMapLocationListener;

// TODO: 11/14/20 CPU 休眠

public class AMapLocationManager implements IAMapLocationManager  {

    private Context mContext;

    private Status mStatus = Status.INIT;

    private AMapLocationHelper mLocationHelper;

    @Override
    public void init(@NonNull Context context, @NonNull AMapLocationListener listener) {
        if (checkStatus(Status.INIT)) {
            return;
        }
        mContext = context.getApplicationContext();
        mLocationHelper = new AMapLocationHelper(mContext, listener);
        mLocationHelper.initLocation();
        mStatus = Status.READY;
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
