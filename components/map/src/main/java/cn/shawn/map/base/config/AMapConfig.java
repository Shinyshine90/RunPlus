package cn.shawn.map.base.config;

import java.io.Serializable;

public class AMapConfig implements Serializable {

    public boolean myLocationEnable;

    public boolean trafficEnable;

    public boolean roadArrowEnable;

    public boolean touchPoiEnable;

    public boolean highFpsSupport;

    public float minZoomLevel;

    public float maxZoomLevel;

    public float initZoomLevel;

    public boolean uiZoomControlEnable;

    public boolean uiMyLocationButtonEnable;

    public AMapStyle mapStyle;

    private AMapConfig(Builder builder) {
        myLocationEnable = builder.myLocationEnable;
        uiMyLocationButtonEnable = builder.uiMyLocationButtonEnable;
        trafficEnable = builder.trafficEnable;
        roadArrowEnable = builder.roadArrowEnable;
        touchPoiEnable = builder.touchPoiEnable;
        minZoomLevel = builder.minZoomLevel;
        maxZoomLevel = builder.maxZoomLevel;
        initZoomLevel = builder.initZoomLevel;
        highFpsSupport = builder.highFpsSupport;
        mapStyle = builder.mapStyle;
    }

    public static class Builder {

       private boolean myLocationEnable = false;

       private boolean uiMyLocationButtonEnable = false;

       private boolean trafficEnable = false;

       private boolean roadArrowEnable = false;

       private boolean touchPoiEnable = false;

       private boolean highFpsSupport = true;

       private float minZoomLevel = 12.5f;

       private float maxZoomLevel = 16f;

       private float initZoomLevel = 15;

       private AMapStyle mapStyle = null;

        public Builder setMyLocationEnable(boolean myLocationEnable) {
            this.myLocationEnable = myLocationEnable;
            return this;
        }

        public Builder setTrafficEnable(boolean trafficEnable) {
            this.trafficEnable = trafficEnable;
            return this;
        }

        public Builder setRoadArrowEnable(boolean roadArrowEnable) {
            this.roadArrowEnable = roadArrowEnable;
            return this;
        }

        public Builder setTouchPoiEnable(boolean touchPoiEnable) {
            this.touchPoiEnable = touchPoiEnable;
            return this;
        }

        public Builder setMinZoomLevel(float minZoomLevel) {
            this.minZoomLevel = minZoomLevel;
            return this;
        }

        public Builder setMaxZoomLevel(float maxZoomLevel) {
            this.maxZoomLevel = maxZoomLevel;
            return this;
        }

        public Builder setInitZoomLevel(float initZoomLevel) {
            this.initZoomLevel = initZoomLevel;
            return this;
        }

        public Builder setHighFpsSupport(boolean highFpsSupport) {
            this.highFpsSupport = highFpsSupport;
            return this;
        }

        public Builder setMapStyle(AMapStyle mapStyle) {
            this.mapStyle = mapStyle;
            return this;
        }

        public static AMapConfig.Builder createDefault() {
            return new Builder();
        }

        public AMapConfig build() {
            return new AMapConfig(this);
        }
    }

}
