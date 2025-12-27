package com.example.test2;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NameDao {

    @Insert
    void insert(NameEntity name);

    @Query("SELECT * FROM NameEntity")
    List<NameEntity> getAllNames();
}
