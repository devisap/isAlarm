package com.id.ac.stiki.doleno.isalarm.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.NotificationManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

import com.id.ac.stiki.doleno.isalarm.R;
import com.id.ac.stiki.doleno.isalarm.database.AlarmModel;
import com.id.ac.stiki.doleno.isalarm.viewmodel.AlarmActiveViewModel;

import java.util.Objects;

public class AlarmActive extends AppCompatActivity {
    private int id, hour, minute, code, date;
    private boolean repeat, daily;
    private AlarmActiveViewModel alarmActiveViewModel;

    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    private Sensor mLightSensor;
    private float mLightQuantity;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_active);

        id = getIntent().getIntExtra("ID", 0);
        hour = getIntent().getIntExtra("HOUR", 0);
        minute = getIntent().getIntExtra("MINUTE", 0);
        repeat = getIntent().getBooleanExtra("REPEAT", false);
        daily = getIntent().getBooleanExtra("DAILY", false);
        date = getIntent().getIntExtra("DATE", 0);
        code = getIntent().getIntExtra("CODE", 0);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_NORMAL);
//        mAccel = 10f;
//        mAccelCurrent = SensorManager.GRAVITY_EARTH;
//        mAccelLast = SensorManager.GRAVITY_EARTH;

        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(
                mSensorListener, mLightSensor, SensorManager.SENSOR_DELAY_UI);

        alarmActiveViewModel = ViewModelProviders.of(this).get(AlarmActiveViewModel.class);
        setRepeat();

    }

    public void setRepeat() {
        if (repeat) {
            AlarmModel model = new AlarmModel();
            model.id = id;
            model.hours = hour;
            model.minutes = minute;
            model.isRepeat = repeat;
            model.isDaily = daily;
            model.code = code;
            model.date   = date;
            alarmActiveViewModel.setRepeatAlarm(model);
        }
    }

    public void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            mLightQuantity = event.values[0];
            Toast.makeText(getApplicationContext(), String.valueOf(mLightQuantity), Toast.LENGTH_SHORT).show();
//            float x = event.values[0];
//            float y = event.values[1];
//            float z = event.values[2];
//            mAccelLast = mAccelCurrent;
//            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
//            float delta = mAccelCurrent - mAccelLast;
//            mAccel = mAccel * 0.9f + delta;
//            if (mAccel > 12) {
//                count++;
//                if(count > 20){
//                    clearNotification();
//                    finish();
//                }
//
//                Toast.makeText(getApplicationContext(), "Shake event detected"+count, Toast.LENGTH_SHORT).show();
//
//            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    protected void onResume() {
//        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

}