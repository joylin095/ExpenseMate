package com.example.expensemate.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.expensemate.model.Tag;

import java.util.Date;
import java.util.Set;

@Entity(tableName = "record")
public class RecordEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String type;
    private float price;
    @TypeConverters(Converters.class)
    private Date date;
    @TypeConverters(Converters.class)
    private Set<Tag> tags;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
}
