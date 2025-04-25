package com.example.expensemate.controller;

import com.example.expensemate.manager.RecordManager;
import com.example.expensemate.manager.TagManager;
import com.example.expensemate.model.Record;
import com.example.expensemate.model.Tag;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class User {
    private RecordManager recordManager;
    private TagManager tagManager;
    private static User userInstance;

    public User() {
        this.recordManager = new RecordManager();
        this.tagManager = new TagManager();
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

    public String getRecordType() {
        return this.recordManager.getRecordType();
    }

    public void enterName(String name) {
        this.recordManager.enterName(name);
    }
    public String getRecordName() {
        return this.recordManager.getRecordName();
    }

    public void enterPrice(float price) {
        this.recordManager.enterPrice(price);
    }

    public float getRecordPrice() {
        return this.recordManager.getRecordPrice();
    }

    public void selectDate(Date date) {
        this.recordManager.selectDate(date);
    }

    public Date getRecordDate() {
        return this.recordManager.getRecordDate();
    }

    public void selectTag(String tagName) {
        Tag tag = this.tagManager.selectTag(tagName);
        this.recordManager.setTag(tag);
    }

    public void deleteSelectedTag(String tagName) {
        Tag tag = this.tagManager.selectTag(tagName);
        this.recordManager.deleteTag(tag);
    }

    public void saveRecord() {
        this.recordManager.saveRecord();
    }

    public void editRecord() {
        this.recordManager.editRecord();
    }

    public Map<String, Record> getRecordList() {
        return this.recordManager.getRecordList();
    }

    public List<String> getAvaliableTags() {
        return this.tagManager.getTagList();
    }

    public List<String> getSelectedTags() {
        return this.recordManager.getSelectedTags();
    }
}
