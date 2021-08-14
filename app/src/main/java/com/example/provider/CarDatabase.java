package com.example.provider;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Car_entity.class}, version = 1)
public abstract class CarDatabase extends RoomDatabase {

    public static final String CAR_DATABASE_NAME = "car_database";
    public abstract CarDao CarDao ();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile CarDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //getDatabase()  needs as input the context, a reference to the Room Database class, and a name for the database.
    static CarDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CarDatabase.class) {
                //  if there is no instance of database,
                if (INSTANCE == null) {
                    // it creates a new instance using ‘Room.databaseBuilder()’,
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CarDatabase.class, CAR_DATABASE_NAME)
                            .build();
                }
            }
        }
        // returns a reference to the current database instance if it is not null.
        return INSTANCE;

    }
}
