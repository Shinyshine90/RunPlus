package cn.shawn.runplus.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;


@Dao
public interface RunRecordDao {

    @Query("select * from run_record")
    Single<List<RunRecord>> loadAll();

    @Insert
    Completable insert(RunRecord record);

    @Insert
    Completable insertAll(RunRecord...records);

    @Query("select * from run_record where complete = 0 ")
    Single<List<RunRecord>> loadNotCompleteRecord();

    @Query("select * from run_record where record_id = :recordId")
    Single<List<RunRecord>> getRecord(String recordId);

    @Update
    Completable updateRecord(RunRecord record);
}
