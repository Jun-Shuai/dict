package com.example.dict.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.dict.retrofit.json.DictJson;

@Entity
public class Word {
    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "word")
    private String word;

    @ColumnInfo(name = "phonogram")
    private String phonogram;

    @ColumnInfo(name = "translate")
    private String translate;

    @ColumnInfo(name = "music")
    private String music;

    @ColumnInfo(name = "word_tream")
    private String word_tream;


    public Word() {}

    public Word(DictJson.DataBean dataBean) {
        this.uid = dataBean.getId();
        this.music = dataBean.getMusic();
        this.phonogram = dataBean.getPhonogram();
        this.translate = dataBean.getTranslate();
        this.word_tream = dataBean.getWord_tream();
        this.word = dataBean.getWord();
    }



    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonogram() {
        return phonogram;
    }

    public void setPhonogram(String phonogram) {
        this.phonogram = phonogram;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getWord_tream() {
        return word_tream;
    }

    public void setWord_tream(String word_tream) {
        this.word_tream = word_tream;
    }
}
