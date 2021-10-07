package de.ur.mi.android.demos.healthbestie.dashboard.calories_calculator_function;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import de.ur.mi.android.demos.healthbestie.R;
import de.ur.mi.android.demos.healthbestie.User;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


public class CaloriesCalculator extends AppCompatActivity {

    public static final String HEIGHT = "HEIGHT";
    public static final String WEIGHT = "WEIGHT";
    public static final String FACTOR = "FACTOR";

    private EditText heightInput, weightInput;
    private RadioButton active_lvl_1, active_lvl_2, active_lvl_3, active_lvl_4, active_lvl_5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories_calculator);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_calories_calculator);
        heightInput = (EditText) findViewById(R.id.heightInput);
        weightInput = (EditText) findViewById(R.id.weightInput);
        active_lvl_1 = (RadioButton) findViewById(R.id.lvl1);
        active_lvl_2 = (RadioButton) findViewById(R.id.lvl2);
        active_lvl_3 = (RadioButton) findViewById(R.id.lvl3);
        active_lvl_4 = (RadioButton) findViewById(R.id.lvl4);
        active_lvl_5 = (RadioButton) findViewById(R.id.lvl5);
        Button calculateButton = (Button) findViewById(R.id.calculateCaloriesButton);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResultButtonClicked();
            }
        });
    }

    private void onResultButtonClicked() {

        if (heightInput.getText().toString().equals("") || weightInput.getText().toString().equals("")) {
            Toast.makeText(CaloriesCalculator.this, "All fields required", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, CaloriesCalculatorResult.class);
            intent.putExtra(HEIGHT, Double.parseDouble(heightInput.getText().toString()));
            intent.putExtra(WEIGHT, Double.parseDouble(weightInput.getText().toString()));
            if(active_lvl_1.isChecked()){
                intent.putExtra(FACTOR, 1.2);
            }
            else if(active_lvl_2.isChecked()){
                intent.putExtra(FACTOR, 1.375);
            }
            else if(active_lvl_3.isChecked()){
                intent.putExtra(FACTOR,1.55);
            }
            else if(active_lvl_4.isChecked()){
                intent.putExtra(FACTOR,1.725);
            }
            else if(active_lvl_5.isChecked()){
                intent.putExtra(FACTOR,1.9);
            }

            startActivity(intent);
        }


    }

}