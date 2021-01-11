package com.id.ac.stiki.doleno.isalarm.adapter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.id.ac.stiki.doleno.isalarm.R;
import com.id.ac.stiki.doleno.isalarm.database.AlarmModel;
import com.id.ac.stiki.doleno.isalarm.repository.AlarmRepository;
import com.id.ac.stiki.doleno.isalarm.service.AlarmService;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
    private List<AlarmModel> alarmModels;
    private AlarmRepository alarmRepository;
    private AlarmService alarmService;
    private String titleMinutes, titleHours;
    private Application application;

    public AlarmAdapter(List<AlarmModel> alarmModels, Application application) {
        this.alarmModels = alarmModels;
        this.alarmRepository = new AlarmRepository(application);
        this.alarmService = new AlarmService();
        this.application = application;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(alarmModels.get(position).minutes < 10){
            titleMinutes = "0"+alarmModels.get(position).minutes;
        }else{
            titleMinutes = String.valueOf(alarmModels.get(position).minutes);
        }

        if(alarmModels.get(position).hours < 10){
            titleHours = "0"+alarmModels.get(position).hours;
        }else{
            titleHours = String.valueOf(alarmModels.get(position).hours);
        }

        holder.txtTitle.setText(alarmModels.get(position).title);
        holder.txtAlarm.setText(titleHours+":"+titleMinutes);

        if(alarmModels.get(position).isRepeat && alarmModels.get(position).isDaily){
            holder.txtStatus.setText("Daily");
        }else if(!alarmModels.get(position).isRepeat && !alarmModels.get(position).isDaily){
            holder.txtStatus.setText("Once");
        }else if(alarmModels.get(position).isRepeat && !alarmModels.get(position).isDaily){
            Log.e("Size: ", String.valueOf(alarmRepository.getGroupAlarm(alarmModels.get(position).group).size()));
            String detStatus = "";
            for (AlarmModel model: alarmRepository.getGroupAlarm(alarmModels.get(position).group)
                 ) {
                    if(model.date == 1){
                        detStatus += "Ming ";
                    }else if(model.date == 2){
                        detStatus += "Sen ";
                    }else if(model.date == 3){
                        detStatus += "Sel ";
                    }else if(model.date == 4){
                        detStatus += "Rab ";
                    }else if(model.date == 5){
                        detStatus += "Kam ";
                    }else if(model.date == 6){
                        detStatus += "Jum ";
                    }else if(model.date == 7){
                        detStatus += "Sab ";
                    }
                holder.txtStatus.setText(detStatus);
            }
        }

        holder.btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alarmModels.get(position).isRepeat && !alarmModels.get(position).isDaily){
                    for (AlarmModel model: alarmRepository.getGroupAlarm(alarmModels.get(position).group)
                         ) {
                        alarmRepository.updateAlarm(model.id, false);
                        alarmService.stopAlarm(holder.itemView.getContext(), model);
                    }
                }else{
                    alarmRepository.updateAlarm(alarmModels.get(position).id, false);
                    alarmService.stopAlarm(holder.itemView.getContext(), alarmModels.get(position));
                }
            }
        });

        holder.btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alarmModels.get(position).isRepeat && !alarmModels.get(position).isDaily){
                    for (AlarmModel model: alarmRepository.getGroupAlarm(alarmModels.get(position).group)
                         ) {
                        alarmRepository.updateAlarm(model.id, true);
                        alarmService.createAlarm(holder.itemView.getContext(), model);
                    }
                }else{
                    alarmRepository.updateAlarm(alarmModels.get(position).id, true);
                    alarmService.createAlarm(holder.itemView.getContext(), alarmModels.get(position));
                }

            }
        });


        if(alarmModels.get(position).isActive){
            holder.btnOff.setVisibility(View.GONE);
            holder.btnOn.setVisibility(View.VISIBLE);
            holder.txtTitle.setTextColor(application.getResources().getColor(R.color.colorWhite));
            holder.txtAlarm.setTextColor(application.getResources().getColor(R.color.colorAccent));
            holder.txtStatus.setTextColor(application.getResources().getColor(R.color.colorWhite));
        }else{
            holder.btnOn.setVisibility(View.GONE);
            holder.btnOff.setVisibility(View.VISIBLE);
            holder.txtTitle.setTextColor(application.getResources().getColor(R.color.colorSecondary));
            holder.txtAlarm.setTextColor(application.getResources().getColor(R.color.colorSecondary));
            holder.txtStatus.setTextColor(application.getResources().getColor(R.color.colorSecondary));
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Hapus Alarm")
                        .setMessage("Apakah anda yakin ingin menghapus alarm?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(alarmModels.get(position).isRepeat && !alarmModels.get(position).isDaily){
                                    for (AlarmModel model: alarmRepository.getGroupAlarm(alarmModels.get(position).group)
                                    ) {
                                        alarmService.stopAlarm(holder.itemView.getContext(), model);
                                        alarmRepository.deletAlarm(model);
                                    }
                                }else{
                                    alarmService.stopAlarm(holder.itemView.getContext(), alarmModels.get(position));
                                    alarmRepository.deletAlarm(alarmModels.get(position));
                                }

                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAlarm, txtStatus, txtTitle;
//        Switch switchStatus;
        Button btnOff, btnOn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtAlarm = itemView.findViewById(R.id.txt_alarm);
            txtStatus = itemView.findViewById(R.id.txt_status);
//            switchStatus = itemView.findViewById(R.id.switch_status);
            btnOff = itemView.findViewById(R.id.btn_off);
            btnOn = itemView.findViewById(R.id.btn_on);
        }
    }
}
