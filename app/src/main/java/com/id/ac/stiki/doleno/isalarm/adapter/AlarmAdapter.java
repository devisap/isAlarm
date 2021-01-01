package com.id.ac.stiki.doleno.isalarm.adapter;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

    public AlarmAdapter(List<AlarmModel> alarmModels, Application application) {
        this.alarmModels = alarmModels;
        this.alarmRepository = new AlarmRepository(application);
        this.alarmService = new AlarmService();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTitle.setText(alarmModels.get(position).title);
        if(alarmModels.get(position).minutes < 10){
            holder.txtAlarm.setText(alarmModels.get(position).hours+":"+("0"+alarmModels.get(position).minutes));
        }else{
            holder.txtAlarm.setText(alarmModels.get(position).hours+":"+(alarmModels.get(position).minutes));
        }

        if(alarmModels.get(position).isRepeat && alarmModels.get(position).isDaily){
            holder.txtStatus.setText("Daily");
        }else if(!alarmModels.get(position).isRepeat && !alarmModels.get(position).isDaily){
            holder.txtStatus.setText("Once");
        }else if(alarmModels.get(position).isRepeat && !alarmModels.get(position).isDaily){
            holder.txtStatus.setText("Custom");
        }

//        holder.switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                alarmRepository.updateAlarm(alarmModels.get(position));
////                if(compoundButton.isChecked()){
////                    alarmService.createAlarm(holder.itemView.getContext(), alarmModels.get(position));
////                }else{
////                    alarmService.stopAlarm(holder.itemView.getContext(), alarmModels.get(position));
////                }
//            }
//        });

//        if(alarmModels.get(position).isActive){
//            holder.switchStatus.setChecked(true);
//        }else{
//            holder.switchStatus.setChecked(false);
//        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Hapus Alarm")
                        .setMessage("Apakah anda yakin ingin menghapus alarm?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                alarmRepository.deletAlarm(alarmModels.get(position));
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
        Switch switchStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtAlarm = itemView.findViewById(R.id.txt_alarm);
            txtStatus = itemView.findViewById(R.id.txt_status);
            switchStatus = itemView.findViewById(R.id.switch_status);
        }
    }
}
