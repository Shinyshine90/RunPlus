package cn.shawn.runplus.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.shawn.map.practise.TestLocationActivity;
import cn.shawn.map.practise.TestMapHomeActivity;
import cn.shawn.mock.LocationMockManager;
import cn.shawn.runplus.R;
public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupClickListener();

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
      v -> startActivity(new Intent(this, RunActivity.class)),
      v -> startActivity(new Intent(this, RecordsActivity.class)),
      v -> {
          LocationMockManager.getInstance().init(this);
          LocationMockManager.getInstance().startMockRoute(this);
          Toast.makeText(this,"Mock启动", Toast.LENGTH_SHORT).show();
      }
    };

}