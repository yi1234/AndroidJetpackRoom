package com.hhe.androidjetpackroom.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hhe.androidjetpackroom.BaseApp;
import com.hhe.androidjetpackroom.DataRepository;
import com.hhe.androidjetpackroom.db.entity.User;

import java.util.List;

public class UserModel extends AndroidViewModel {
    private final DataRepository mRepository;
    public UserModel(@NonNull Application application) {
        super(application);
        mRepository = ((BaseApp) application).getRepository();
    }


    public LiveData<List<User>> getUsers() {
        return mRepository.getUsers();
    }

    public LiveData<List<User>> getUserIds(int[] ids) {
        return mRepository.getUserIds(ids);
    }

    public LiveData<User> getUser(String frist, String end) {
        return mRepository.getUser(frist,end);
    }

    public void delete(User user){
        mRepository.delete(user);
    }

    public void insertUsers(User users){
        mRepository.insertUsers(users);
    }

}
