package com.example.expensemate.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.expensemate.dao.RecordDao;
import com.example.expensemate.dao.TagDao;
import com.example.expensemate.entity.RecordEntity;
import com.example.expensemate.entity.RecordTagCrossRef;
import com.example.expensemate.entity.TagEntity;

@Database(entities = {RecordEntity.class, TagEntity.class, RecordTagCrossRef.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase AppDatabaseInstance;
    public abstract RecordDao recordDao();
    public abstract TagDao tagDao();

    public static AppDatabase getInstance(Context context){
        if (AppDatabaseInstance == null) {
            synchronized (AppDatabase.class){
                if (AppDatabaseInstance == null) {
                    AppDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "expensemate-db")
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    new Thread(() -> {
                                        TagDao tagDao = getInstance(context).tagDao();
                                        tagDao.insert(new TagEntity("食物"));
                                        tagDao.insert(new TagEntity("娛樂"));
                                        tagDao.insert(new TagEntity("交通"));
                                    }).start();
                                }
                            })
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return AppDatabaseInstance;
    }
}
