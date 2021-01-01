package com.id.ac.stiki.doleno.isalarm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.id.ac.stiki.doleno.isalarm.database.AlarmDatabase;
import com.id.ac.stiki.doleno.isalarm.database.AlarmModel;
import com.id.ac.stiki.doleno.isalarm.repository.AlarmRepository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private  AlarmRepository alarmRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        alarmRepository = new AlarmRepository(application);
    }

    public LiveData<List<AlarmModel>> getListData(){
        return alarmRepository.getAlarm();
    }
}
