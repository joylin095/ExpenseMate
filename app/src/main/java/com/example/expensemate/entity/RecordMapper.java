package com.example.expensemate.entity;

import com.example.expensemate.model.Record;

public class RecordMapper {
    public static RecordEntity toEntity(Record record) {
        RecordEntity entity = new RecordEntity();
        entity.setId(record.getId());
        entity.setName(record.getName());
        entity.setType(record.getType());
        entity.setPrice(record.getPrice());
        entity.setDate(record.getDate());
        entity.setTags(record.getTags());
        return entity;
    }

    public static Record toModel(RecordEntity entity) {
        Record record = new Record();
        record.setId(entity.getId());
        record.setName(entity.getName());
        record.setType(entity.getType());
        record.setPrice(entity.getPrice());
        record.setDate(entity.getDate());
        record.setTags(entity.getTags());
        return record;
    }
}
