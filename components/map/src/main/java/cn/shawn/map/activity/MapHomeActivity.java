package cn.shawn.map.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.particle.ParticleEmissionModule;
import com.amap.api.maps.model.particle.ParticleOverlay;
import com.amap.api.maps.model.particle.ParticleOverlayOptions;
import com.amap.api.maps.model.particle.RandomVelocityBetweenTwoConstants;
import com.amap.api.maps.model.particle.SinglePointParticleShape;
import cn.shawn.map.R;

public class MapHomeActivity extends AppCompatActivity {

    private MapView mMapView;

    private AMap mAMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_home);
        mMapView = findViewById(R.id.mv);
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        createFpsOverlay();
        showLocationPoint();
        boolean result = PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION);
        Log.i("MapHomeActivity", "onCreate: "+result);
    }

    private void showLocationPoint() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        //myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAMap.setTrafficEnabled(false);
        mAMap.setRoadArrowEnable(false);
        mAMap.setTouchPoiEnable(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }


    private ParticleOverlay mFpsOverlay;

    /**
     * 利用添加自定义粒子图层提高地图的刷新率
     */
    private void createFpsOverlay() {
        if (mFpsOverlay != null) {
            return;
        }
        ParticleOverlayOptions particleSystemOptions = new ParticleOverlayOptions();
        particleSystemOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_transparent));
        int time = 1000;
        // 设置最大数量，持续时间，是否循环
        particleSystemOptions.setMaxParticles(1);
        particleSystemOptions.setDuration(time);
        particleSystemOptions.setLoop(true);
        // 设置每个粒子 存活时间，以及发射时的状态
        particleSystemOptions.setParticleLifeTime(time);
        particleSystemOptions.setParticleStartSpeed(new RandomVelocityBetweenTwoConstants(10, 10, 10, 100, 10, 10));
        // 设置发射率
        particleSystemOptions.setParticleEmissionModule(new ParticleEmissionModule(10, 1000));
        // 设置发射位置
        particleSystemOptions.setParticleShapeModule(new SinglePointParticleShape(0f, 0f, 0, true));
        // 设置大小
        particleSystemOptions.setStartParticleSize(1, 1);
        mFpsOverlay = mAMap.addParticleOverlay(particleSystemOptions);
        mFpsOverlay.destroy();
    }
}