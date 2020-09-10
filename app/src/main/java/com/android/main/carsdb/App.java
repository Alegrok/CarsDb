package com.android.main.carsdb;

import android.app.Application;

import androidx.room.Room;

import com.android.main.carsdb.Data.CarsAppDatabase;
import com.android.main.carsdb.Utils.Util;

public class App extends Application {

    private static CarsAppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        appDatabase = Room
                .databaseBuilder(this, CarsAppDatabase.class, Util.DATABASE_NAME)
                .build();
    }

    public static CarsAppDatabase getAppDatabase() {
        return appDatabase;
    }
}
