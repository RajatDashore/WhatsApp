package com.example.whatsappclone;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DataBaseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DataBaseHelper helper);

    @Query("SELECT * FROM blockedcontacts ORDER BY id DESC ")
    List<DataBaseHelper> getlist();

    @Delete
    void delete(DataBaseHelper helper);

//    @Query("UPDATE blockedcontacts SET name = :name, email = email , WHERE id")
//    void update (int id,String name,String email,String image);

}
