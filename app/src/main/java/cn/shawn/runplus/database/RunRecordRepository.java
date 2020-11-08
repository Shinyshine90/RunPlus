package cn.shawn.runplus.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Room;

import cn.shawn.base.repository.IBaseRepository;

public class RunRecordRepository implements IBaseRepository {

    private static final String DB_FILE_NAME = "run_record.db";

    private RunRecordDatabase mRunRecordDatabase;

    @Override
    public void init(@NonNull Context context) {
        mRunRecordDatabase = Room.databaseBuilder(context,
                RunRecordDatabase.class, DB_FILE_NAME).build();
    }

    public RunRecordDao runRecordDao() {
        return mRunRecordDatabase.runRecordDao();
    }

    public RunPointDao runPointDao() {
        return mRunRecordDatabase.runPointDao();
    }
}
