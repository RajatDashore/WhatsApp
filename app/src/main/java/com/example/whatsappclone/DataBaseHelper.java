package com.example.whatsappclone;


import android.annotation.SuppressLint;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@SuppressLint("RestrictedApi")
@Entity(tableName = "blockedcontacts")
public class DataBaseHelper implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int id = 0;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "image")
    String Image;
    @ColumnInfo(name = "email")
    String Email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

}
