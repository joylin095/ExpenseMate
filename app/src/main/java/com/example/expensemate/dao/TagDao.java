package com.example.expensemate.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.expensemate.entity.TagEntity;

import java.util.List;
import java.util.Set;

@Dao
public interface TagDao {
     @Insert(onConflict = OnConflictStrategy.IGNORE)
     void insert(TagEntity tag);

     @Query("SELECT * FROM tag")
     List<TagEntity> getAllTags();
}
