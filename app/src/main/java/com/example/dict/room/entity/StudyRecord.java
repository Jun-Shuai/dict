package com.example.dict.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class StudyRecord {

    @PrimaryKey(autoGenerate = true)
    private int sid; // id

    @ColumnInfo(name = "word_id")
    private String word_id; // word表中的id

    @ColumnInfo(name = "study_date")
    private long study_date; // 学习日期，记录次数，计算学习算法

    public StudyRecord() {}

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getWord_id() {
        return word_id;
    }

    public void setWord_id(String word_id) {
        this.word_id = word_id;
    }

    public long getStudy_date() {
        return study_date;
    }

    public void setStudy_date(long study_date) {
        this.study_date = study_date;
    }
}
