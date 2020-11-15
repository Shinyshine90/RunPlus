package cn.shawn.runplus.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "run_record")
public class RunRecord {

    @PrimaryKey
    @NonNull
    public String record_id;

    public int type;

    public boolean complete;

    public long start_stamp;

    public long end_stamp;


}
