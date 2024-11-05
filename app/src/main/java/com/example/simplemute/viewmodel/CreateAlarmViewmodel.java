package com.example.simplemute.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.simplemute.Mute;
import com.example.simplemute.broadcastreciver.database.MuteRepository;

public class CreateAlarmViewmodel extends AndroidViewModel {

    private MuteRepository muteRepository;
    public CreateAlarmViewmodel(@NonNull Application application) {
        super(application);

        muteRepository = new MuteRepository(application);
    }

    public void insert(Mute mute){
        muteRepository.insert(mute);
    }

    public void update(Mute mute){
        muteRepository.update(mute);
    }

}
