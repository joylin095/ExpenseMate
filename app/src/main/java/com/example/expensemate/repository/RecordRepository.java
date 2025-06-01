package com.example.expensemate.repository;

import android.content.Context;

import com.example.expensemate.dao.RecordDao;
import com.example.expensemate.database.AppDatabase;
import com.example.expensemate.entity.RecordEntity;
import com.example.expensemate.entity.RecordMapper;
import com.example.expensemate.model.Record;

import java.util.ArrayList;
import java.util.List;

public class RecordRepository {
    private RecordDao recordDao;

    public RecordRepository(Context context){
        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        recordDao = db.recordDao();
    }

    public List<Record> getAllRecords() {
        List<RecordEntity> recordEntities = recordDao.getAllRecords();
        List<Record> records = new ArrayList<>();
        for (RecordEntity entity : recordEntities) {
            records.add(RecordMapper.toModel(entity));
        }
        return records;
    }

    public void insertRecord(Record record) {
        RecordEntity recordEntity = RecordMapper.toEntity(record);
        recordDao.insert(recordEntity);
    }

    public void updateRecord(Record record) {
        RecordEntity recordEntity = RecordMapper.toEntity(record);
        recordDao.update(recordEntity);
    }

    public void deleteRecord(String recordId) {
        recordDao.deleteById(recordId);
    }
}
