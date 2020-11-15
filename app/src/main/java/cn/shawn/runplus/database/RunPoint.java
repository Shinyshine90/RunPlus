package cn.shawn.runplus.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "run_point")
public class RunPoint {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String record_id;

    public int sdk_type;

    public double latitude;

    public double longitude;

    public double altitude;

    public float speed;

    public long time;

    public long elapsed_time_nanos;

    public int location_type;

    public String coord_type;

    public int error_code;

    public float accuracy;

    public int gps_accuracy_status;

    public int satellites;

    public int trusted_level;

}
