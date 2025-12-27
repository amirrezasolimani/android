package com.example.p5;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDao {

    @Query(
            "SELECT * FROM words " +
                    "WHERE category = :category " +
                    "AND blocked = 0 " +
                    "AND id NOT IN (" +
                    "   SELECT wordId FROM used_words WHERE sessionId = :sessionId" +
                    ") " +
                    "ORDER BY RANDOM() " +
                    "LIMIT 1"
    )
    WordEntity getRandomWord(String category, long sessionId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void markUsed(UsedWordEntity used);

    @Query("DELETE FROM used_words WHERE sessionId = :sessionId")
    void clearUsedForSession(long sessionId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertWords(List<WordEntity> words);
}
