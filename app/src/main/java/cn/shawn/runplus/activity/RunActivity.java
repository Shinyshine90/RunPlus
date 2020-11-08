package cn.shawn.runplus.activity;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocationListener;

import cn.shawn.map.fragment.TraceMapFragment;
import cn.shawn.map.location.amap.AMapLocationManager;
import cn.shawn.runplus.R;

public class RunActivity extends AppCompatActivity {

    private final String TAG_FRAGMENT = "TraceMapFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        startLocation();
        initMapFragment();
    }

    private void startLocation() {
        AMapLocationManager.getInstance().registerListener(mLocationListener);
        AMapLocationManager.getInstance().start();
    }

    private AMapLocationListener mLocationListener = aMapLocation -> {
        if (aMapLocation == null) return;
        TraceMapFragment traceFragment = getFragment();
        if (traceFragment != null) {
            traceFragment.onLocation(aMapLocation);
        }
        Log.i("RunActivity", ": "+aMapLocation.toString());
    };

    private void initMapFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_map, TraceMapFragment.create(), TAG_FRAGMENT)
                .commitNowAllowingStateLoss();
    }

    private TraceMapFragment getFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if (fragment instanceof TraceMapFragment) {
            return (TraceMapFragment) fragment;
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AMapLocationManager.getInstance().unregisterListener(mLocationListener);
    }
}