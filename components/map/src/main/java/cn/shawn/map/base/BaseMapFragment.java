package cn.shawn.map.base;

import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import cn.shawn.map.R;
import cn.shawn.map.base.config.AMapConfig;
import cn.shawn.map.base.config.AMapStyle;
import cn.shawn.map.utils.FileUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public abstract class BaseMapFragment extends Fragment {

    private static final String TAG = "BaseMapFragment";

    private View mRoot;

    private AMap mAMap;

    private MapView mMapView;

    private View mProgressView;

    private ParticleOverlay mFpsOverlay;

    private Disposable mLoadDisposable;

    private AMapConfig mMapConfig;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtra();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mRoot = inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupMapView(savedInstanceState);
    }

    protected abstract void getExtra();

    @LayoutRes
    protected abstract int getLayoutId();

    @IdRes
    protected abstract int getMapViewId();

    @IdRes
    protected abstract int getProgressId();

    protected abstract void onMapSetupComplete();

    protected AMapConfig createConfig() {
        return AMapConfig.Builder.createDefault().build();
    }

    protected AMapConfig getConfig() {
        if (mMapConfig == null) {
            mMapConfig = createConfig();
        }
        return mMapConfig;
    }

    private void setupMapView(Bundle savedInstanceStat) {
        mMapView = mRoot.findViewById(getMapViewId());
        mMapView.onCreate(savedInstanceStat);
        mAMap = mMapView.getMap();

        mLoadDisposable = loadMapView();
    }

    private void setupMapCommonly() {
        setupUiSettings();
        setUpZoomSize();
        setupMyLocation();
        setupHighFpsLayer();
        onMapSetupComplete();
    }

    private Disposable loadMapView() {
        return Observable.just(getConfig())
                .filter(shouldLoadStyle())
                .map(loadStyleData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(config -> {
                    dismissProgress();
                    setupStyle();
                    setupMapCommonly();
                }, throwable -> {
                    throwable.printStackTrace();
                    dismissProgress();
                    setupMapCommonly();
                });
    }

    private Predicate<AMapConfig> shouldLoadStyle() {
        return config -> {
            boolean hasStyleData = getConfig().mapStyle != null
                    && getConfig().mapStyle.hasStyleData();
            boolean isValidData = getConfig().mapStyle != null
                    && getConfig().mapStyle.checkStyleDataValid();
            if (!hasStyleData) {
                setupMapCommonly();
                return false;
            } else if (isValidData) {
                setupStyle();
                setupMapCommonly();
                return false;
            } else {
                return true;
            }
        };
    }

    private Function<AMapConfig, AMapConfig> loadStyleData() {
        return config -> {
            showProgress();
            AMapStyle mapStyle = config.mapStyle;
            String[] stylePaths= mapStyle.getStylePath();
            FileUtil.copyAssetsToData(getContext(), mapStyle.styleDataAsset, stylePaths[0]);
            FileUtil.copyAssetsToData(getContext(), mapStyle.styleExtraAsset, stylePaths[1]);
            return config;
        };
    }

    private void setupStyle() {
        AMapStyle mapStyle = getConfig().mapStyle;
        mAMap.setCustomMapStyle(
                new CustomMapStyleOptions()
                        .setStyleDataPath(mapStyle.getStylePath()[0])
                        .setStyleExtraPath(mapStyle.getStylePath()[1])
        );
    }

    private void setupUiSettings() {
        mAMap.getUiSettings().setZoomControlsEnabled(getConfig().uiZoomControlEnable);
        mAMap.getUiSettings().setMyLocationButtonEnabled(getConfig().uiMyLocationButtonEnable);
    }

    private void setUpZoomSize() {
        mAMap.setTrafficEnabled(getConfig().trafficEnable);
        mAMap.setRoadArrowEnable(getConfig().roadArrowEnable);
        mAMap.setTouchPoiEnable(getConfig().touchPoiEnable);

        mAMap.setMinZoomLevel(getConfig().minZoomLevel);
        mAMap.setMaxZoomLevel(getConfig().maxZoomLevel);

        mAMap.moveCamera(CameraUpdateFactory.zoomTo(getConfig().initZoomLevel));
    }

    private void setupMyLocation() {
        mAMap.setMyLocationEnabled(getConfig().myLocationEnable);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        if (!getConfig().myLocationEnable) {
            return;
        }
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_center_locate));
        //初始化定位蓝点样式类
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeColor(android.R.color.transparent);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
    }

    protected void setupHighFpsLayer() {
        if (!getConfig().highFpsSupport) {
            return;
        }
        if (mFpsOverlay != null) {
            mFpsOverlay.destroy();
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

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapView.onDestroy();
        mMapView = null;
        mAMap = null;
        if (mLoadDisposable != null) {
            mLoadDisposable.dispose();
        }
    }

    protected MapView getMapView() {
        return mMapView;
    }

    protected AMap getAMap() {
        return mAMap;
    }

    private View getProgressView() {
        if (mProgressView == null) {
            mProgressView = mRoot.findViewById(getProgressId());
        }
        return mProgressView;
    }

    protected void showProgress() {
        if (getProgressView() != null) {
            getProgressView().setVisibility(View.VISIBLE);
        }
    }

    protected void dismissProgress() {
        if (getProgressView() != null) {
            getProgressView().setVisibility(View.GONE);
        }
    }

}