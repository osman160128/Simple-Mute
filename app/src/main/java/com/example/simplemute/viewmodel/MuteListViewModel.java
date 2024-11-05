package com.example.simplemute.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.simplemute.Mute;
import com.example.simplemute.broadcastreciver.database.MuteRepository;

import java.util.List;

public class MuteListViewModel extends AndroidViewModel {

    private MuteRepository muteRepository;
    private LiveData<List<Mute>>  muteLiveData;
    public MuteListViewModel(@NonNull Application application) {
        super(application);

        muteRepository = new MuteRepository(application);
        muteLiveData = muteRepository.getAlarmsLiveData();

    }
    public void update(Mute mute) {
        muteRepository.update(mute);
    }

    public LiveData<List<Mute>> getAlarmsLiveData() {
        return muteLiveData;
    }

    public void delete(int alarmId){muteRepository.delete(alarmId);
    }

}
