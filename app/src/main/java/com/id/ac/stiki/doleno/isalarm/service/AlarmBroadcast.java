package com.id.ac.stiki.doleno.isalarm.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.id.ac.stiki.doleno.isalarm.R;

public class AlarmBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent();
        i.setClassName("com.id.ac.stiki.doleno.isalarm", "com.id.ac.stiki.doleno.isalarm.view.AlarmActive");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("ID", intent.getIntExtra("ID", 0));
        i.putExtra("TITLE", intent.getStringExtra("TITLE"));
        i.putExtra("HOUR", intent.getIntExtra("HOUR", 0));
        i.putExtra("MINUTE", intent.getIntExtra("MINUTE", 0));
        i.putExtra("REPEAT", intent.getBooleanExtra("REPEAT", false));
        i.putExtra("DAILY", intent.getBooleanExtra("DAILY", false));
        i.putExtra("DATE", intent.getIntExtra("DATE", 0));
        i.putExtra("CODE", intent.getIntExtra("CODE", 0));
        context.startActivity(i);

        showNotification(context, "Title", "Text", i);
    }

    public void showNotification(Context context, String s, String s1, Intent intent) {
        Uri url = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("notificationID", "notification", NotificationManager.IMPORTANCE_HIGH);
            notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                    //.setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(s)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentText(s1)
                    .setSound(url, AudioManager.STREAM_ALARM)
                    .setChannelId("notificationID")
                    .build();
            notificationManager.createNotificationChannel(mChannel);
        } else {
            notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                    //.setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(s)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                    .setContentText(s1)
                    .setSound(url, AudioManager.STREAM_ALARM)
                    .build();
        }
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.flags |= Notification.FLAG_INSISTENT;
        notification.flags|= Notification.DEFAULT_LIGHTS;
        notification.flags|= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(1, notification);
    }
}
