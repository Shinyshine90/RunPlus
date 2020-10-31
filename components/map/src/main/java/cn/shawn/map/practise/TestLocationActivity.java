package cn.shawn.map.practise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;

import cn.shawn.map.R;
import cn.shawn.map.location.amap.AMapLocationHelper;
import cn.shawn.map.location.amap.LocationUtils;

public class TestLocationActivity extends AppCompatActivity implements AMapLocationListener {

    public static final String TAG = "MainActivityMAP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_location);
        startLocation();
    }

    private void startLocation() {
        AMapLocationHelper helper = new AMapLocationHelper(this, this);
        helper.initLocation();
        helper.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        String info = LocationUtils.getLocationInfo(aMapLocation);
        ((TextView)findViewById(R.id.tv_show_info)).setText(info);
        Log.i(TAG, "onLocationChanged: "+aMapLocation);
    }
}