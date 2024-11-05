package com.example.simplemute.broadcastreciver.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.simplemute.Mute;

import java.util.List;

@Dao
public interface MuteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Mute mute);

    @Query("DELETE FROM mute_table")
    void deleteAll();

    @Query("SELECT * FROM mute_table ORDER BY muteId ASC")
    LiveData<List<Mute>> getMutes();


    @Update
    void update(Mute mute);

    @Query("Delete from mute_table where muteId = :muteID")
    void delete(int muteID);

}
