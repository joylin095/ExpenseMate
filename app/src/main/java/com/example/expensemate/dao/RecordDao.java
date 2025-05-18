package com.example.expensemate.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.expensemate.entity.RecordEntity;

import java.util.List;

@Dao
public interface RecordDao {
    @Insert
    void insert(RecordEntity record);

    @Update
    void update(RecordEntity record);

    @Query("DELETE FROM record WHERE id = :recordId")
    void deleteById(String recordId);


    @Query("SELECT * FROM record")
    List<RecordEntity> getAllRecords();
}
