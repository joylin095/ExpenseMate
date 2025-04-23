package com.example.expensemate.model;

import java.util.Date;
import java.util.UUID;

public class Record {
    private String id;
    private String name;
    private String type;
    private float price;
    private Date date;

    public Record() {
        this.id = UUID.randomUUID().toString();
        this.date = new Date();
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