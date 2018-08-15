package com.kucingapes.simplequicknote.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.kucingapes.simplequicknote.Activity.ListNotes;
import com.kucingapes.simplequicknote.R;

public class NotifReceiver extends BroadcastReceiver {

    public static String CONTENT = "content";
    @Override
    public void onReceive(Context context, Intent intent) {
        String content = intent.getStringExtra(CONTENT);
        setupNotification(context, content);
    }

    public static void setupNotification(Context context, String content) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "notify_001");
        Intent ii = new Intent(context,ListNotes.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setContentTitle("Reminder !!");
        mBuilder.setSmallIcon(R.drawable.icon_anydpi);
        mBuilder.setContentText(content);
        mBuilder.setStyle(bigText);
        mBuilder.setPriority(Notification.DEFAULT_ALL);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }

        if (mNotificationManager != null) {
            mNotificationManager.notify(200, mBuilder.build());
        }
    }
}
