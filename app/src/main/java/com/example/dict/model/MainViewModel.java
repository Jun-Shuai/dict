package com.example.dict.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dict.room.entity.Word;

import java.util.List;

public class MainViewModel extends ViewModel {
    MutableLiveData<List<Word>> wordListModel = new MutableLiveData<>();

    public MutableLiveData<List<Word>> getWordListModel() {
        return wordListModel;
    }

    public void setWordListModel(MutableLiveData<List<Word>> wordListModel) {
        this.wordListModel = wordListModel;
    }
}
