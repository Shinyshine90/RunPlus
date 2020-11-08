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

    @Insert
    Completable insert(RunPoint runPoint);

    @Insert
    Completable insertAll(RunPoint...point);

}
