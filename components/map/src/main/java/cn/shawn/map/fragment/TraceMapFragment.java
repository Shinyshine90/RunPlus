package cn.shawn.map.fragment;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorCreator;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.shawn.map.R;
import cn.shawn.map.base.config.AMapConfig;
import cn.shawn.map.base.BaseMapFragment;
import cn.shawn.map.base.config.AMapStyle;

public class TraceMapFragment extends BaseMapFragment {

    public static TraceMapFragment create() {
        return new TraceMapFragment();
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

    private List<AMapLocation> mTraceLocations = new LinkedList<>();

    public void onLocation(@NonNull AMapLocation location) {
        mTraceLocations.add(location);
        getAMap().clear();
        drawTraceLine();
        drawCurrMarker(location);
        setupHighFpsLayer();
    }

    private void drawCurrMarker(@NonNull AMapLocation location) {
        if (getMapView() == null) return;
        LatLng currLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(currLatLng);
        circleOptions.strokeColor(Color.WHITE);
        circleOptions.strokeWidth(10);
        circleOptions.radius(20);
        circleOptions.fillColor(Color.LTGRAY);

        getAMap().addCircle(circleOptions);
        getAMap().animateCamera(CameraUpdateFactory.newLatLng(currLatLng));
    }

    private void drawTraceLine() {
        if (getMapView() == null || mTraceLocations.isEmpty()) return;
        PolylineOptions polylineOptions = new PolylineOptions();
        List<LatLng> latLngList = new ArrayList<>(mTraceLocations.size());
        for (AMapLocation location : mTraceLocations) {
            latLngList.add(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLngList.get(0));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_sportmap_start));
        getAMap().addMarker(markerOptions);

        polylineOptions.addAll(latLngList);
        polylineOptions.color(Color.LTGRAY);
        polylineOptions.aboveMaskLayer(false);
        getAMap().addPolyline(polylineOptions);

    }

}
