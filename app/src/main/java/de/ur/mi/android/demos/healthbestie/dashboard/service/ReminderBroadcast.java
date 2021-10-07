package de.ur.mi.android.demos.healthbestie.dashboard.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import de.ur.mi.android.demos.healthbestie.R;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"water-notification")
                .setSmallIcon(R.drawable.ic_baseline_home_24)
                .setContentTitle("Drink yo watah")
                .setContentText("You need to drink your water")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(1, builder.build());
    }
}
