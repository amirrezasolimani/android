package com.example.p5;

import androidx.room.Entity;

@Entity(
        tableName = "used_words",
        primaryKeys = {"sessionId", "wordId"}
)
public class UsedWordEntity {
    public long sessionId;
    public int wordId;

    public UsedWordEntity(long sessionId, int wordId) {
        this.sessionId = sessionId;
        this.wordId = wordId;
    }
}
