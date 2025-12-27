package com.example.test2;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NameEntity.class}, version = 1, exportSchema = false)
public abstract class Appdb extends RoomDatabase {

    private static Appdb INSTANCE;

    public abstract NameDao nameDao();

    // متد Singleton
    public static synchronized Appdb getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            Appdb.class,
                            "app_db"
                    )
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
