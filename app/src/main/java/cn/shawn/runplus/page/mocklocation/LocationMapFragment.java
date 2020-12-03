package cn.shawn.runplus.page.mocklocation;

import android.graphics.Point;
import android.util.Log;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;

import cn.shawn.map.base.BaseMapFragment;
import cn.shawn.map.base.config.AMapConfig;
import cn.shawn.map.base.config.AMapStyle;
import cn.shawn.mock.LocationMockManager;
import cn.shawn.runplus.R;
import cn.shawn.runplus.utils.SpUtil;

public class LocationMapFragment extends BaseMapFragment {

    @Override
    protected void getExtra() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_location;
    }

    @Override
    protected int getMapViewId() {
        return R.id.mv;
    }

    @Override
    protected int getProgressId() {
        return R.id.pb;
    }

    @Override
    protected AMapConfig createConfig() {
        return AMapConfig.Builder.createDefault()
                .setMaxZoomLevel(17f)
                .setMapStyle(AMapStyle.createCamarnoStyle(getContext()))
                .build();
    }

    @Override
    protected void onMapSetupComplete() {
        initStyle();
        getView().findViewById(R.id.btn_confirm).setOnClickListener(
                v -> {
                    LatLng latLng = getMapCenterPoint();
                    Log.i("LocationMapFragment", "onMapSetupComplete: " + latLng);
                    SpUtil.saveMockLocation(getContext(), latLng);
                }
        );
        getView().findViewById(R.id.btn_start_mock).setOnClickListener(
                v -> {
                    LatLng latLng = SpUtil.getMockLocation(getContext());
                    LocationMockManager.getInstance().init(getContext());
                    LocationMockManager.getInstance().startMockLocation(latLng);
                }
        );
    }

    private void initStyle() {
        getAMap().setMyLocationEnabled(true);
        MyLocationStyle style = new MyLocationStyle();
        style.myLocationIcon(BitmapDescriptorFactory.fromResource(cn.shawn.map.R.drawable.ic_center_locate));
        //初始化定位蓝点样式类
        style.radiusFillColor(android.R.color.transparent);
        style.strokeColor(android.R.color.transparent);
        style.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        getAMap().setMyLocationStyle(style);
    }

    public LatLng getMapCenterPoint() {
        int left = getMapView().getLeft();
        int top = getMapView().getTop();
        int right = getMapView().getRight();
        int bottom = getMapView().getBottom();
        // 获得屏幕点击的位置
        int x = (int) (getMapView().getX() + (right - left) / 2);
        int y = (int) (getMapView().getY() + (bottom - top) / 2);
        return getAMap().getProjection().fromScreenLocation(new Point(x, y));
    }

}
