package com.example.dict.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dict.room.entity.StudyRecord;
import java.util.List;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface StudyRecordDao {

    @Query("SELECT * FROM studyrecord group by study_date") // 查询全部数据
    Single<List<StudyRecord>> getAll();

    @Insert
    Completable insertAll(StudyRecord... records); // 添加新的学习记录

    @Delete
    void delete(StudyRecord... records);
}
