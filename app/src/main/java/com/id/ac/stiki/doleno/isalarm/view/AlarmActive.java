package com.id.ac.stiki.doleno.isalarm.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.id.ac.stiki.doleno.isalarm.R;
import com.id.ac.stiki.doleno.isalarm.database.AlarmModel;
import com.id.ac.stiki.doleno.isalarm.viewmodel.AlarmActiveViewModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class AlarmActive extends AppCompatActivity {
    private int id, hour, minute, code, date, confirmDirection;
    private boolean repeat, daily;
    private String label, confirmNumber, confirmNumberText;
    private AlarmActiveViewModel alarmActiveViewModel;

    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    private Sensor mLightSensor;
    private float mLightQuantity;
    private int count = 0;

    private Sensor mOrientationSensor;
    private String direction;
    public static DecimalFormat DECIMAL_FORMATTER;

    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;

    private EditText editText;
    private TextView txt_label, txt_alarm, txt_direction, txt_currentDirection, txt_currentShake, txt_currentIntensityLight;
    private String titleMinutes, titleHours;
    private ImageView img_direction, img_number;
    private LinearLayout section_shake, section_direction, section_light, section_voice;

    private int randSelectStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_active);
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);



        id = getIntent().getIntExtra("ID", 0);
        label = getIntent().getStringExtra("TITLE");
        hour = getIntent().getIntExtra("HOUR", 0);
        hour = getIntent().getIntExtra("HOUR", 0);
        minute = getIntent().getIntExtra("MINUTE", 0);
        repeat = getIntent().getBooleanExtra("REPEAT", false);
        daily = getIntent().getBooleanExtra("DAILY", false);
        date = getIntent().getIntExtra("DATE", 0);
        code = getIntent().getIntExtra("CODE", 0);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        txt_direction = findViewById(R.id.txt_direction);
        txt_currentDirection = findViewById(R.id.txt_currentDirection);
        txt_currentShake = findViewById(R.id.txt_currentShake);
        txt_currentIntensityLight = findViewById(R.id.txt_currentIntensityLight);
        img_direction = findViewById(R.id.img_direction);
        img_number = findViewById(R.id.img_number);
        section_shake = findViewById(R.id.section_shake);
        section_direction = findViewById(R.id.section_direction);
        section_light = findViewById(R.id.section_light);
        section_voice = findViewById(R.id.section_voice);

        txt_label = findViewById(R.id.txt_label);
        txt_alarm = findViewById(R.id.txt_alarm);

        if(minute < 10){
            titleMinutes = "0"+minute;
        }else{
            titleMinutes = String.valueOf(minute);
        }

        if(hour < 10){
            titleHours = "0"+hour;
        }else{
            titleHours = String.valueOf(hour);
        }

        txt_label.setText(label);
        txt_alarm.setText(titleHours+":"+titleMinutes);


//        shake sensor
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

//        light sensor
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

//        orientation sensor
        mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        // define decimal formatter
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMATTER = new DecimalFormat("#.000", symbols);


