/*
package com.example.whatsappclone;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.whatsappclone.Fragments.ChatsFragments;

@Database(entities = DataBaseHelper.class, version = 2, exportSchema = false)
public abstract class DataBase extends RoomDatabase {

    private static final String DATABASE_NAME = "contacts";
    private static DataBase database;

    public synchronized static DataBase getInstance(ChatsFragments context) {
        if (database == null) {
            database = Room.databaseBuilder(context.requireContext(),
                            DataBase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();

        }
        return database;
    }

    public abstract DataBaseDAO UserDao();
}
*/