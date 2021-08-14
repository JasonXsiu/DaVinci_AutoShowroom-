package com.example.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CarViewModel extends AndroidViewModel {

    private  CarRepository mRepository;
    private  LiveData<List<Car_entity>> mAllCars;

    public CarViewModel(@NonNull Application application) {
        super(application);
        mRepository = new CarRepository(application);
        mAllCars = mRepository.getAllCars();
    }

    public LiveData<List<Car_entity>> getAllCars() {
        return mAllCars;
    }

    public void insert(Car_entity car_entity) {
        mRepository.insert(car_entity);
    }
    public void deleteAll(){ mRepository.deleteAll(); }

    public void deleteLastCar(){
        mRepository.deleteLastCar();
    }
}
