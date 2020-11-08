package cn.shawn.base.repository;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class RepositoryCenter {

    private RepositoryCenter() {}

    private static class Holder {
        static RepositoryCenter sInstance = new RepositoryCenter();
    }

    public static RepositoryCenter getInstance() {
        return Holder.sInstance;
    }

    private Map<Class<? extends IBaseRepository>, IBaseRepository> mRepositories = new HashMap<>();

    @MainThread
    public <T extends IBaseRepository> T getRepository(@NonNull Class<T> clz) {
        IBaseRepository repository = mRepositories.get(clz);
        if (repository == null) {
            try {
                repository = clz.newInstance();
                mRepositories.put(clz, repository);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (T) repository;
    }
}
