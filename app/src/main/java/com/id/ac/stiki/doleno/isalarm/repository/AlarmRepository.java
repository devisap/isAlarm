package com.id.ac.stiki.doleno.isalarm.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.id.ac.stiki.doleno.isalarm.database.AlarmDAO;
import com.id.ac.stiki.doleno.isalarm.database.AlarmDatabase;
import com.id.ac.stiki.doleno.isalarm.database.AlarmModel;

import java.util.List;

public class AlarmRepository {
    private AlarmDAO alarmDAO;

    public AlarmRepository(Application application) {
        alarmDAO = AlarmDatabase.getInstance(application).alarmDAO();

    }

    public LiveData<List<AlarmModel>> getAlarm(){
        return alarmDAO.getAlarm();
    }

    public void insertAlarm (final AlarmModel alarmModel){
        AlarmDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                alarmDAO.insertAlarm(alarmModel);
            }
        });
    }

    public void updateAlarm(final AlarmModel alarmModel){
        AlarmDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                alarmDAO.updateAlarm(alarmModel);
            }
        });
    }

    public void deletAlarm(final AlarmModel alarmModel){
        AlarmDatabase.executorService.execute(new Runnable() {
            @Override
            public void run() {
                alarmDAO.deleteAlarm(alarmModel);
            }
        });
    }
}
