package com.id.ac.stiki.doleno.isalarm.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.id.ac.stiki.doleno.isalarm.database.AlarmModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {AlarmModel.class}, version = 1, exportSchema = false)
public abstract class AlarmDatabase extends RoomDatabase {
    private static final String DB_NAME = "alarm_db";
    private static AlarmDatabase instance;
    public static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static synchronized AlarmDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AlarmDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract AlarmDAO alarmDAO();
}