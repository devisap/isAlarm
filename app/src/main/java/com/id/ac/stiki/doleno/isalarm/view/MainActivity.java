package com.id.ac.stiki.doleno.isalarm.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.id.ac.stiki.doleno.isalarm.R;
import com.id.ac.stiki.doleno.isalarm.adapter.AlarmAdapter;
import com.id.ac.stiki.doleno.isalarm.database.AlarmModel;
import com.id.ac.stiki.doleno.isalarm.view.AddAlarm;
import com.id.ac.stiki.doleno.isalarm.viewmodel.MainActivityViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel mainActivityViewModel;
    private RecyclerView recyclerView;

    private ImageButton btn_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        recyclerView = findViewById(R.id.recycler_view);

        String[] PERMISSIONS = {
                Manifest.permission.RECEIVE_BOOT_COMPLETED,
                Manifest.permission.RECORD_AUDIO,
        };

        if (!hasPermissions(PERMISSIONS)) {
            //kalau gaada bakal minta request permission (popup)
            ActivityCompat.requestPermissions(this, PERMISSIONS, 0);
        } else {
            process();
        }

        btn_info = findViewById(R.id.btn_info);
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);
            }
        });

    }

    public void process() {
        mainActivityViewModel.getListData().observe(this, new Observer<List<AlarmModel>>() {
            @Override
            public void onChanged(List<AlarmModel> alarmModels) {
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(new AlarmAdapter(alarmModels, getApplication()));
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddAlarm.class);
                startActivity(i);
            }
        });
    }

    private boolean hasPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED
                        || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Izin diperlukan untuk menggunakan aplikasi", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    process();
                }
            }
        }
    }
}