package com.id.ac.stiki.doleno.isalarm.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.id.ac.stiki.doleno.isalarm.R;
import com.id.ac.stiki.doleno.isalarm.database.AlarmModel;
import com.id.ac.stiki.doleno.isalarm.preference.AppPreference;
import com.id.ac.stiki.doleno.isalarm.viewmodel.AddAlarmViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddAlarm extends AppCompatActivity {
    private CheckBox checkBoxRepeat, checkBoxSunday, checkBoxMonday, checkBoxTuesday, checkBoxWednesday, checkBoxThursday, checkBoxFriday, checkBoxSaturday;
    private RadioGroup radioGroup;
    private RadioButton radioButtonDaily, radioButtonCustom;
    private LinearLayout linearLayoutCustom;
    private Button btnSimpan;
    private EditText edtLabel;
    private TimePicker timePicker;
    private List<AlarmModel> alarmDetailModelList;

    private AddAlarmViewModel addAlarmViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        timePicker = (TimePicker)findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        addAlarmViewModel = ViewModelProviders.of(this).get(AddAlarmViewModel.class);

        radioGroup = findViewById(R.id.radioGroup);
        radioButtonDaily = findViewById(R.id.radioButtonDaily);
        radioButtonCustom = findViewById(R.id.radioButtonCustom);
        linearLayoutCustom = findViewById(R.id.linearLayoutCustom);
        checkBoxRepeat = findViewById(R.id.checkBoxRepeat);
        checkBoxSunday = findViewById(R.id.checkBoxSunday);
        checkBoxMonday = findViewById(R.id.checkBoxMonday);
        checkBoxTuesday = findViewById(R.id.checkBoxTuesday);
        checkBoxWednesday = findViewById(R.id.checkBoxWednesday);
        checkBoxThursday = findViewById(R.id.checkBoxThursday);
        checkBoxFriday = findViewById(R.id.checkBoxFriday);
        checkBoxSaturday = findViewById(R.id.checkBoxSaturday);
        btnSimpan = findViewById(R.id.btn_simpan);
        edtLabel = findViewById(R.id.edt_label);
        alarmDetailModelList = new ArrayList<>();

        addAlarmViewModel.getListData().observe(this, new Observer<List<AlarmModel>>() {
            @Override
            public void onChanged(List<AlarmModel> alarmModels) {
                for (AlarmModel model: alarmModels
                     ) {
                    Log.e("id", String.valueOf(model.id));
                    Log.e("code", String.valueOf(model.code));
                    Log.e("title", String.valueOf(model.title));
                    Log.e("group", String.valueOf(model.group));
                    Log.e("hours", String.valueOf(model.hours));
                    Log.e("minutes", String.valueOf(model.minutes));
                    Log.e("isRepeat", String.valueOf(model.isRepeat));
                    Log.e("isDaily", String.valueOf(model.isDaily));
                }
            }
        });


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkBoxRepeat.isChecked()) {
                    addAlarmViewModel.insertAlarm(addAlarmData(false, 0));
                } else {
                    if (!radioButtonDaily.isChecked()) {
                        if (checkBoxSunday.isChecked()) {
                            alarmDetailModelList.add(addAlarmData(radioButtonDaily.isChecked(), Calendar.SUNDAY));
                        }

                        if (checkBoxMonday.isChecked()) {
                            alarmDetailModelList.add(addAlarmData(radioButtonDaily.isChecked(), Calendar.MONDAY));
                        }

                        if (checkBoxTuesday.isChecked()) {
                            alarmDetailModelList.add(addAlarmData(radioButtonDaily.isChecked(), Calendar.TUESDAY));
                        }

                        if (checkBoxWednesday.isChecked()) {
                            alarmDetailModelList.add(addAlarmData(radioButtonDaily.isChecked(), Calendar.WEDNESDAY));
                        }

                        if (checkBoxThursday.isChecked()) {
                            alarmDetailModelList.add(addAlarmData(radioButtonDaily.isChecked(), Calendar.THURSDAY));
                        }

                        if (checkBoxFriday.isChecked()) {
                            alarmDetailModelList.add(addAlarmData(radioButtonDaily.isChecked(), Calendar.FRIDAY));
                        }

                        if (checkBoxSaturday.isChecked()) {
                            alarmDetailModelList.add(addAlarmData(radioButtonDaily.isChecked(), Calendar.FRIDAY));
                        }
                        addAlarmViewModel.insertMultipleAlarm(alarmDetailModelList);
                    } else {
                        addAlarmViewModel.insertAlarm(addAlarmData(radioButtonDaily.isChecked(), 0));
                    }
                }
                AppPreference.saveGroup(view.getContext(), AppPreference.getGroup(view.getContext()) + 1);
                finish();
            }
        });


        checkBoxRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioGroup.setVisibility(View.VISIBLE);
                } else {
                    radioGroup.setVisibility(View.GONE);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonDaily:
                        linearLayoutCustom.setVisibility(View.GONE);
                        break;
                    case R.id.radioButtonCustom:
                        linearLayoutCustom.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

    }

    public AlarmModel addAlarmData(boolean isDaily, int date) {
        AlarmModel alarmModel = new AlarmModel();
        alarmModel.code = AppPreference.getCode(this);
        alarmModel.title = edtLabel.getText().toString();
        alarmModel.group = AppPreference.getGroup(this);
        alarmModel.hours = timePicker.getCurrentHour();
        alarmModel.minutes = timePicker.getCurrentMinute();
        alarmModel.isRepeat = checkBoxRepeat.isChecked();
        alarmModel.isDaily = isDaily;
        alarmModel.date = date;
        AppPreference.saveCode(this, (AppPreference.getCode(this) + 1));
        return alarmModel;
    }
}