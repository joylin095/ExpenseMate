package com.example.expensemate.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class Record {
    private static final float MAX_PRICE = 100000000.0f;
    private String id;
    private String name;
    private String type;
    private float price;
    private Date date;
    private Set<Tag> tags;

    public Record() {
        this.id = UUID.randomUUID().toString();
        this.date = new Date();
        this.tags = new LinkedHashSet<>();
    }

    public Record(Record record){
        this.id = record.id;
        this.name = record.name;
        this.type = record.type;
        this.price = record.price;
        this.date = record.date;
        this.tags = new LinkedHashSet<>(record.tags);
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
        if (!isNameValid(name)){
            throw new IllegalArgumentException("Invalid name");
        }
    }

    public boolean isNameValid(String name) {
        return name != null && !name.isEmpty();
    }

    public void setPrice(float price) {
        if (price > MAX_PRICE) {
            this.price = MAX_PRICE;
        }
        else{
            this.price = price;
        }
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTag(Tag tag) {
        this.tags.add(tag);
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void deleteTag(Tag tag) {
        if (tags.size() == 1) {
            throw new IllegalArgumentException("Cannot delete the last tag");
        }
        this.tags.remove(tag);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public float getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }

    public boolean isValid() {
        return isNameValid(name) && type != null && price > 0 && date != null && !tags.isEmpty();
    }
}