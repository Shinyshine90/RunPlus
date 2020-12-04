package cn.shawn.runplus.page.fakelocation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.amap.api.maps.model.LatLng;

import cn.shawn.fake.FakeGpsService;
import cn.shawn.runplus.R;

public class FakeLocationActivity extends AppCompatActivity {

    public static final String TAG_FRAGMENT = "MapFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_location);
        initMap();
    }

    private void initMap() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_map, new FakeLocationFragment(), TAG_FRAGMENT)
                .commitAllowingStateLoss();
    }

    public void startFakeLocation(LatLng latLng) {
        startService(new Intent(this, FakeGpsService.class));
        bindService(new Intent(this, FakeGpsService.class),
                new MyServiceConnection(latLng), BIND_AUTO_CREATE);
    }

    private static class MyServiceConnection implements ServiceConnection {

        private LatLng mLatlng;

        public MyServiceConnection(LatLng latlng) {
            this.mLatlng = latlng;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            FakeGpsService.FakeGpsBinder binder = (FakeGpsService.FakeGpsBinder) service;
            binder.startFakeLocation(mLatlng);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }


}