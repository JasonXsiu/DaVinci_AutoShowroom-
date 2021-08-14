package com.example.cars_app;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.provider.CarViewModel;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class CardActivity extends AppCompatActivity {
    ArrayList<String> itemList;
    ArrayList<Car> data;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CarRecyclerAdapter adapter;
    String maker, model, price, year, seat,color;
    private CarViewModel mCarViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        recyclerView = findViewById(R.id.my_recycler_view);
        data = new ArrayList<Car>();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //receiving data from the MainActivity
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        itemList = bundle.getStringArrayList("itemList");
//        createCars();

        adapter = new CarRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        setUpDatabase();
    }

    private void createCars(){
        String maker, model, price, year, seat,color;
        StringTokenizer stringTokenizer;
        for (String item : itemList){
            stringTokenizer = new StringTokenizer(item);
            model = stringTokenizer.nextToken(",");
            year = stringTokenizer.nextToken(",");
            color = stringTokenizer.nextToken(",");
            seat = stringTokenizer.nextToken(",");
            price = stringTokenizer.nextToken(",");
            maker = stringTokenizer.nextToken(",");;
            Car newItem = new Car ( maker,  model,  year,  color,  seat,  price );
            data.add(newItem);
        }
    }

    private void setUpDatabase(){
        mCarViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        //fetch the data from the database and pass it to the adapter.
        mCarViewModel.getAllCars().observe(this, newData -> {
            adapter.setmCars(newData);
            adapter.notifyDataSetChanged();
        });
    }

}






