package com.example.simplemute.broadcastreciver.database;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.example.simplemute.Mute;

import java.util.List;

public class MuteRepository {

    MuteDao muteDao;
    LiveData<List<Mute>> muteLiveData;

    public MuteRepository(Application application) {
        MuteDatabase db = MuteDatabase.getDatabase(application);
        muteDao = db.alarmDao();
        muteLiveData = muteDao.getMutes();
    }

    public void insert(Mute mute) {
        MuteDatabase.databaseWriteExecutor.execute(() -> {
            muteDao.insert(mute);
        });
        Log.d("insert","from muteRepository");
    }

    public void update(Mute mute) {
        MuteDatabase.databaseWriteExecutor.execute(() -> {
            muteDao.update(mute);
        });
    }

    public LiveData<List<Mute>> getAlarmsLiveData() {
        return muteLiveData;
    }

    public void delete(int muteId){
        MuteDatabase.databaseWriteExecutor.execute(() -> {
            muteDao.delete(muteId);
        });
    }
}
