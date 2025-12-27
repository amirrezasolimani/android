package com.example.p5;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {WordEntity.class, UsedWordEntity.class}, version = 1, exportSchema = false)
public abstract class AppDb extends RoomDatabase {

    public abstract WordDao wordDao();

    private static volatile AppDb INSTANCE;

    public static AppDb getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDb.class,
                                    "guess_game.db"
                            )
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        AppDb database = getInstance(context);
                                        seed(database.wordDao());
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void seed(WordDao dao) {
        List<WordEntity> words = Arrays.asList(
                new WordEntity(201, "بستنی", "geography", false),
                new WordEntity(202, "چوب", "geography", false),
                new WordEntity(203, "لیوان", "geography", false),
                new WordEntity(204, "فوتبال", "geography", false),
                new WordEntity(205, "عشق", "geography", false),
                new WordEntity(206, "خانواده", "geography", false),
                new WordEntity(207, "گوشی", "geography", false),
                new WordEntity(208, "محله", "geography", false),
                new WordEntity(209, "کوروش", "geography", false),
                new WordEntity(210, "قرص", "geography", false),
                new WordEntity(211, "مبل", "geography", false),
                new WordEntity(212, "مو", "geography", false),
                new WordEntity(213, "کار", "geography", false),
                new WordEntity(214, "یخ", "geography", false),
                new WordEntity(215, "برق", "geography", false),
                new WordEntity(216, "جاوا", "geography", false),
                new WordEntity(217, "ماشین", "geography", false),
                new WordEntity(218, "ایتالیا", "geography", false),
                new WordEntity(219, "اراک", "geography", false),
                new WordEntity(220, "نیویورک", "geography", false)
        );

        dao.insertWords(words);
    }
}
