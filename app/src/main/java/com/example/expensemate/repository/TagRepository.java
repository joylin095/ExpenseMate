package com.example.expensemate.repository;

import android.content.Context;

import com.example.expensemate.dao.TagDao;
import com.example.expensemate.database.AppDatabase;
import com.example.expensemate.entity.TagEntity;

import java.util.List;
import java.util.stream.Collectors;

public class TagRepository {
    private TagDao tagDao;

    public TagRepository(Context context){
        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        tagDao = db.tagDao();
    }

    public List<String> getAllTags() {
        List<TagEntity> tagEntities = tagDao.getAllTags();
        return tagEntities.stream()
                .map(TagEntity::getName)
                .collect(Collectors.toList());
    }

    public void insertTag(String tagName) {
        tagDao.insert(new TagEntity(tagName));
    }
}
