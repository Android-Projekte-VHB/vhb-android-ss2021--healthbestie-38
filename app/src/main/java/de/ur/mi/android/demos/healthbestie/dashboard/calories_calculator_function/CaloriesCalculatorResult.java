package de.ur.mi.android.demos.healthbestie.dashboard.calories_calculator_function;

import de.ur.mi.android.demos.healthbestie.R;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CaloriesCalculatorResult extends AppCompatActivity{

    private TextView caloriesToMaintainW, caloriesToLooseW, caloriesToGainW;
    private CaloriesCalculator caloriesCalculatorStartActivity;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        showResults();
    }

    private void initUI() {
        setContentView(R.layout.activity_calories_calculator_result);
        caloriesToMaintainW = findViewById(R.id.calories_needed_result);
        caloriesToLooseW = findViewById(R.id.calories_loose_weight_result);
        caloriesToGainW = findViewById(R.id.calories_gain_weight_result);
    }

    private void showResults() {
        CaloriesFunctionStatsHelper helper = new CaloriesFunctionStatsHelper();
        Bundle extra = getIntent().getExtras();

        helper.setHeight(extra.getDouble(CaloriesCalculator.HEIGHT));
        helper.setWeight(extra.getDouble(CaloriesCalculator.WEIGHT));
        helper.setFactor(extra.getDouble(CaloriesCalculator.FACTOR));

        //set age and gender here
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://health-bestie-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = mFirebaseDatabase.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String age = dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("age").getValue(String.class);
                String gender = dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("gender").getValue(String.class);

                helper.setAge(Integer.parseInt(age));
                helper.setGender(gender);

                caloriesToMaintainW.setText(String.valueOf(Math.round(helper.getCaloriesToMaintain())));
                caloriesToLooseW.setText(String.valueOf(Math.round(helper.getCaloriesToLoose())));
                caloriesToGainW.setText(String.valueOf(Math.round(helper.getCaloriesToGain())));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
