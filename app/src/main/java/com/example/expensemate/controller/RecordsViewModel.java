package com.example.expensemate.controller;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensemate.model.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordsViewModel extends ViewModel {
    private final MutableLiveData<Map<String, Record>> recordList = new MutableLiveData<>(new HashMap<>());
    private User user = User.getInstance();

    public LiveData<Map<String, Record>> getRecordList() {
        return recordList;
    }

    public void loadRecords() {
        recordList.setValue(user.getRecordList());
    }
}
