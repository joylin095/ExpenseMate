package com.example.expensemate.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.expensemate.dao.RecordDao;
import com.example.expensemate.entity.Converters;
import com.example.expensemate.entity.RecordEntity;

@Database(entities = {RecordEntity.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase AppDatabaseInstance;
    public abstract RecordDao recordDao();

    public static AppDatabase getInstance(Context context){
        if (AppDatabaseInstance == null) {
            synchronized (AppDatabase.class){
                if (AppDatabaseInstance == null) {
                    AppDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "expensemate-db").build();
                }
            }
        }
        return AppDatabaseInstance;
    }
}
