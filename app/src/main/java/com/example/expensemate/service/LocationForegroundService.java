package com.example.expensemate.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.util.Log;

import com.example.expensemate.MyApplication;
import com.example.expensemate.R;
import com.example.expensemate.model.NotifyHandler;

public class LocationForegroundService extends Service {
    private Handler handler;
    private Runnable locationRunnable;
    private static final long INTERVAL = 6 * 1000;
    private static final String CHANNEL_ID = "location_service_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("LocationService", "onCreate() called");
        createNotificationChannel();

        handler = new Handler();
        locationRunnable = new Runnable() {
            @Override
            public void run() {
                NotifyHandler notifyHandler = MyApplication.getInstance().getNotifyHandler();
                notifyHandler.checkLocationAndNotify(shouldNotify -> {
                    if (shouldNotify) {
                        sendUserNotification("Expense Mate", "記帳囉!");
                    }
                });

                handler.postDelayed(this, INTERVAL);
            }
        };
        handler.post(locationRunnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, createForegroundNotification());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(locationRunnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification createForegroundNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("位置追蹤中")
                .setContentText("正在背景持續抓取位置")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
    }

    private void sendUserNotification(String title, String message) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();
        Log.i("LocationService", "send user out of check");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            manager.notify((int) System.currentTimeMillis(), notification); // 唯一通知 ID
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "位置服務通知",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}