package com.example.simplemute.broadcastreciver;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;

import com.example.simplemute.Mute;
import com.example.simplemute.broadcastreciver.database.MuteRepository;

import java.util.List;

public class RescheduleAlarmsService extends LifecycleService {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

       MuteRepository muteRepository= new MuteRepository(getApplication());

        muteRepository.getAlarmsLiveData().observe(this, new Observer<List<Mute>>() {
            @Override
            public void onChanged(List<Mute> alarms) {
                for (Mute a : alarms) {
                    if (a.isStarted()) {
                        a.shedule(getApplicationContext());

                    }
                }
            }
        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }
}
