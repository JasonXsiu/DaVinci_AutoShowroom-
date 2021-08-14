package com.example.provider;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cars")
public class Car_entity {
    public static final String COLUMN_ID = "carId";

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = COLUMN_ID)
    private int id;



    private String maker;

    @ColumnInfo(name = "carModel")
    private String model;
    @ColumnInfo(name = "carColor")
    private    String color ;



    @ColumnInfo(name = "carSeats")
    private    int seats ;



    @ColumnInfo(name = "carRating")
    private int rating ;


    @ColumnInfo(name = "carYear")
    private    int year ;
    @ColumnInfo(name = "carPrice")
    private  double price;

    public Car_entity(String maker, String model, int year, String color, int seats, double price ){
        this.maker = maker;
        this.model = model;
        this.year = year;
        this.color = color;
        this.seats = seats;
        this.price = price;
        setRating(5);

    }



    //getters
    public String getMaker() {
        return maker;
    }

    public String getModel() {
        return model;
    }
    public int getId() {
        return id;
    }
    public String getColor() {
        return color;
    }

    public int getSeats() {
        return seats;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
    }
    //setter
    public void setId(int id) {
        this.id = id;
    }


    public void setRating(int rating) {
        this.rating = 5;
    }
    public int getRating() {
        return rating;
    }
}
