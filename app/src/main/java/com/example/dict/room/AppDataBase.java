package com.example.dict.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.dict.room.dao.StudyRecordDao;
import com.example.dict.room.dao.WordDao;
import com.example.dict.room.entity.StudyRecord;
import com.example.dict.room.entity.Word;

@Database(entities = {Word.class, StudyRecord.class}, version = 2, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract WordDao wordDao();
    public abstract StudyRecordDao studyRecordDao();
}