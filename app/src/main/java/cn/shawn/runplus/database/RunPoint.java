package cn.shawn.runplus.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "run_point")
public class RunPoint {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long recordId;

    public double latitude;

    public double longitude;
}
