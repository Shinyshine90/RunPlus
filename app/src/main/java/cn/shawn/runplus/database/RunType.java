package cn.shawn.runplus.database;

public enum RunType {

    OUTDOOR_RUN(0),
    INDOOR_RUN(1);

    int type;

    RunType(int key) {
        this.type = key;
    }
}
