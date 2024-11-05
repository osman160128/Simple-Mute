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

public class MuteService  extends Service {

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

        if(bundle!=null) {
            mute = (Mute)  bundle.getSerializable(getString(R.string.arg_alarm_obj));
        }


        if(mute!=null){
            muteTitle = mute.getTitle();
            muteHourFrom = mute.getHourFrom();
            muteMinuteFrom = mute.getMinuteFrom();
            muteHourTo  = mute.getHourTo();
            muteMinuteTo = mute.getMinuteTo();
        }


        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent iNotify = new Intent(getApplicationContext(), MainActivity.class);
        iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,iNotify,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(muteTitle)
                .setContentText("Your Mute start from "+muteHourFrom+"."+muteMinuteFrom+":"+muteHourTo+"."+muteMinuteTo)
                .setSmallIcon(R.drawable.silence)
                .setSound(null)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setFullScreenIntent(pendingIntent,true)
                .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"new Channel",NotificationManager.IMPORTANCE_HIGH));
        }

    //    nm.notify(PERMISIION_REQUEST_CODE,notification);
        startForeground(1, notification);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
