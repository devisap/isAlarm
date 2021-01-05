package com.id.ac.stiki.doleno.isalarm.database;

import android.telecom.Call;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface AlarmDAO {
    @Query("SELECT * FROM alarmmodel GROUP BY `group`")
    LiveData<List<AlarmModel>> getAlarm();

    @Query("SELECT * FROM alarmmodel WHERE `group` = :group")
    List<AlarmModel> getGroupAlarm(int group);

    @Insert
    public void insertAlarm(AlarmModel alarmModel);

    @Query("UPDATE AlarmModel SET isActive = :isActive WHERE id = :id")
    public int updateAlarm(int id, boolean isActive);

//    @Update
//    public void updateAlarm(AlarmModel alarmModel);

    @Delete
    public void deleteAlarm(AlarmModel alarmModel);
}
