package de.ur.mi.android.demos.healthbestie.dashboard.sleep_calculator_function;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import de.ur.mi.android.demos.healthbestie.R;

public class SleepCalculator2ndScreen extends AppCompatActivity {

    public static final String WAKE_UP_OPTION_CHOSE = "WAKE UP OPTION CHOSE";
    public static final String RESULT_DESCRIPTION1 = "RESULT DESCRIPTION 1";
    public static final String RESULT_DESCRIPTION2 = "RESULT DESCRIPTION 2";
    public static final String HOUR = "HOUR";
    public static final String MINUTE = "MINUTE";

    private TextView inputTimeLabel;
    private TimePicker timePicked;
    private int hour, minute;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_sleep_calculator2nd_screen);
        inputTimeLabel = findViewById(R.id.inputTimeLabel);
        timePicked = (TimePicker) findViewById(R.id.fragment_timePicker);
        timePicked.setIs24HourView(true);
        Button sleepCalculateBtn = findViewById(R.id.bed_time_calculator_button);

        inputTimeLabel.setText(getIntent().getStringExtra(SleepCalculator.OPTION_LABEL));

        sleepCalculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResultButtonClicked();
            }
        });
    }

    private void onResultButtonClicked() {
        Intent intent = new Intent(this, SleepCalculator3rdScreen.class);

        hour = timePicked.getHour();
        minute = timePicked.getMinute();

        if(getIntent().getStringExtra(SleepCalculator.WAKE_UP_TICKED).equals("true")){
            intent.putExtra(WAKE_UP_OPTION_CHOSE, "true");
        }
        else if(getIntent().getStringExtra(SleepCalculator.WAKE_UP_TICKED).equals("false")){
            intent.putExtra(WAKE_UP_OPTION_CHOSE, "false");
        }
        intent.putExtra(HOUR, hour);
        intent.putExtra(MINUTE, minute);

        intent.putExtra(RESULT_DESCRIPTION1, getIntent().getStringExtra(SleepCalculator.RESULT_DESCRIPTION1));
        intent.putExtra(RESULT_DESCRIPTION2, getIntent().getStringExtra(SleepCalculator.RESULT_DESCRIPTION2));
        startActivity(intent);
    }

}
