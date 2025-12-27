package com.example.test2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NameEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public NameEntity(String name) {
        this.name = name;
    }
}
