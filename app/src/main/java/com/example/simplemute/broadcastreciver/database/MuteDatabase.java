package com.example.simplemute.broadcastreciver.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.simplemute.Mute;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Mute.class}, version = 1, exportSchema = false)
public abstract class MuteDatabase extends RoomDatabase {

    public abstract MuteDao alarmDao();

    static volatile MuteDatabase INSTANCE;
    static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MuteDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MuteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MuteDatabase.class,
                            "mute_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }

}
