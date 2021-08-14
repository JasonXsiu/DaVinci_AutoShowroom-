package com.example.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CarRepository {
    //a reference to the Dao interface which will be used to execute the database operations we have defined earlier.
    private final CarDao mCarDao;
    //defines an array list that is used to hold a copy of your data.
    private final LiveData<List<Car_entity>> mAllCars;

    /*
        this contractor creates a reference to the database that will be used to access the Dao interface.
    */
    CarRepository(Application application) {
        CarDatabase db = CarDatabase.getDatabase(application);
        mCarDao = db.CarDao();
//        The Dao interface will be used later to get the list of cars.
        mAllCars = mCarDao.getAllCar_entity();
    }

    LiveData<List<Car_entity>> getAllCars() {
        return mAllCars;
    }

    void insert(Car_entity car) {
        CarDatabase.databaseWriteExecutor.execute(() -> mCarDao.addCar_entity(car));
    }

    void deleteAll() {
        CarDatabase.databaseWriteExecutor.execute(() -> {
            mCarDao.deleteAllCar_entities();
        });
    }

    void deleteLastCar() {
        CarDatabase.databaseWriteExecutor.execute(() -> {
            mCarDao.deleteLastCar();
        });
    }
}

