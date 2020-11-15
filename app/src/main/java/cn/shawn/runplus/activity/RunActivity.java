package cn.shawn.runplus.activity;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import cn.shawn.runplus.fragment.TraceMapFragment;
import cn.shawn.runplus.R;
import cn.shawn.runplus.manager.RunManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class RunActivity extends AppCompatActivity {

    private final String TAG_FRAGMENT_MAP = "TraceMapFragment";

    private CompositeDisposable mDisposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        RunManager.getInstance().registerLocationCallback(this::onLocation);
        RunManager.getInstance().registerStatusChangeListener(this::onStatusChange);
        initView();
        initMapFragment();
        startMock();
    }

    private void initView() {
        Button start = findViewById(R.id.btn_start);
        Button resume = findViewById(R.id.btn_resume);
        start.setOnClickListener(this::startRun);
        resume.setOnClickListener(this::resumeRun);

        findViewById(R.id.btn_pause).setOnClickListener(this::pauseRun);
        findViewById(R.id.btn_stop).setOnClickListener(this::stopRun);

        /*Disposable d = RunManager.getInstance().hasRemainedRecord()
            .observeOn(AndroidSchedulers.mainThread())
                .subscribe(hasRemained -> {
                    start.setVisibility(hasRemained ? View.GONE : View.VISIBLE);
                    resume.setVisibility(hasRemained ? View.VISIBLE : View.GONE);
                }, error -> Log.e("RunActivity", "initView: " +error.getMessage()));
        mDisposables.add(d);*/
    }

    private void startRun(View v) {
        RunManager.getInstance().startRun()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe();
        showMapFragment();
    }

    private void resumeRun(View v) {
        RunManager.getInstance().resumeRun();
        showMapFragment();
    }

    private void pauseRun(View v) {
        RunManager.getInstance().pauseRun();
    }

    private void stopRun(View v) {
        RunManager.getInstance().completeRun();
    }

    private void startMock() {

    }

    private void initMapFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_map, TraceMapFragment.create(), TAG_FRAGMENT_MAP)
                .commitNowAllowingStateLoss();
    }

    private void showMapFragment() {
        findViewById(R.id.fl_map).setVisibility(View.VISIBLE);
    }

    private void dismissMapFragment() {
        findViewById(R.id.fl_map).setVisibility(View.GONE);
    }

    private TraceMapFragment getTraceFragment(String fragmentTag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (fragment instanceof TraceMapFragment) {
            return (TraceMapFragment) fragment;
        }
        return null;
    }

    private void onLocation(AMapLocation location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        TraceMapFragment mapFragment = getTraceFragment(TAG_FRAGMENT_MAP);
        if (mapFragment != null) {
            mapFragment.onLocation(latLng);
        }
    }

    private void onStatusChange(RunManager.Status status) {
        switch (status) {
            case RUNNING:
                findViewById(R.id.btn_start).setVisibility(View.GONE);
                findViewById(R.id.btn_resume).setVisibility(View.GONE);
                findViewById(R.id.btn_pause).setVisibility(View.VISIBLE);
                findViewById(R.id.btn_stop).setVisibility(View.VISIBLE);
                break;
            case PAUSED:
                findViewById(R.id.btn_start).setVisibility(View.GONE);
                findViewById(R.id.btn_resume).setVisibility(View.VISIBLE);
                findViewById(R.id.btn_pause).setVisibility(View.GONE);
                findViewById(R.id.btn_stop).setVisibility(View.VISIBLE);
                 break;
            case STOP:

                break;
        }
    }

    @Override
    protected void onDestroy() {
        RunManager.getInstance().unregisterLocationCallback(this::onLocation);
        RunManager.getInstance().unregisterStatusChangeListener(this::onStatusChange);
        super.onDestroy();
    }
}