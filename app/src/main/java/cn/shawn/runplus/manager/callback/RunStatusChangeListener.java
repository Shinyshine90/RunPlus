package cn.shawn.runplus.manager.callback;

import cn.shawn.runplus.manager.RunManager;

public interface RunStatusChangeListener {
    void onStatusChanged(RunManager.Status status);
}
