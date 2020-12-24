package com.hhe.androidjetpackroom.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.hhe.androidjetpackroom.db.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    LiveData<List<User>> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    LiveData<List<User>> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE first_name LIKE :frist And "
            + "last_name LIKE :last")
    LiveData<User> findByName(String frist ,String last);

    @Insert
    void insertAll(User... users);

//    @Insert
//    void insertAll(List<User> users);

    @Delete
    void delete(User user);

}
