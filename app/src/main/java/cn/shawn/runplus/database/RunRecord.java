package cn.shawn.runplus.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "run_record")
public class RunRecord {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public int type;

    public boolean complete;

    public int durationTime;

    public RunRecord(int type, int durationTime) {
        this.type = type;
        this.durationTime = durationTime;
    }

    @Override
    public String toString() {
        return "RunRecord{" +
                "id=" + id +
                ", type=" + type +
                ", complete=" + complete +
                ", durationTime=" + durationTime +
                '}';
    }

    public static RunRecord mock() {
        int type = (int) (Math.random() * 2);
        int duration = (int) (10 + 10 * Math.random());
        return new RunRecord(type, duration);
    }

}
