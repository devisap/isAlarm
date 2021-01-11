package com.id.ac.stiki.doleno.isalarm.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.id.ac.stiki.doleno.isalarm.database.AlarmModel;
import com.id.ac.stiki.doleno.isalarm.repository.AlarmRepository;

import java.util.Calendar;

public class AlarmService {
    public void createAlarm(Context context, AlarmModel model) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        if (model.isRepeat && !model.isDaily) {
            if(calendar.get(Calendar.DAY_OF_WEEK) > model.date){
                calendar.add(Calendar.DATE, 7 - model.date);
            }
            calendar.set(Calendar.DAY_OF_WEEK, model.date);
        }
        calendar.set(Calendar.HOUR_OF_DAY, model.hours);
        calendar.set(Calendar.MINUTE, model.minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmBroadcast.class);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.putExtra("ID", model.id);
        intent.putExtra("TITLE", model.title);
        intent.putExtra("HOUR", model.hours);
        intent.putExtra("MINUTE", model.minutes);
        intent.putExtra("REPEAT", model.isRepeat);
        intent.putExtra("DAILY", model.isDaily);
        intent.putExtra("DATE", model.date);
        intent.putExtra("CODE", model.code);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, model.code, intent, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    public void stopAlarm(Context context, AlarmModel model) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcast.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, model.code, intent, 0);
        alarmManager.cancel(alarmPendingIntent);
        //this.started = false;

        String toastText = String.format("Alarm cancelled for %02d:%02d with id %d", model.hours, model.minutes, model.id);
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
        Log.e("cancel", toastText);
    }

    public void repeatAlarm(Context context, AlarmModel model) {
        Calendar calendar = Calendar.getInstance();
        if (model.isRepeat && model.isDaily) {
            calendar.add(Calendar.DATE, 1);
        } else if (model.isRepeat && !model.isDaily) {
//            calendar.set(Calendar.DAY_OF_WEEK, model.date);
            calendar.add(Calendar.DATE, 7);
        }
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, model.hours);
        calendar.set(Calendar.MINUTE, model.minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmBroadcast.class);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.putExtra("ID", model.id);
        intent.putExtra("HOUR", model.hours);
        intent.putExtra("MINUTE", model.minutes);
        intent.putExtra("REPEAT", model.isRepeat);
        intent.putExtra("DAILY", model.isDaily);
        intent.putExtra("DATE", model.date);
        intent.putExtra("CODE", model.code);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, model.code, intent, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}
