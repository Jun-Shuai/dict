package com.example.dict.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dict.room.entity.Word;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface WordDao {

    @Query("SELECT * FROM Word where uid < 100")
    Single<List<Word>> getAll();

    @Query("SELECT * FROM Word WHERE uid IN (:userIds)")
    List<Word> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM word WHERE uid LIKE :first AND "
            + "uid LIKE :last LIMIT 1")
    Word findByName(String first, String last);

    @Insert
    Completable insertAll(Word... words);

    @Delete
    void delete(Word user);

}
