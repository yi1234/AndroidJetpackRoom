package com.hhe.androidjetpackroom;

import android.app.Application;

import com.hhe.androidjetpackroom.db.AppDatabase;
import com.hhe.androidjetpackroom.db.AppExecutors;

public class BaseApp extends Application {
    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}
