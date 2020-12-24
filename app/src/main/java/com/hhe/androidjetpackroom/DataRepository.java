package com.hhe.androidjetpackroom;

import androidx.lifecycle.LiveData;

import com.hhe.androidjetpackroom.db.AppDatabase;
import com.hhe.androidjetpackroom.db.entity.User;

import java.util.List;

public class DataRepository {
    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    private LiveData<List<User>> users;
    private LiveData<List<User>> userIds;
    private LiveData<User> user;

    public DataRepository(AppDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<User>>  getUsers() {
        users = mDatabase.userDao().getAll();
        return users;
    }

    public LiveData<List<User>> getUserIds(int[] ids) {
        userIds = mDatabase.userDao().loadAllByIds(ids);
        return userIds;
    }

    public LiveData<User> getUser(String frist, String end) {
        user = mDatabase.userDao().findByName(frist, end);
        return user;
    }

    public void delete(User user){
        mDatabase.userDao().delete(user);
    }

    public void insertUsers(User users){
        mDatabase.userDao().insertAll(users);
    }


}
