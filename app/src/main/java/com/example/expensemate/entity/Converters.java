package com.example.expensemate.entity;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import com.example.expensemate.model.Tag;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Set;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String fromTags(Set<Tag> tags){
        return new Gson().toJson(tags);
    }

    @TypeConverter
    public static Set<Tag> toTags(String tagsJson) {
        Type type = new TypeToken<Set<Tag>>() {}.getType();
        return new Gson().fromJson(tagsJson, type);
    }
}
