package cn.shawn.runplus;

import android.app.Application;
import android.content.Context;

import cn.shawn.base.repository.RepositoryCenter;
import cn.shawn.base.utils.ContextUtil;
import cn.shawn.runplus.database.RunRecordRepository;
import cn.shawn.runplus.manager.RunManager;

public class RunPlusApp extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ContextUtil.setContext(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerRepository();
        RunManager.getInstance().init(this);
    }

    private void registerRepository() {
        RepositoryCenter.getInstance().getRepository(RunRecordRepository.class).init(this);
    }
}
