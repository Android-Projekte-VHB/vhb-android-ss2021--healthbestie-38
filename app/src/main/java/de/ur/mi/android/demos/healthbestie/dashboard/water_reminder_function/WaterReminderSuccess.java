package de.ur.mi.android.demos.healthbestie.dashboard.water_reminder_function;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.ur.mi.android.demos.healthbestie.R;
import de.ur.mi.android.demos.healthbestie.dashboard.service.ReminderBroadcast;

public class WaterReminderSuccess extends AppCompatActivity implements View.OnClickListener {

    TextView display;
    Button cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_reminder_success);

        Intent intent = getIntent();
        display = findViewById(R.id.waterRemindSuccess_Text);
        cancel = findViewById(R.id.waterRemindSuccess_Cancel);

        String varName = intent.getStringExtra("userInput");

        display.setText("You will be reminded for every " + varName+ " minutes");

        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.waterRemindSuccess_Cancel:
                resetAlarm();
        }
    }
    private void resetAlarm() {

        Intent intent = new Intent(this, ReminderBroadcast.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);

        SharedPreferences sharedPref = getSharedPreferences("savedFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("startedAlarm", "");
        editor.apply();

        Intent previousScreen = new Intent(this, WaterReminder.class);
        startActivity(previousScreen);
        finish();
    }
}