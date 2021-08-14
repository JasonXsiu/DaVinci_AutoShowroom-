package com.example.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface CarDao {



    @Query("select * from cars" )
    LiveData<List<Car_entity>> getAllCar_entity();

//    @Query("select * from cars where carMaker=:model")
//    List<Car_entity> getCar_Model(String model);

    @Insert
    void addCar_entity(Car_entity car);

    @Query("delete from cars where carId = (select MAX(carId) from cars )")
    void deleteLastCar();

    @Query("delete FROM cars")
    void deleteAllCar_entities();

}
