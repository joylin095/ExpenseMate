package com.example.expensemate.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.expensemate.entity.RecordEntity;
import com.example.expensemate.entity.RecordTagCrossRef;
import com.example.expensemate.entity.RecordWithTags;

import java.util.List;

@Dao
public interface RecordDao {
    @Insert
    void insertRecord(RecordEntity record);

    @Insert
    void insertRecordTagsRef(List<RecordTagCrossRef> refs);

    @Update
    void update(RecordEntity record);

    @Query("DELETE FROM recordtagcrossref WHERE id = :recordId")
    void deleteRecordTagsRefByRecordId(String recordId);

    @Query("DELETE FROM record WHERE id = :recordId")
    void deleteById(String recordId);


    @Transaction
    @Query("SELECT * FROM record")
    List<RecordWithTags> getAllRecords();
}
