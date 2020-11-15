package cn.shawn.runplus.database;

import com.amap.api.location.AMapLocation;

public class RunModelFactory {

    enum SDK_TYPE {
        GOOGLE(0), GD(1), BD(2);

        public int type;

        SDK_TYPE(int type) {
            this.type = type;
        }
    }

    public static RunPoint createAMapRunPoint(String recordId, AMapLocation aMapLocation) {
        RunPoint point = new RunPoint();
        point.record_id = recordId;
        point.sdk_type = SDK_TYPE.GD.type;

        point.latitude = aMapLocation.getLatitude();
        point.longitude = aMapLocation.getLongitude();
        point.altitude = aMapLocation.getAltitude();

        point.speed = aMapLocation.getSpeed();
        point.time = aMapLocation.getTime();
        point.elapsed_time_nanos = aMapLocation.getElapsedRealtimeNanos();

        point.location_type = aMapLocation.getLocationType();
        point.coord_type = aMapLocation.getCoordType();
        point.error_code = aMapLocation.getErrorCode();

        point.accuracy = aMapLocation.getAccuracy();
        point.gps_accuracy_status = aMapLocation.getGpsAccuracyStatus();
        point.satellites = aMapLocation.getSatellites();
        point.trusted_level = aMapLocation.getTrustedLevel();

        return point;
    }


    enum SPORT_TYPE {

        OUTSIDE_RUN(0);

        int type;

        SPORT_TYPE(int type) {
            this.type = type;
        }
    }

    public static RunRecord createRunRecord(String recordId) {
        RunRecord record = new RunRecord();
        record.record_id = recordId;
        record.type = SPORT_TYPE.OUTSIDE_RUN.type;
        record.complete = false;
        return record;
    }


}
