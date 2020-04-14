package com.example.dict.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.dict.room.dao.WordDao;
import com.example.dict.room.entity.Word;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract WordDao wordDao();
}