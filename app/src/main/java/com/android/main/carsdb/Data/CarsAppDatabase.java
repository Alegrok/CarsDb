package com.android.main.carsdb.Data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.android.main.carsdb.Model.Car;

@Database(entities = {Car.class}, version = 1)
public abstract class CarsAppDatabase extends RoomDatabase {

    public abstract CarDAO getCarDAO();
}
