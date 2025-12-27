package com.example.p6;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_emails")
public class UserEmailEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String email;

    public UserEmailEntity(String email) {
        this.email = email;
    }
}
