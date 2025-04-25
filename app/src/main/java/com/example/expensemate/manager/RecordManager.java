package com.example.expensemate.manager;

import com.example.expensemate.model.Record;
import com.example.expensemate.model.Tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordManager {
    private Map<String, Record> recordList;
    private Record record;

    public RecordManager(){
        this.recordList = new HashMap<>();
    }

    public void createRecord(){
        this.record = new Record();
    }

    public void selectRecord(String recordId){
        this.record = this.recordList.get(recordId);
    }

    public void selectType(String type){
        this.record.setType(type);
    }

    public void enterName(String name){
        this.record.setName(name);
    }

    public void enterPrice(float price){
        this.record.setPrice(price);
    }

    public void selectDate(Date date){
        this.record.setDate(date);
    }

    public void setTag(Tag tag){
        this.record.setTag(tag);
    }

    public void deleteTag(Tag tag){
        this.record.deleteTag(tag);
    }

    public void saveRecord(){
        this.recordList.put(this.record.getId(), this.record);
    }

    public void editRecord() {
        this.recordList.put(this.record.getId(), this.record);
    }

    public Map<String, Record> getRecordList() {
        return recordList;
    }

    public List<String> getSelectedTags() {
        List<String> selectedTags = new ArrayList<>();
        for (Tag tag : this.record.getTags()) {
            selectedTags.add(tag.getName());
        }
        return selectedTags;
    }
}
