package cn.shawn.map.practise;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CustomMapStyleOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.particle.ParticleEmissionModule;
import com.amap.api.maps.model.particle.ParticleOverlay;
import com.amap.api.maps.model.particle.ParticleOverlayOptions;
import com.amap.api.maps.model.particle.RandomVelocityBetweenTwoConstants;
import com.amap.api.maps.model.particle.SinglePointParticleShape;

import java.io.File;
import cn.shawn.map.utils.FileUtil;
import cn.shawn.map.R;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TestMapHomeActivity extends AppCompatActivity {

    public static final String TAG = "TestMapHomeActivity";

    private static final String[] sStylePaths = {
            "map/style/macarno/style.data",
            "map/style/macarno/style_extra.data"
    };

    private View mMapMask;

    private ProgressBar mProgressBar;

    private MapView mMapView;

    private AMap mAMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_home);
        mMapView = findViewById(R.id.mv);
        mMapView.onCreate(savedInstanceState);
        mMapMask = findViewById(R.id.v_mask);
        mAMap = mMapView.getMap();
        mProgressBar = findViewById(R.id.pb);
        loadMap();
    }

    private void loadMap() {
        Observable.just(1)
                .map(checkStyleDataExist() ? loadFromSdcard() : loadFromAssets())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mMapLoadObserver);
    }

    private boolean checkStyleDataExist() {
        File style = new File(assetToFilePath(sStylePaths[0]));
        File extra = new File(assetToFilePath(sStylePaths[1]));
        return style.isFile() && style.length() > 0
                && extra.isFile() && style.length() > 0;
    }

    private Function<Integer, File[]> loadFromSdcard() {
        return integer -> new File[] {
            new File(assetToFilePath(sStylePaths[0])),
            new File(assetToFilePath(sStylePaths[1]))
        };
    }

    private Function<Integer, File[]> loadFromAssets() {
        return integer -> {
            Context context = TestMapHomeActivity.this;
            File[] files = new File[2];
            files[0] = FileUtil.copyAssetsToData(context, sStylePaths[0], assetToFilePath(sStylePaths[0]));
            files[1] = FileUtil.copyAssetsToData(context, sStylePaths[1], assetToFilePath(sStylePaths[1]));
            return files;
        };
    }

    private String assetToFilePath(String assetPath) {
        return getFilesDir().getPath() +  File.separator + assetPath;
    }

    private Observer<File[]> mMapLoadObserver = new Observer<File[]>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            mProgressBar.setVisibility(View.VISIBLE);
            mMapView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onNext(File @NonNull [] files) {
            showCustomStyle(files);
            showLocationPoint();
            setUpZoomSize();
            createFpsOverlay();

            mMapMask.animate().alpha(0f).setDuration(2000).start();
            mMapView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);

        }

        @Override
        public void onError(@NonNull Throwable e) {
            Log.e(TAG, "onError: "+ e.getMessage() );
            e.printStackTrace();
            showLocationPoint();
            setUpZoomSize();
            createFpsOverlay();

            mMapView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onComplete() { }
    };

    private void setUpZoomSize() {
        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAMap.setTrafficEnabled(false);
        mAMap.setRoadArrowEnable(false);
        mAMap.setTouchPoiEnable(false);

        mAMap.setMinZoomLevel(12.5f);
        mAMap.setMaxZoomLevel(16f);

        mAMap.moveCamera(CameraUpdateFactory.zoomTo(15f));
    }

    private void showCustomStyle(File[] files) {
        mAMap.setCustomMapStyle(
                new CustomMapStyleOptions()
                .setStyleDataPath(files[0].getPath())
                .setStyleExtraPath(files[1].getPath())
        );
    }

    private void showLocationPoint() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_center_locate));
        //初始化定位蓝点样式类
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeColor(android.R.color.transparent);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。

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
    }
}