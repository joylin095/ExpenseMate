package com.example.expensemate.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class Record {
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

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
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
}