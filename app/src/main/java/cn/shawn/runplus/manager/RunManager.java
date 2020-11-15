package cn.shawn.runplus.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import com.amap.api.location.AMapLocation;
import java.util.HashSet;
import java.util.Set;

import cn.shawn.base.repository.RepositoryCenter;
import cn.shawn.base.utils.CollectionUtil;
import cn.shawn.base.utils.UuidUtil;
import cn.shawn.map.location.amap.AMapLocationManager;
import cn.shawn.runplus.database.RunModelFactory;
import cn.shawn.runplus.database.RunRecord;
import cn.shawn.runplus.database.RunRecordRepository;
import cn.shawn.runplus.manager.callback.LocationCallback;
import cn.shawn.runplus.manager.callback.LocationProvider;
import cn.shawn.runplus.manager.callback.RunStatusChangeListener;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class RunManager implements LocationProvider {

    private static final String TAG = "RunManager";

    private RunManager() {
    }

    private static class Holder {
        @SuppressLint("StaticFieldLeak")
        private static RunManager sInstance = new RunManager();
    }

    public static RunManager getInstance() {
        return Holder.sInstance;
    }

    private Status mStatus = Status.IDLE;

    private Context mContext;

    private AMapLocationManager mLocationManager;

    private RunRecordRepository mDataRepository;

    private RunDataRecorder mCurrRunDataRecorder;

    private final Set<LocationCallback> mLocationCallbacks = new HashSet<>();

    private final Set<RunStatusChangeListener> mRunStatusChangeListeners = new HashSet<>();

    public void init(@NonNull Context context) {
        mContext = context.getApplicationContext();
        mLocationManager = new AMapLocationManager();
        mLocationManager.init(context.getApplicationContext(), this::onLocation);
        mDataRepository = RepositoryCenter.getInstance().getRepository(RunRecordRepository.class);
    }

    public Single<Boolean> hasRemainedRecord() {
        return mDataRepository.runRecordDao()
                .loadNotCompleteRecord()
                .map(CollectionUtil::isNotEmpty)
                .subscribeOn(Schedulers.io());
    }

    public Completable startRun() {
        RunRecord newRecord = RunModelFactory.createRunRecord(UuidUtil.generate());
        return mDataRepository.runRecordDao().insert(newRecord)
                .subscribeOn(Schedulers.io())
                .doOnComplete(() -> {
                    onStartBrandNew(newRecord);
                });
    }

    public void pauseRun() {
        if (mStatus == Status.RUNNING) return;
        mStatus = Status.PAUSED;
        onStatusChanged();
    }

    public void resumeRun() {
        if (mStatus != Status.PAUSED) return;
        mLocationManager.start();
        mStatus = Status.RUNNING;
        onStatusChanged();
    }

    public void completeRun() {
        if (mStatus == Status.IDLE || mStatus == Status.STOP) return;
        mLocationManager.stop();
        mLocationManager.release();
        mCurrRunDataRecorder.complete();
        mStatus = Status.STOP;
        onStatusChanged();
    }

    private void onStartBrandNew(RunRecord newRecord) {
        mLocationManager.start();
        releaseRecorder();
        mCurrRunDataRecorder = new RunDataRecorder(mContext, newRecord, this);
        mCurrRunDataRecorder.start();
        mStatus = Status.RUNNING;
        onStatusChanged();
    }

    private void releaseRecorder() {
        if (mCurrRunDataRecorder == null) return;

    }

    private void onStatusChanged() {
        new Handler(Looper.getMainLooper()).post(() -> {
            synchronized (mRunStatusChangeListeners) {
                for (RunStatusChangeListener listener : mRunStatusChangeListeners) {
                    listener.onStatusChanged(mStatus);
                }
            }
        });
    }

    public void registerStatusChangeListener(RunStatusChangeListener listener) {
        synchronized (mRunStatusChangeListeners) {
            mRunStatusChangeListeners.add(listener);
        }
    }

    public void unregisterStatusChangeListener(RunStatusChangeListener listener) {
        synchronized (mRunStatusChangeListeners) {
            mRunStatusChangeListeners.remove(listener);
        }
    }

    @Override
    public void onLocation(AMapLocation latLng) {
        if (mStatus != Status.RUNNING) return;
        synchronized (mLocationCallbacks) {
            for (LocationCallback callback : mLocationCallbacks) {
                callback.onLocation(latLng);
            }
        }
    }

    @Override
    public void registerLocationCallback(LocationCallback callback) {
        synchronized (mLocationCallbacks) {
            mLocationCallbacks.add(callback);
        }
    }

    @Override
    public void unregisterLocationCallback(LocationCallback callback) {
        synchronized (mLocationCallbacks) {
            mLocationCallbacks.remove(callback);
        }
    }

    public enum Status {
        IDLE, RUNNING, PAUSED, STOP
    }
}
