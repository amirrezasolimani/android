package com.example.p5;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "words")
public class WordEntity {

    @PrimaryKey
    public int id;          // ID یکتا

    public String text;     // کلمه
    public String category; // مثلا "geography"

    public boolean blocked; // مسدود دائمی (true/false)

    public WordEntity(int id, String text, String category, boolean blocked) {
        this.id = id;
        this.text = text;
        this.category = category;
        this.blocked = blocked;


    }
}

