package com.example.expensemate.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class User {
    private RecordManager recordManager = null;
    private TagManager tagManager = null;

    public User(RecordManager recordManager, TagManager tagManager) {
        this.recordManager = recordManager;
        this.tagManager = tagManager;
    }

    public void createRecord() {
        this.recordManager.createRecord();
    }

    public Record getRecord() {
        return this.recordManager.getRecord();
    }

    public void setRecords(List<Record> records) {
        this.recordManager.setRecords(records);
    }

    public void setTags(List<String> tags) {
        this.tagManager.setTags(tags);
    }

    public void selectRecord(String id) {
        this.recordManager.selectRecord(id);
    }

    public void deleteRecord(String id) {
        this.recordManager.deleteRecord(id);
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

    public void addTag(String tagName) {
        this.tagManager.createTag(tagName);
    }

    public void selectTag(String tagName) {
        Tag tag = this.tagManager.selectTag(tagName);
        this.recordManager.setTag(tag);
    }

    public void unselectTag(String tagName) {
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

    public List<String> getAvailableTags() {
        return this.tagManager.getTagList();
    }

    public List<String> getSelectedTags() {
        return this.recordManager.getSelectedTags();
    }

    public List<Record> getRecordsForMonth(int year, int month) {
        return this.recordManager.getRecordsForMonth(year, month);
    }

//    public List<String> getTagsForMonth(int year, int month) {
//        return this.recordManager.getTagsForMonth(year, month);
//    }

//    public Map<String, Float> getTagCombinationSums(int year, int month, List<String> selectedTags) {
//        return this.recordManager.calculateComboTagSums(year, month, selectedTags);
//    }

//    public ChartData getChartData(ChartFilter filter) {
//        return this.recordManager.generateChartData(filter);
//    }
}
