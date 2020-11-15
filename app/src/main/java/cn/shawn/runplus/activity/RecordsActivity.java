package cn.shawn.runplus.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import cn.shawn.base.repository.RepositoryCenter;
import cn.shawn.base.utils.DateUtil;
import cn.shawn.runplus.R;
import cn.shawn.runplus.database.RunRecord;
import cn.shawn.runplus.database.RunRecordRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("CheckResult")
public class RecordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        load();
    }

    private void load() {
        RepositoryCenter.getInstance().getRepository(RunRecordRepository.class)
                .runRecordDao()
                .loadAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::fill);
    }

    private void fill(List<RunRecord> records) {
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecordAdapter(records));
    }

    private void onItemClick(RunRecord record) {
        RecordActivity.start(this, record.record_id);
    }

    private class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.Holder> {

        private List<RunRecord> mRecords;

        public RecordAdapter(List<RunRecord> records) {
            this.mRecords = records;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
            return new Holder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            RunRecord record = mRecords.get(position);
            holder.tv.setText(record.record_id);
            holder.tvDate.setText(DateUtil.format(record.start_stamp));
            holder.itemView.setOnClickListener(v -> {
                onItemClick(record);
            });
        }

        @Override
        public int getItemCount() {
            return mRecords == null ? 0 : mRecords.size();
        }

        private class Holder extends RecyclerView.ViewHolder {

            public TextView tv;

            public TextView tvDate;

            public Holder(@NonNull View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv_title);
                tvDate = itemView.findViewById(R.id.tv_date);
            }
        }
    }

}