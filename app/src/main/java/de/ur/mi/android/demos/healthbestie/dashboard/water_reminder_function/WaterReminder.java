package de.ur.mi.android.demos.healthbestie.dashboard.water_reminder_function;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.ur.mi.android.demos.healthbestie.R;
import de.ur.mi.android.demos.healthbestie.dashboard.service.ReminderBroadcast;

public class WaterReminder extends AppCompatActivity implements  View.OnClickListener{


    private EditText waterLimit, remindFrequency;
    private Button confirmButton;
    private Button CancelButton;

    private static final String CHANNEL_ID = "water-notification";
    private static final String CHANNEL_NAME = "water-nofification";
    private static final String CHANNEL_DESCRIPTION = "Description here";

    private Boolean cancel = false;

    /**
     * setting up the Alarm Manager
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_reminder);

        confirmButton = findViewById(R.id.waterReminder_Confirm);
        CancelButton = findViewById(R.id.waterReminder_CancelButton);
        remindFrequency = findViewById(R.id.waterReminder_frequency);

        //run only on allowed versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        confirmButton.setOnClickListener(this);
        CancelButton.setOnClickListener(this);

    }

    @Override
    //setting up onClickListeners for the two buttons
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.waterReminder_Confirm:
                displayNotification();
                NavigateSuccessScreen();
                break;

            case R.id.waterReminder_CancelButton:
                finish();
                break;

            default:
                break;

        }
    }

    private void displayNotification(){

        Intent intent = new Intent(this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long timeAtButtonClick = System.currentTimeMillis();
        //just for testing, change in actual product
        long sleepTime = 1000 * 10;
        long interval = Long.parseLong(remindFrequency.getText().toString());
        //repeat as user input, first start after 10 seconds
        alarmManager.set(AlarmManager.RTC_WAKEUP,timeAtButtonClick+sleepTime,pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeAtButtonClick + sleepTime, interval * 60000, pendingIntent);


    }

    private void endNotification() {
        Intent intent = new Intent(this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

    }

    private void NavigateSuccessScreen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reminder Set Successfully!")
                .setMessage("You will be reminded every " + remindFrequency.getText().toString() + " minutes")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(WaterReminder.this, WaterReminderSuccess.class);
                        intent.putExtra("userInput", remindFrequency.getText().toString());

                        SharedPreferences sharedPref = getSharedPreferences("savedFile", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("startedAlarm", remindFrequency.getText().toString());
                        editor.apply();

                        startActivity(intent);
                        finish();
                    }
                })

        ;
        builder.create();
        builder.show();

    }
}