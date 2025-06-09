package com.example.expensemate;

import android.app.Application;

import com.example.expensemate.model.Chart;
import com.example.expensemate.model.RecordManager;
import com.example.expensemate.model.TagManager;
import com.example.expensemate.model.User;

public class MyApplication extends Application {
    private User user = null;
    private Chart chart = null;
    private RecordManager recordManager = null;
    private TagManager tagManager = null;
    private static MyApplication mInstance = null;

    @Override
    public void onCreate() {
        this.recordManager = new RecordManager();
        this.tagManager = new TagManager();
        super.onCreate();
        user = new User(recordManager, tagManager);
        chart = new Chart(recordManager);
        mInstance = this;
    }

    public User getUser() {
        return user;
    }

    public Chart getChart() {
        return chart;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}
