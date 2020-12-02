package cn.shawn.runplus.page.records;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.annotation.SuppressLint;
import android.os.Bundle;
import cn.shawn.runplus.R;
import cn.shawn.runplus.activity.RecordActivity;
import cn.shawn.runplus.database.RunRecord;
import cn.shawn.runplus.databinding.ActivityRecordsBinding;

@SuppressLint("CheckResult")
public class RecordsActivity extends AppCompatActivity {

    private ActivityRecordsBinding mDataBinding;

    private RecordsViewModel mRecordModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_records);
        mRecordModel = new ViewModelProvider(this).get(RecordsViewModel.class);
        mDataBinding.setVm(mRecordModel);
        mDataBinding.setLifecycleOwner(this);
        mRecordModel.loadRecords();
        mRecordModel.recordsData.observe(this, records -> {
            RecordAdapter adapter = new RecordAdapter(this::onItemClick, records);
            mDataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            mDataBinding.recyclerView.setAdapter(adapter);
        });
    }

    private void onItemClick(RunRecord record) {
        RecordActivity.start(this, record.record_id);
    }

}