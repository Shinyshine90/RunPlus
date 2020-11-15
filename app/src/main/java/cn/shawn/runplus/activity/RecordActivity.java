package cn.shawn.runplus.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.amap.api.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;
import cn.shawn.base.repository.RepositoryCenter;
import cn.shawn.runplus.R;
import cn.shawn.runplus.database.RunPoint;
import cn.shawn.runplus.database.RunRecordRepository;
import cn.shawn.runplus.fragment.TraceMapFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RecordActivity extends AppCompatActivity {

    private static final String EXTRA_RECORD_ID = "EXTRA_RECORD_ID";

    public static void start(Context context, String recordId) {
        Intent intent = new Intent(context, RecordActivity.class);
        intent.putExtra(EXTRA_RECORD_ID, recordId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        showRecordFragment();
    }

    @SuppressLint("CheckResult")
    private void showRecordFragment() {
        String recordId = getIntent().getStringExtra(EXTRA_RECORD_ID);
        RepositoryCenter.getInstance().getRepository(RunRecordRepository.class)
                .runPointDao()
                .loadAllByRecordId(recordId)
                .map(runPoints -> {
                    List<LatLng> latLngList = new ArrayList<>(runPoints.size());
                    for (int i = 0; i < runPoints.size(); i++) {
                        RunPoint point = runPoints.get(i);
                        latLngList.add(new LatLng(point.latitude, point.longitude));
                    }
                    return latLngList;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showRecord);

    }

    private void showRecord(List<LatLng> runPoints) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.cl_container, TraceMapFragment.create(runPoints))
                .commitAllowingStateLoss();

    }
}