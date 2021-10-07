package de.ur.mi.android.demos.healthbestie.dashboard.sleep_calculator_function;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import de.ur.mi.android.demos.healthbestie.R;

public class SleepCalculator extends AppCompatActivity {

    public static final String OPTION_LABEL = "OPTION LABEL";
    public static final String RESULT_DESCRIPTION1 = "RESULT DESCRIPTION 1";
    public static final String RESULT_DESCRIPTION2 = "RESULT DESCRIPTION 2";
    public static final String WAKE_UP_TICKED = "WAKE UP OPTION CHOSE";
    private RadioButton wakeUpAt, goToSleepAt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_sleep_calculator);
        wakeUpAt = findViewById(R.id.wakeUpOption);
        goToSleepAt = findViewById(R.id.sleepTimeOption);
        Button button = findViewById(R.id.nextButtonSleepCalculator);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResultButtonClicked();
            }
        });
    }

    private void onResultButtonClicked() {
        Intent intent = new Intent(this, SleepCalculator2ndScreen.class);
        if (wakeUpAt.isChecked()){
            intent.putExtra(OPTION_LABEL, "I want to wake up at:");
            intent.putExtra(RESULT_DESCRIPTION1, "To wake up at ");
            intent.putExtra(RESULT_DESCRIPTION2, " you should sleep at: ");
            intent.putExtra(WAKE_UP_TICKED, "true");
        }
        else if (goToSleepAt.isChecked()){
            intent.putExtra(OPTION_LABEL, "I plan to go to sleep at:");
            intent.putExtra(RESULT_DESCRIPTION1, "To sleep at ");
            intent.putExtra(RESULT_DESCRIPTION2, ", you should wake up at: ");
            intent.putExtra(WAKE_UP_TICKED, "false");
        }
        startActivity(intent);
    }
}