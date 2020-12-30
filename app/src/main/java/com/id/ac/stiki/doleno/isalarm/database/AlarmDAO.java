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

    @Delete
    public void deleteAlarm(AlarmModel alarmModel);
}
