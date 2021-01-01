package com.id.ac.stiki.doleno.isalarm.database;

import android.telecom.Call;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface AlarmDAO {
    @Query("SELECT * FROM alarmmodel")
    LiveData<List<AlarmModel>> getAlarm();

    @Insert
    public void insertAlarm(AlarmModel alarmModel);

    @Query("UPDATE AlarmModel SET isActive = :isActive WHERE id = :id")
    public void updateAlarm(int id, boolean isActive);

    @Delete
    public void deleteAlarm(AlarmModel alarmModel);
}
