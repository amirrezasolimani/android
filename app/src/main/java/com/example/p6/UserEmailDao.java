package com.example.p6;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserEmailDao {

    @Insert
    void insert(UserEmailEntity email);

    @Query("SELECT * FROM user_emails")
    List<UserEmailEntity> getAll();
}
