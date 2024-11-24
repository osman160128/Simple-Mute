package com.example.simplemute.broadcastreciver.database;

import static com.example.simplemute.fragments.MuteListFragment.muteListViewModel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.simplemute.Mute;
import com.example.simplemute.R;
import com.example.simplemute.broadcastreciver.RescheduleAlarmsService;

import java.util.Calendar;

public class UnMuteBroadCastReciver extends BroadcastReceiver {

    AudioManager audioManager;
    Mute mute;

    @Override
    public void onReceive(Context context, Intent intent) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Bundle bundle=intent.getBundleExtra(context.getString(R.string.bundle_alarm_obj));

        if (bundle!=null)
            mute =(Mute)bundle.getSerializable(context.getString(R.string.arg_alarm_obj));
        if (mute != null) {
            if (!mute.isRecurring()) {

                if(!mute.isStarted()){
                    muteListViewModel.delete(mute.getMuteId());
                    mute.cancelAlarm(context);
                }
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            } else {
                if (isAlarmToday(mute)) {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
            }

        }
    }

    boolean isAlarmToday(Mute alarm1) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int today = calendar.get(Calendar.DAY_OF_WEEK);

        switch (today) {
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
}