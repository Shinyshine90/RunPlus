package cn.shawn.runplus;

import android.app.Application;
import cn.shawn.base.repository.RepositoryCenter;
import cn.shawn.base.utils.ContextUtil;
import cn.shawn.map.location.amap.AMapLocationManager;
import cn.shawn.runplus.database.RunRecordRepository;

public class RunPlusApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtil.setContext(this);
        AMapLocationManager.getInstance().init(this);
        RepositoryCenter.getInstance().getRepository(RunRecordRepository.class).init(this);
    }
}
