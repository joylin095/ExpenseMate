package com.example.expensemate.entity;

import com.example.expensemate.model.Record;
import com.example.expensemate.model.Tag;

import java.util.Date;
import java.util.stream.Collectors;

public class RecordMapper {
    public static RecordEntity toEntity(Record record) {
        RecordEntity entity = new RecordEntity();
        entity.setId(record.getId());
        entity.setName(record.getName());
        entity.setType(record.getType());
        entity.setPrice(record.getPrice());
        entity.setDate(record.getDate().getTime());
        return entity;
    }

    public static Record toModel(RecordWithTags recordWithTags) {
        Record record = new Record();
        RecordEntity entity = recordWithTags.record;
        record.setId(entity.getId());
        record.setName(entity.getName());
        record.setType(entity.getType());
        record.setPrice(entity.getPrice());
        record.setDate(new Date(entity.getDate()));
        record.setTags(recordWithTags.tags.stream()
                .map(tag -> new Tag(tag.getName()))
                .collect(Collectors.toSet()));
        return record;
    }
}
