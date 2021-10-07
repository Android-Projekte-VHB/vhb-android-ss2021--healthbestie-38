package de.ur.mi.android.demos.healthbestie.dashboard.sleep_calculator_function;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.ur.mi.android.demos.healthbestie.R;


public class SleepCalculator3rdScreen extends AppCompatActivity {

    private TextView timeResultLabel;
    private TextView nineHrsSleep, sevenHrsSleep, sixHrsSleep, fourHrsSleep;
    private RadioButton wakeUpAt, goToSleepAt;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        showResults();
    }

    private void initUI(){
        setContentView(R.layout.activity_sleep_calculator3rd_screen);
        timeResultLabel = findViewById(R.id.timeResultLabel);
        nineHrsSleep = findViewById(R.id.nine_hrs_sleep_time_result);
        sevenHrsSleep = findViewById(R.id.seven_hrs_sleep_time_result);
        sixHrsSleep = findViewById(R.id.six_hrs_sleep_time_result);
        fourHrsSleep = findViewById(R.id.four_hrs_sleep_time_result);
        wakeUpAt = findViewById(R.id.wakeUpOption);
        goToSleepAt = findViewById(R.id.sleepTimeOption);
    }

    private void showResults(){
        TimeCountHelper helper = new TimeCountHelper();
        Bundle extra = getIntent().getExtras();

        timeResultLabel.setText(extra.getString(SleepCalculator2ndScreen.RESULT_DESCRIPTION1)
                + twoDigitNumberFormat(extra.getInt(SleepCalculator2ndScreen.HOUR))
                +":" + twoDigitNumberFormat(extra.getInt(SleepCalculator2ndScreen.MINUTE))
                + "" + extra.getString(SleepCalculator2ndScreen.RESULT_DESCRIPTION2));

        if(getIntent().getStringExtra(SleepCalculator2ndScreen.WAKE_UP_OPTION_CHOSE).equals("true")) {
            helper.setWakeUpHour(extra.getInt(SleepCalculator2ndScreen.HOUR));
            helper.setWakeUpMin(extra.getInt(SleepCalculator2ndScreen.MINUTE));

            nineHrsSleep.setText(twoDigitNumberFormat(helper.getHr9hrsBeforeWakeUp()) + ":" + twoDigitNumberFormat(helper.getMin9hrsBeforeWakeUp()));
            sevenHrsSleep.setText(twoDigitNumberFormat(helper.getHr7HrsBeforeWakeUp()) + ":" + twoDigitNumberFormat(helper.getMin7hrsBeforeWakeUp()));
            sixHrsSleep.setText(twoDigitNumberFormat(helper.getHr6HrsBeforeWakeUp()) + ":" + twoDigitNumberFormat(helper.getMin6hrsBeforeWakeUp()));
            fourHrsSleep.setText(twoDigitNumberFormat(helper.getHr4HrsBeforeWakeUp()) + ":" + twoDigitNumberFormat(helper.getMin4hrsBeforeWakeUp()));
        }
        if(getIntent().getStringExtra(SleepCalculator2ndScreen.WAKE_UP_OPTION_CHOSE).equals("false")) {
            helper.setSleepHour(extra.getInt(SleepCalculator2ndScreen.HOUR));
            helper.setSleepMin(extra.getInt(SleepCalculator2ndScreen.MINUTE));
            nineHrsSleep.setText(twoDigitNumberFormat(helper.getHr9hrsAfterGoToBed()) + ":" + twoDigitNumberFormat(helper.getMin9hrsAfterGoToBed()));
            sevenHrsSleep.setText(twoDigitNumberFormat(helper.getHr7hrsAfterGoToBed()) + ":" + twoDigitNumberFormat(helper.getMin7hrsAfterGoToBed()));
            sixHrsSleep.setText(twoDigitNumberFormat(helper.getHr6hrsAfterGoToBed()) + ":" + twoDigitNumberFormat(helper.getMin6hrsAfterGoToBed()));
            fourHrsSleep.setText(twoDigitNumberFormat(helper.getHr4hrsAfterGoToBed()) + ":" + twoDigitNumberFormat(helper.getMin4hrsAfterGoToBed()));
        }

    }

    private String twoDigitNumberFormat(int num){
        String numFormat;
        if (num < 10){
            numFormat = "0" + String.valueOf(num);
        } else {
            numFormat = "" + String.valueOf(num);
        }
        return numFormat;
    }

}
