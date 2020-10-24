package cn.shawn.runplus.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cn.shawn.aop.permission.RequestPermission;
import cn.shawn.permission.PermissionActivity;
import cn.shawn.runplus.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, PermissionActivity.class));
        Log.i("MainActivity", "onCreate: "+onTest());
        checkPermission();
    }

    @RequestPermission
    private void checkPermission() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public String onTest() {
        return "HelloWorld";
    }
}