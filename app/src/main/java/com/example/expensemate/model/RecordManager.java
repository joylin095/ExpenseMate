package com.example.expensemate.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecordManager {
    private Map<String, Record> recordList;
    private Record currentRecord;

    public RecordManager(){
        this.recordList = new HashMap<>();
    }

    public void createRecord(){
        this.currentRecord = new Record();
    }

    public Record getRecord(){
        return this.currentRecord;
    }

    public void setRecords(List<Record> records){
        for (Record record : records) {
            this.recordList.put(record.getId(), record);
        }
    }

    public void selectRecord(String recordId){
        Record originalRecord = this.recordList.get(recordId);
        this.currentRecord = new Record(originalRecord);
    }

    public void deleteRecord(String recordId){
        this.recordList.remove(recordId);
    }

    public void selectType(String type){
        this.currentRecord.setType(type);
    }

    public String getRecordType(){
        return this.currentRecord.getType();
    }

    public void enterName(String name){
        this.currentRecord.setName(name);
    }

    public String getRecordName(){
        return this.currentRecord.getName();
    }

    public void enterPrice(float price){
        this.currentRecord.setPrice(price);
    }

    public float getRecordPrice(){
        return this.currentRecord.getPrice();
    }

    public void selectDate(Date date){
        this.currentRecord.setDate(date);
    }

    public Date getRecordDate(){
        return this.currentRecord.getDate();
    }

    public void setTag(Tag tag){
        this.currentRecord.setTag(tag);
    }

    public void deleteTag(Tag tag){
        this.currentRecord.deleteTag(tag);
    }

    public void saveRecord(){
        if (this.currentRecord.isValid()){
            this.recordList.put(this.currentRecord.getId(), this.currentRecord);
        }
        else{
            throw new IllegalArgumentException("Invalid record");
        }
    }

    public void editRecord() {
        if (this.currentRecord.isValid()){
            this.recordList.put(this.currentRecord.getId(), this.currentRecord);
        }
        else{
            throw new IllegalArgumentException("Invalid record");
        }
    }

    public Map<String, Record> getRecordList() {
        return recordList;
    }

    public List<String> getSelectedTags() {
        List<String> selectedTags = new ArrayList<>();
        for (Tag tag : this.currentRecord.getTags()) {
            selectedTags.add(tag.getName());
        }
        return selectedTags;
    }

    public List<Record> getRecordsForMonth(int year, int month) {
        List<Record> allRecords = new ArrayList<>(this.recordList.values());
        List<Record> filteredRecords = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (Record record : allRecords) {
            calendar.setTime(record.getDate());
            int recordYear = calendar.get(Calendar.YEAR);
            int recordMonth = calendar.get(Calendar.MONTH);

            if (recordYear == year && recordMonth == month) {
                filteredRecords.add(record);
            }
        }

        return filteredRecords;
    }

    public List<String> getTagsForMonth(int year, int month) {
        List<Record> recordsForMonth = getRecordsForMonth(year, month);
        Set<String> uniqueTags = new HashSet<>();

        for (Record record : recordsForMonth) {
            for (Tag tag : record.getTags()) {
                uniqueTags.add(tag.getName());
            }
        }

        return new ArrayList<>(uniqueTags);
    }
}
