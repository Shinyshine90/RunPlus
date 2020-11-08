package cn.shawn.runplus.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import cn.shawn.base.repository.RepositoryCenter;
import cn.shawn.map.practise.TestLocationActivity;
import cn.shawn.map.practise.TestMapHomeActivity;
import cn.shawn.runplus.R;
import cn.shawn.runplus.database.RunRecord;
import cn.shawn.runplus.database.RunRecordRepository;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupClickListener();
        testDatabase();
    }

    private void testDatabase() {
        RunRecordRepository repository = RepositoryCenter.getInstance().getRepository(RunRecordRepository.class);
        for (int i = 0; i < 1000; i++) {

        }
        repository.runRecordDao().insert(RunRecord.mock()).subscribeOn(Schedulers.io()).subscribe();
        repository.runRecordDao().insert(RunRecord.mock()).subscribeOn(Schedulers.io()).subscribe();
        repository.runRecordDao().insert(RunRecord.mock()).subscribeOn(Schedulers.io()).subscribe();
        repository.runRecordDao().insert(RunRecord.mock()).subscribeOn(Schedulers.io()).subscribe();
        repository.runRecordDao().loadAll()
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<RunRecord>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<RunRecord> runRecords) {
                        for (RunRecord record : runRecords) {
                            Log.i("MainActivity", "onSuccess: "+record);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    private void setupClickListener() {
        ViewGroup group = findViewById(R.id.ll);
        for (int i = 0; i < group.getChildCount(); i++) {
            group.getChildAt(i).setOnClickListener(mListeners[i]);
        }
    }

    private View.OnClickListener[] mListeners = {
      v -> startActivity(new Intent(this, TestMapHomeActivity.class)),
      v -> startActivity(new Intent(this, TestLocationActivity.class)),
      v -> startActivity(new Intent(this, RunActivity.class))
    };


}