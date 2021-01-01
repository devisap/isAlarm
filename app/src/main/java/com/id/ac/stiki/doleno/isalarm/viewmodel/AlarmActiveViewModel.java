package com.id.ac.stiki.doleno.isalarm.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.id.ac.stiki.doleno.isalarm.database.AlarmModel;
import com.id.ac.stiki.doleno.isalarm.service.AlarmService;

public class AlarmActiveViewModel extends AndroidViewModel {
    private AlarmService alarmService;
    private Context context;

    public AlarmActiveViewModel(@NonNull Application application) {
        super(application);
        alarmService = new AlarmService();
        context = application.getApplicationContext();
    }

    public void setRepeatAlarm(AlarmModel model) {
        alarmService.repeatAlarm(context, model);
    }


}
