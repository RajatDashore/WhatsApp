package com.example.whatsappclone;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = DataBaseHelper.class, version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase {

    private static final String DATABASE_NAME = "contacts";
    private static DataBase database;

    public synchronized static DataBase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), DataBase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return database;
    }

    public abstract DataBaseDAO UserDao();
}
