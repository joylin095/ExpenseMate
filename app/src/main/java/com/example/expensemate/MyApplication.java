package com.example.expensemate;

import android.app.Application;

import com.example.expensemate.model.User;

public class MyApplication extends Application {
    private User user = null;
    private static MyApplication mInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        user = new User();
        mInstance = this;
    }

    public User getUser() {
        return user;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}
