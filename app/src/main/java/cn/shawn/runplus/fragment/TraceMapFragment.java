package cn.shawn.runplus.fragment;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.List;

import cn.shawn.base.utils.CollectionUtil;
import cn.shawn.map.R;
import cn.shawn.map.base.config.AMapConfig;
import cn.shawn.map.base.BaseMapFragment;
import cn.shawn.map.base.config.AMapStyle;

public class TraceMapFragment extends BaseMapFragment {

    private static final String EXTRA_POINTS = "extra_points";

    public static TraceMapFragment create() {
        return create(null);
    }

    public static TraceMapFragment create(List<LatLng> points) {
        TraceMapFragment fragment = new TraceMapFragment();
        if (points != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(EXTRA_POINTS, new ArrayList<>(points));
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    private List<LatLng> mTracePoints = new ArrayList<>();

    @Override
    protected void getExtra() {
        if (getArguments() == null) return;
        List<LatLng> points = getArguments().getParcelableArrayList(EXTRA_POINTS);
        if (points != null) {
            mTracePoints.addAll(points);
        }
    }

    @Override
    protected AMapConfig createConfig() {
        return AMapConfig.Builder.createDefault()
                .setMapStyle(AMapStyle.createCamarnoStyle(getContext()))
                .build();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_trace;
    }

    @Override
    protected int getMapViewId() {
        return R.id.mv;
    }

    @Override
    protected int getProgressId() { return R.id.pb; }

    @Override
    protected void onMapSetupComplete() {
        if (CollectionUtil.isNotEmpty(mTracePoints)) {
            getAMap().clear();
            drawTraceLine();
            drawCurrMarker(mTracePoints.get(mTracePoints.size()-1));
            setupHighFpsLayer();
        }
    }

    public void onLocation(@NonNull LatLng location) {
        mTracePoints.add(location);
        getAMap().clear();
        drawTraceLine();
        drawCurrMarker(location);
        setupHighFpsLayer();
    }

    private void drawCurrMarker(@NonNull LatLng location) {
        if (getMapView() == null) return;

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(location);
        circleOptions.strokeColor(Color.WHITE);
        circleOptions.strokeWidth(10);
        circleOptions.radius(20);
        circleOptions.fillColor(Color.LTGRAY);

        getAMap().addCircle(circleOptions);
        getAMap().animateCamera(CameraUpdateFactory.newLatLng(location));
    }

    private void drawTraceLine() {
        if (getMapView() == null || mTracePoints.isEmpty()) return;
        PolylineOptions polylineOptions = new PolylineOptions();
        List<LatLng> latLngList = new ArrayList<>(mTracePoints);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLngList.get(0));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_sportmap_start));
        getAMap().addMarker(markerOptions);

        polylineOptions.addAll(latLngList);
        polylineOptions.color(Color.RED);
        polylineOptions.aboveMaskLayer(false);
        getAMap().addPolyline(polylineOptions);

    }

}
