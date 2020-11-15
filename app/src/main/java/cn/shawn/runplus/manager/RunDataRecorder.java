package cn.shawn.runplus.manager;

import android.content.Context;
import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import cn.shawn.base.repository.RepositoryCenter;
import cn.shawn.runplus.database.RunModelFactory;
import cn.shawn.runplus.database.RunRecord;
import cn.shawn.runplus.database.RunRecordRepository;
import cn.shawn.runplus.manager.callback.LocationCallback;
import cn.shawn.runplus.manager.callback.LocationProvider;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RunDataRecorder implements LocationCallback {

    private RunRecord mRunRecord;

    private Context mContext;

    private volatile boolean mHeadConfirmed;

    private volatile boolean mHasCompleted;

    private PublishSubject<AMapLocation> mLocationPublisher;

    private LocationProvider mProvider;

    public RunDataRecorder(Context context, RunRecord runRecord, LocationProvider provider) {
        mContext = context;
        mRunRecord = runRecord;
        mProvider = provider;
        provider.registerLocationCallback(this);

        mLocationPublisher = PublishSubject.create();
        mLocationPublisher.subscribeOn(Schedulers.io()).subscribe(mLocationObserver);
    }

    public void start() {
        mRunRecord.start_stamp = System.currentTimeMillis();
        RunRecordRepository repository = RepositoryCenter.getInstance().getRepository(RunRecordRepository.class);
        repository.runRecordDao().updateRecord(mRunRecord)
            .subscribeOn(Schedulers.io()).subscribe();
    }

    public void complete() {
        mRunRecord.end_stamp = System.currentTimeMillis();
        RunRecordRepository repository = RepositoryCenter.getInstance().getRepository(RunRecordRepository.class);
        repository.runRecordDao().updateRecord(mRunRecord)
                .subscribeOn(Schedulers.io()).subscribe();

        mProvider.unregisterLocationCallback(this);
        mHasCompleted = true;
    }

    @Override
    public void onLocation(AMapLocation location) {
        if (mHasCompleted) return;
        if (!mHeadConfirmed) {
            mLocationPublisher.onNext(location);
        } else {
            saveLocation(location);
        }
    }

    private void saveLocation(AMapLocation location) {
        RepositoryCenter.getInstance().getRepository(RunRecordRepository.class)
                .runPointDao()
                .insert(RunModelFactory.createAMapRunPoint(mRunRecord.record_id, location))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private Observer<AMapLocation> mLocationObserver = new Observer<AMapLocation>() {

        private AMapLocation mLastLatLng;

        @Override
        public void onSubscribe(Disposable d) { }

        @Override
        public void onNext(AMapLocation location) {
            if (mLastLatLng == null) {
                mLastLatLng = location;
                return;
            }
            if (AMapUtils.calculateLineDistance(
                    new LatLng(mLastLatLng.getLatitude(), mLastLatLng.getLongitude()),
                    new LatLng(location.getLatitude(), location.getLongitude())) < 10f) {
                mHeadConfirmed = true;
                saveLocation(location);
                return;
            }
            mLastLatLng = location;
        }

        @Override
        public void onError(Throwable e) { }

        @Override
        public void onComplete() {

        }
    };


}
