package com.example.expensemate.repository;

import android.content.Context;

import com.example.expensemate.dao.RecordDao;
import com.example.expensemate.database.AppDatabase;
import com.example.expensemate.entity.RecordEntity;
import com.example.expensemate.entity.RecordMapper;
import com.example.expensemate.entity.RecordTagCrossRef;
import com.example.expensemate.entity.RecordWithTags;
import com.example.expensemate.model.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecordRepository {
    private RecordDao recordDao;

    public RecordRepository(Context context){
        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        recordDao = db.recordDao();
    }

    public List<Record> getAllRecords() {
        List<RecordWithTags> recordWithTags = recordDao.getAllRecords();
        List<Record> records = new ArrayList<>();
        for (RecordWithTags entity : recordWithTags) {
            records.add(RecordMapper.toModel(entity));
        }
        return records;
    }

    public void insertRecord(Record record) {
        RecordEntity recordEntity = RecordMapper.toEntity(record);
        recordDao.insertRecord(recordEntity);

        List<RecordTagCrossRef> refs = record.getTags().stream()
                .map(tag -> new RecordTagCrossRef(record.getId(), tag.getName()))
                .collect(Collectors.toList());

        recordDao.insertRecordTagsRef(refs);
    }

    public void updateRecord(Record record) {
        RecordEntity recordEntity = RecordMapper.toEntity(record);
        recordDao.update(recordEntity);

        List<RecordTagCrossRef> refs = record.getTags().stream()
                .map(tag -> new RecordTagCrossRef(record.getId(), tag.getName()))
                .collect(Collectors.toList());

        recordDao.deleteRecordTagsRefByRecordId(record.getId());
        recordDao.insertRecordTagsRef(refs);
    }

    public void deleteRecord(String recordId) {
        recordDao.deleteById(recordId);
    }
}