//        speech to text
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id");
        editText = findViewById(R.id.editText);
        findViewById(R.id.button).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        editText.setHint("Anda bisa melihat inputan disini");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        editText.setText("");
                        editText.setHint("Mendengar...");
                        break;
                }
                return false;
            }
        });

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                //displaying the first match
                if (matches != null){
                    editText.setText(matches.get(0));
                    if(matches.get(0).equalsIgnoreCase(confirmNumber) || matches.get(0).equalsIgnoreCase(confirmNumberText)){
                        clearNotification();
                        finish();
                    }
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        randSelectStatus = new Random().nextInt(4) + 1;
//        randSelectStatus = 4;
        if(randSelectStatus == 1){
            section_shake.setVisibility(View.VISIBLE);
        }else if(randSelectStatus == 2){
            setDirection();
            section_direction.setVisibility(View.VISIBLE);
        }else if(randSelectStatus == 3){
            section_light.setVisibility(View.VISIBLE);
        }else if(randSelectStatus == 4){
            setNumber();
            section_voice.setVisibility(View.VISIBLE);
        }

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
            if(randSelectStatus == 1){
                //            shake sensor
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                mAccelLast = mAccelCurrent;
                mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
                float delta = mAccelCurrent - mAccelLast;
                mAccel = mAccel * 0.9f + delta;
                if (mAccel > 12) {
                    count++;
                    if(count > 30){
                        clearNotification();
                        finish();
                    }
                    txt_currentShake.setText(String.valueOf(count));
                }
            }else if(randSelectStatus == 2){
            //            orientation sensor
                int azimuth = (int) event.values[0];

                if(azimuth == confirmDirection){
                    clearNotification();
                    finish();
                }
                txt_currentDirection.setText(String.valueOf(azimuth)+"°");
            }else if(randSelectStatus == 3){
                //            light sensor
                mLightQuantity = event.values[0];
                txt_currentIntensityLight.setText(String.valueOf(mLightQuantity));
                if(mLightQuantity >= 150){
                    clearNotification();
                    finish();
                }
            }

        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    protected void onResume() {
        switch (randSelectStatus){
            case 1 :
                //        shake sensor
                Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case 2 :
//                        orientation sensor
                mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                       SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case 3 :
                //        light sensor
                mSensorManager.registerListener(
                        mSensorListener, mLightSensor, SensorManager.SENSOR_DELAY_UI);
                break;
        }




        super.onResume();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }


    public void setDirection(){
        int randDirection = (int) (Math.random() * 3);
        switch (randDirection){
            case 0:
                img_direction.setImageResource(R.drawable.ic_direction_north);
                txt_direction.setText("Arahkan smartphone kamu ke arah utara / 0°");
                confirmDirection = 0;
                break;
            case 1:
                img_direction.setImageResource(R.drawable.ic_direction_east);
                txt_direction.setText("Arahkan smartphone kamu ke arah timur / 90°");
                confirmDirection = 90;
                break;
            case 2:
                img_direction.setImageResource(R.drawable.ic_direction_south);
                txt_direction.setText("Arahkan smartphone kamu ke arah selatan / 180°");
                confirmDirection = 180;
                break;
            case 3:
                img_direction.setImageResource(R.drawable.ic_direction_west);
                txt_direction.setText("Arahkan smartphone kamu ke arah barat / 270°");
                confirmDirection = 270;
                break;
        }
    }

    public void setNumber(){
        int randNumber = (int) (Math.random() * 9);
        switch (randNumber){
            case 0:
                img_number.setImageResource(R.drawable.ic_zero);
                confirmNumber = "0";
                confirmNumberText = "nol";
                break;
            case 1:
                img_number.setImageResource(R.drawable.ic_one);
                confirmNumber = "1";
                confirmNumberText = "satu";
                break;
            case 2:
                img_number.setImageResource(R.drawable.ic_two);
                confirmNumber = "2";
                confirmNumberText = "dua";
                break;
            case 3:
                img_number.setImageResource(R.drawable.ic_three);
                confirmNumber = "3";
                confirmNumberText = "tiga";
                break;
            case 4:
                img_number.setImageResource(R.drawable.ic_four);
                confirmNumber = "4";
                confirmNumberText = "empat";
                break;
            case 5:
                img_number.setImageResource(R.drawable.ic_five);
                confirmNumber = "5";
                confirmNumberText = "lima";
                break;
            case 6:
                img_number.setImageResource(R.drawable.ic_six);
                confirmNumber = "6";
                confirmNumberText = "enam";
                break;
            case 7:
                img_number.setImageResource(R.drawable.ic_seven);
                confirmNumber = "7";
                confirmNumberText = "tujuh";
                break;
            case 8:
                img_number.setImageResource(R.drawable.ic_eight);
                confirmNumber = "8";
                confirmNumberText = "delapan";
                break;
            case 9:
                img_number.setImageResource(R.drawable.ic_nine);
                confirmNumber = "9";
                confirmNumberText = "sembilan";
                break;
        }
    }

}