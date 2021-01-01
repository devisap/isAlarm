package com.id.ac.stiki.doleno.isalarm.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AlarmModel {
    @PrimaryKey (autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo (name = "code")
    public int code;

    @ColumnInfo (name = "group")
    public int group;

    @ColumnInfo (name = "title")
    public String title;

    @ColumnInfo (name = "hours")
    public int hours;

    @ColumnInfo (name = "minutes")
    public int minutes;

    @ColumnInfo (name = "isRepeat")
    public boolean isRepeat;

    @ColumnInfo (name = "isDaily")
    public boolean isDaily;

    @ColumnInfo (name = "isActive")
    public boolean isActive;

    @ColumnInfo (name = "date")
    public int date;

}
