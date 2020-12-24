package com.hhe.androidjetpackroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hhe.androidjetpackroom.db.AppDatabase;
import com.hhe.androidjetpackroom.db.AppExecutors;
import com.hhe.androidjetpackroom.db.entity.User;
import com.hhe.androidjetpackroom.viewmodel.UserModel;

import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userModel = new ViewModelProvider(this,  ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(UserModel.class);
    }
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    public void insertAll(View view) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                User user = new User();
                user.firstName = "钟";
                user.lastName = "文";
                userModel.insertUsers(user);
                emitter.onNext("");
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Toast.makeText(MainActivity.this, "插入成功", Toast.LENGTH_SHORT).show();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("insert","throwable = " + throwable.getMessage());
            }
        });
        compositeDisposable.add(disposable);
    }

    public void findByName(View view) {
        userModel.getUser("钟", "文").observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    Log.d("findByName", user.toString());
                }
            }
        });
    }

    public void getAll(View view) {
        userModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user : users) {
                    Log.d("getAll", user.toString());
                }
            }
        });


    }

    public void loadAllByIds(View view) {
        int[] ids = {0};
        userModel.getUserIds(ids).observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user : users) {
                    Log.d("loadAllByIds", user.toString());
                }
            }
        });
    }

    public void delete(View view) {

        Disposable disposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                User user = new User();
                user.firstName = "钟";
                user.lastName = "文";
                userModel.delete(user);
                emitter.onNext("");
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("delete","throwable = " + throwable.getMessage());
            }
        });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}