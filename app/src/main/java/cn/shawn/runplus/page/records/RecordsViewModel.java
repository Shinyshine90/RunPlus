package cn.shawn.runplus.page.records;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import cn.shawn.base.repository.RepositoryCenter;
import cn.shawn.runplus.database.RunRecord;
import cn.shawn.runplus.database.RunRecordRepository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RecordsViewModel extends ViewModel {

    public static final String TAG = "RecordsViewModel";

    private CompositeDisposable mDisposables = new CompositeDisposable();

    public MutableLiveData<List<RunRecord>> recordsData = new MutableLiveData<>();;

    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    @SuppressLint("CheckResult")
    public void loadRecords() {
        Disposable d = RepositoryCenter.getInstance().getRepository(RunRecordRepository.class)
                .runRecordDao()
                .loadAll()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> isLoading.postValue(true))
                .doAfterTerminate(() -> isLoading.postValue(false))
                .subscribe(records -> recordsData.postValue(records),
                        error -> Log.e("RecordsViewModel", "loadRecords: error"));
        mDisposables.add(d);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "onCleared: " + mDisposables.isDisposed());
    }
}
