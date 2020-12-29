package com.id.ac.stiki.doleno.isalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TimePicker;

public class AddAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        TimePicker timePicker = (TimePicker)findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
    }
}