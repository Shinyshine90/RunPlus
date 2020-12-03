package cn.shawn.runplus.page.mocklocation;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import cn.shawn.runplus.R;

public class MockLocationActivity extends AppCompatActivity {

    public static final String TAG_FRAGMENT = "MapFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_location);
        initMap();
    }

    private void initMap() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_map, new LocationMapFragment(), TAG_FRAGMENT)
                .commitAllowingStateLoss();
    }


}