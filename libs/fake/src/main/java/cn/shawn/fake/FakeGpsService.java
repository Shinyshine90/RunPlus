package cn.shawn.fake;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.amap.api.maps.model.LatLng;

public class FakeGpsService extends Service {

    public static final String TAG = "FakeGpsService";

    private FakeNotificationHelper mNotifyHelper;

    private FakeLocationHelper mFakeLocationHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotifyHelper = new FakeNotificationHelper();
        mNotifyHelper.startForeground(this);

        mFakeLocationHelper = new FakeLocationHelper();
        mFakeLocationHelper.init(this);
    }

    private boolean startFakeLocation(LatLng latLng) {
        mFakeLocationHelper.startFakeLocation(latLng);
        return true;
    }
    
    private boolean startFakeRoute() {
        mFakeLocationHelper.startFakeTrack(this);
        return true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new FakeGpsBinder(this);
    }

    public static class FakeGpsBinder extends Binder {

        private FakeGpsService mService;

        public FakeGpsBinder(FakeGpsService service) {
            mService = service;
        }

        public boolean startFakeLocation(LatLng latLng) {
            return mService.startFakeLocation(latLng);
        }
        
        public boolean startFakeRoute() {
            return mService.startFakeRoute();
        }

    }

}
