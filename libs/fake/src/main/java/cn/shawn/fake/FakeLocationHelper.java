package cn.shawn.fake;

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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.shawn.component.map.utils.GCJ2WGSUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FakeLocationHelper {

    private static final String TAG = "LocationMockManager";

    private static final int TYPE_FAKE_LOCATION = 0;

    private static final int TYPE_FAKE_ROUTE = 1;

    private Map<Integer, Disposable> mRunningTask = new ConcurrentHashMap<>();

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
    public void startFakeTrack(@NonNull Context context) {
        Observable.create((ObservableOnSubscribe<LatLonPoint>) emitter -> {
            List<LatLonPoint> points = new RouteLineFetcher(context).fetch();
            for (LatLonPoint point : points) {
                Thread.sleep(1000L);
                emitter.onNext(point);
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .subscribe(new Observer<LatLonPoint>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        releaseTask(TYPE_FAKE_ROUTE);
                        mRunningTask.put(TYPE_FAKE_ROUTE, d);
                    }

                    @Override
                    public void onNext(LatLonPoint point) {
                        Log.i("LocationMockManager", "onNext: " + point.toString());
                        LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                        postFakeLocation(GCJ2WGSUtils.convertToWGSLat(latLng));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("LocationMockManager", "onError: " + e.toString());
                        Toast.makeText(context, "算路失败", Toast.LENGTH_LONG).show();
                        mRunningTask.remove(TYPE_FAKE_ROUTE);
                    }

                    @Override
                    public void onComplete() {
                        mRunningTask.remove(TYPE_FAKE_ROUTE);
                    }
                });
    }

    public void startFakeLocation(LatLng latLng) {
        LatLng wcjlatLng = GCJ2WGSUtils.convertToWGSLat(latLng);
        Observable.create((ObservableOnSubscribe<LatLng>) emitter -> {
            for (int i = 0; i < Integer.MAX_VALUE / 2; i++) {
                emitter.onNext(wcjlatLng);
                Thread.sleep(100);
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .subscribe(new Observer<LatLng>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        releaseTask(TYPE_FAKE_LOCATION);
                        mRunningTask.put(TYPE_FAKE_LOCATION, d);
                    }

                    @Override
                    public void onNext(LatLng point) {
                        Log.i("LocationMockManager", "onNext: " + point.toString());
                        postFakeLocation(point);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("LocationMockManager", "onError: " + e.toString());
                        mRunningTask.remove(TYPE_FAKE_LOCATION);
                    }

                    @Override
                    public void onComplete() {
                        mRunningTask.remove(TYPE_FAKE_LOCATION);
                    }
                });
    }

    public void postFakeLocation(LatLng latLng) {
        Location locationGps = new Location(LocationManager.GPS_PROVIDER);
        locationGps.setLatitude(latLng.latitude);
        locationGps.setLongitude(latLng.longitude);
        locationGps.setAccuracy(1f);
        locationGps.setTime(SystemClock.elapsedRealtime());
        locationGps.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        mLocationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, locationGps);
    }

    public void releaseTask(int type) {
        Disposable task = mRunningTask.get(type);
        if (task != null && !task.isDisposed()) {
            task.dispose();
        }
    }

    public void release() {
        Collection<Disposable> tasks = mRunningTask.values();
        for (Disposable d : tasks) {
            if (!d.isDisposed()) d.dispose();
        }
    }
}
