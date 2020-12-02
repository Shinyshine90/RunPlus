package cn.shawn.mock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LocationMockManager {

    private static final String TAG = "LocationMockManager";

    private LocationMockManager() { }

    private static class Holder {
        static LocationMockManager sInstance = new LocationMockManager();
    }

    public static LocationMockManager getInstance() {
        return Holder.sInstance;
    }

    private LocationManager mLocationManager;

    public void init(@NonNull Context context) {
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            LocationProvider provider = mLocationManager.getProvider(LocationManager.GPS_PROVIDER);
            if (provider == null) {
                mLocationManager.addTestProvider(LocationManager.GPS_PROVIDER,
                        false,
                        false,
                        false,
                        false,
                        true,
                        true,
                        true,
                        0,
                        5);
            } else {
                mLocationManager.addTestProvider(provider.getName(),
                        provider.requiresNetwork(),
                        provider.requiresSatellite(),
                        provider.requiresCell(),
                        provider.hasMonetaryCost(),
                        provider.supportsAltitude(),
                        provider.supportsSpeed(),
                        provider.supportsBearing(),
                        provider.getPowerRequirement(),
                        provider.getAccuracy());
            }
        } catch (Exception e) {
            Log.e(TAG, "init: add provider error");
        }

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                mLocationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
            } catch (Exception e) {
                Log.e(TAG, "init: enable provider error");
            }
        }
    }

    @SuppressLint("CheckResult")
    public void startMockRoute(@NonNull Context context) {
        Observable.create((ObservableOnSubscribe<LatLonPoint>) emitter -> {
            List<LatLonPoint> points = new RouteLineFetcher(context).fetch();
            for (LatLonPoint point : points) {
                Thread.sleep(3000L);
                emitter.onNext(point);
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LatLonPoint>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LatLonPoint point) {
                        Log.i("LocationMockManager", "onNext: " + point.toString());
                        mockLocation(new LatLng(point.getLatitude(), point.getLongitude()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("LocationMockManager", "onError: " + e.toString());
                        Toast.makeText(context, "算路失败", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void mockLocation(LatLng latLng) {
        Location locationGps = new Location(LocationManager.GPS_PROVIDER);
        locationGps.setLatitude(latLng.latitude);
        locationGps.setLongitude(latLng.longitude);
        locationGps.setAccuracy(1f);
        locationGps.setTime(SystemClock.elapsedRealtime());
        locationGps.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        mLocationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, locationGps);
    }
}
