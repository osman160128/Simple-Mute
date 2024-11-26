package com.example.simplemute.broadcastreciver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.simplemute.MainActivity;
import com.example.simplemute.Mute;
import com.example.simplemute.R;
import com.example.simplemute.util.OnToggleMuteListener;

import java.util.Calendar;

public class MuteService extends Service {

    private static final String CHANNEL_ID = "My Channel";
    Mute mute;
    String muteTitle;
    int muteHourFrom;
    int muteMinuteFrom;
    int muteHourTo;
    int muteMinuteTo;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 1 minute delay
        Bundle bundle = intent.getBundleExtra(getString(R.string.bundle_alarm_obj));

        if (bundle != null) {
            mute = (Mute) bundle.getSerializable(getString(R.string.arg_alarm_obj));
        }

        if (mute != null) {
            muteTitle = mute.getTitle();
            muteHourFrom = mute.getHourFrom();
            muteMinuteFrom = mute.getMinuteFrom();
            muteHourTo = mute.getHourTo();
            muteMinuteTo = mute.getMinuteTo();
        }

        // Get the current time
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY); // 24-hour format
        int currentMinute = currentTime.get(Calendar.MINUTE);

        // Compare current time with mute's "To" time
        if (isTimeLessThan(currentHour, currentMinute, muteHourTo, muteMinuteTo)) {
            // Show the notification if the current time is less than mute's "To" time
            showNotification();
        } else {
            // Cancel the notification if the current time is greater than or equal to mute's "To" time
            cancelNotification();
        }

        return START_STICKY;
    }

    private boolean isTimeLessThan(int currentHour, int currentMinute, int muteHourTo, int muteMinuteTo) {
        if (currentHour < muteHourTo) {
            return true;
        } else if (currentHour == muteHourTo) {
            return currentMinute < muteMinuteTo;
        }
        return false;
    }

    private void showNotification() {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent iNotify = new Intent(getApplicationContext(), MainActivity.class);
        iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, iNotify, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(muteTitle)
                .setContentText("Your Mute starts from " + muteHourFrom + "." + muteMinuteFrom + " to " + muteHourTo + "." + muteMinuteTo)
                .setSmallIcon(R.drawable.muteicon)
                .setSound(null)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setFullScreenIntent(pendingIntent, true)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH));
        }

        startForeground(1, notification);
    }

    private void cancelNotification() {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(1); // Cancels the notification
        stopForeground(true); // Stop the service from running in the foreground
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
