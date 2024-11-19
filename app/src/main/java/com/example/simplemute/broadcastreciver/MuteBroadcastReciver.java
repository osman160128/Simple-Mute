package com.example.simplemute.broadcastreciver;

import static androidx.core.content.ContextCompat.getSystemService;


import static com.example.simplemute.fragments.MuteListFragment.muteListViewModel;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.example.simplemute.Mute;
import com.example.simplemute.R;

import java.util.Calendar;

public class MuteBroadcastReciver extends BroadcastReceiver {

    Mute mute;
    AudioManager audioManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String toastText = String.format("Alarm Reboot");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            startRescheduleAlarmsService(context);
        }
        else {

            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            Bundle bundle = intent.getBundleExtra(context.getString(R.string.bundle_alarm_obj));

            if (bundle != null)
                mute = (Mute) bundle.getSerializable(context.getString(R.string.arg_alarm_obj));
            String toastText = String.format("Alarm Received");
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
            if (mute != null) {
                if (!mute.isRecurring()) {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    startAlarmService(context, mute);
                } else {
                    if (isAlarmToday(mute)) {
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        startAlarmService(context, mute);
                    }
                }
            } else {
                Toast.makeText(context, "mute is not null", Toast.LENGTH_SHORT).show();
            }
        }

    }
    boolean isAlarmToday(Mute alarm1) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch(today) {
            case Calendar.MONDAY:
                if (alarm1.isMonday())
                    return true;
                return false;
            case Calendar.TUESDAY:
                if (alarm1.isTuseday())
                    return true;
                return false;
            case Calendar.WEDNESDAY:
                if (alarm1.isWednesday())
                    return true;
                return false;
            case Calendar.THURSDAY:
                if (alarm1.isThursday())
                    return true;
                return false;
            case Calendar.FRIDAY:
                if (alarm1.isFriday())
                    return true;
                return false;
            case Calendar.SATURDAY:
                if (alarm1.isSaturday())
                    return true;
                return false;
            case Calendar.SUNDAY:
                if (alarm1.isSunday())
                    return true;
                return false;
        }
        return false;
    }
    void startAlarmService(Context context, Mute mute1) {
        Intent intentService = new Intent(context, MuteService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(context.getString(R.string.arg_alarm_obj), mute1);
        intentService.putExtra(context.getString(R.string.bundle_alarm_obj), bundle);

     // Pass if it's a start or end alarm

     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intentService);
                } else {
                    context.startService(intentService);
                }

    }
    private void startRescheduleAlarmsService(Context context) {
        Intent intentService = new Intent(context, RescheduleAlarmsService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
}
