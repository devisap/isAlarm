package com.id.ac.stiki.doleno.isalarm.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.id.ac.stiki.doleno.isalarm.database.AlarmModel;
import com.id.ac.stiki.doleno.isalarm.repository.AlarmRepository;

import java.util.List;

public class AddAlarmViewModel extends AndroidViewModel {
    private AlarmRepository alarmRepository;
    private Context context;


    public AddAlarmViewModel(@NonNull Application application) {
        super(application);
        alarmRepository = new AlarmRepository(application);
        context = application.getApplicationContext();
    }

    public void insertAlarm(AlarmModel alarmModel){
        alarmRepository.insertAlarm(alarmModel);
    }

    public LiveData<List<AlarmModel>> getListData(){
        return alarmRepository.getAlarm();
    }

    public void insertMultipleAlarm(List<AlarmModel> list){
        for (AlarmModel model: list
             ) {
            insertAlarm(model);
        }
    }
}
