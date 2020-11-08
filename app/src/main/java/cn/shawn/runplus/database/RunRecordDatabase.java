package cn.shawn.runplus.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {RunPoint.class, RunRecord.class}, version = 1, exportSchema = true)
public abstract class RunRecordDatabase extends RoomDatabase {

    public abstract RunPointDao runPointDao();

    public abstract RunRecordDao runRecordDao();

}
