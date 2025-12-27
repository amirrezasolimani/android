package com.example.p6;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserEmailEntity.class}, version = 1)
public abstract class AppDb extends RoomDatabase {

    private static AppDb instance;

    public abstract UserEmailDao userEmailDao();

    public static synchronized AppDb getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDb.class,
                    "emails_db"
            ).build();
        }
        return instance;
    }
}
