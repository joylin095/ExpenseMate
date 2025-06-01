package com.example.expensemate.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"id", "name"},
        foreignKeys = {
                @ForeignKey(entity = RecordEntity.class,
                        parentColumns = "id",
                        childColumns = "id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = TagEntity.class,
                        parentColumns = "name",
                        childColumns = "name",
                        onDelete = ForeignKey.CASCADE)
        })
public class RecordTagCrossRef {
    @NonNull
    public String id;
    @NonNull
    public String name;

    public RecordTagCrossRef(@NonNull String id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }
}
