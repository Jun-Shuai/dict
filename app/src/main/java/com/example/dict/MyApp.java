package com.example.dict;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.example.dict.room.AppDataBase;

import java.util.Objects;

public class MyApp extends Application {

//    private MyApp() {}
//    private static class Single {
//        private static final MyApp ins = new MyApp();
//    }
    private AppDataBase dataBase;
    public AppDataBase getDataBase() {
        return dataBase;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 数据库
        dataBase = Room.databaseBuilder(getApplicationContext(),
                AppDataBase.class,
                "my-database.db")
                .build();
        Log.d("TAG","创建数据库！");
    }
}
