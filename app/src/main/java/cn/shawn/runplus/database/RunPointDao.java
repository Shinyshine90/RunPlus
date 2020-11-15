package cn.shawn.runplus.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;


@Dao
public interface RunPointDao {

    @Query("select * from run_point")
    Single<List<RunPoint>> loadAll();

    @Query("select * from run_point where record_id = :recordId")
    Single<List<RunPoint>> loadAllByRecordId(String recordId);

    @Insert
    Completable insert(RunPoint runPoint);

    @Insert
    Completable insertAll(RunPoint...point);



}
