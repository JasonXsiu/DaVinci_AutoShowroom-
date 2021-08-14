package com.example.cars_app;

public class Car {
    private String Model ;
    private String Maker;
    private String Year;
    private String Color;
    private String Seat;
    private String Price;

    public Car(String maker, String model, String year, String color, String seat, String price ){
        this.Maker = maker;
        this.Model=  model;
        this.Year= year;
        this.Color= color;
        this.Seat= seat;
        this.Price=price ;
    }

    public String getModel() {
        return Model;
    }

    public String getMaker() {
        return Maker;
    }

    public String getYear() {
        return Year;
    }

    public String getColor() {
        return Color;
    }

    public String getSeat() {
        return Seat;
    }

    public String getPrice() {
        return Price;
    }
}
