package com.example.expensemate.controller;

import com.example.expensemate.manager.RecordManager;
import com.example.expensemate.model.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class User {
    private RecordManager recordManager;
    /*mock data to test the app
    private List<Record> recordList = new ArrayList<>();*/
    private static User userInstance;

    public User() {
        this.recordManager = new RecordManager();
        /*mock data to test the app
        Record record1 = new Record();
        record1.setName("Groceries");
        record1.setType("Expense");
        record1.setPrice(50.0f);
        Record record2 = new Record();
        record2.setName("Salary");
        record2.setType("Income");
        record2.setPrice(2000.0f);
        recordList.add(record1);
        recordList.add(record2);*/
    }

    public static User getInstance() {
        if (userInstance == null) {
            userInstance = new User();
        }
        return userInstance;
    }

    public void createRecord() {
        this.recordManager.createRecord();
    }

    public void selectRecord(String recordId) {
        this.recordManager.selectRecord(recordId);
    }

    public void selectType(String type) {
        this.recordManager.selectType(type);
    }

    public void enterName(String name) {
        this.recordManager.enterName(name);
    }

    public void enterPrice(float price) {
        this.recordManager.enterPrice(price);
    }

    public void selectDate(Date date) {
        this.recordManager.selectDate(date);
    }

    public void saveRecord() {
        this.recordManager.saveRecord();
        /*mock data to test the app
        recordList = this.recordManager.getRecordList();*/
    }

    public void editRecord() {
        this.recordManager.editRecord();
    }

    public Map<String, Record> getRecordList() {
        /*mock data to test the app
        return recordList;*/
        return this.recordManager.getRecordList();
    }
}
