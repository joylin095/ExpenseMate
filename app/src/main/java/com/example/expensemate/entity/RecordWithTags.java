package com.example.expensemate.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.Set;

public class RecordWithTags {
    @Embedded
    public RecordEntity record;

    @Relation(
        parentColumn = "id",
        entityColumn = "name",
        associateBy = @Junction(RecordTagCrossRef.class)
    )
    public Set<TagEntity> tags;
}
