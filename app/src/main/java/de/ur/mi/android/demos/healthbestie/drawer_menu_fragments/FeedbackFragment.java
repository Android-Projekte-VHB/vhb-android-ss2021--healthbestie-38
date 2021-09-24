package de.ur.mi.android.demos.healthbestie.drawer_menu_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.ur.mi.android.demos.healthbestie.R;
import de.ur.mi.android.demos.healthbestie.User;


public class FeedbackFragment extends Fragment {

    TextView rateCount;
    EditText feedback;
    Button submit;
    RatingBar ratingBar;
    float rateValue;

    public FeedbackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_feedback, container, false);

        rateCount = (TextView) v.findViewById(R.id.rate_count);
        feedback = (EditText) v.findViewById(R.id.feedback);
        ratingBar = (RatingBar) v.findViewById(R.id.rating_bar);
        submit = (Button) v.findViewById(R.id.btn_submit);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateValue = ratingBar.getRating();
                if (rateValue > 0 && rateValue <= 1) {
                    rateCount.setText("Terrible " + rateValue + "/5");
                } else if (rateValue > 1 && rateValue <= 2) {
                    rateCount.setText("Bad " + rateValue + "/5");
                }  else if (rateValue > 2 && rateValue <= 3) {
                    rateCount.setText("Okay " + rateValue + "/5");
                }  else if (rateValue > 3 && rateValue <= 4) {
                    rateCount.setText("Good " + rateValue + "/5");
                }  else if (rateValue > 4 && rateValue <= 5) {
                    rateCount.setText("Perfect! " + rateValue + "/5");
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User userFeedback = new User(FirebaseAuth.getInstance().getCurrentUser().getEmail(), String.valueOf(rateValue), feedback.getText().toString());
                DatabaseReference reference = FirebaseDatabase.getInstance("https://health-bestie-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Feedbacks").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.setValue(userFeedback);
                ratingBar.setRating(0);
                rateCount.setText("");
                feedback.setText("");

            }
        });

        return v;
    }
}