package de.ur.mi.android.demos.healthbestie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Register extends AppCompatActivity {

    private TextView backToLogin;
    private EditText usernameRegister, ageRegister, emailRegister, passwordRegister;
    private RadioButton maleBtn, femaleBtn;
    private Button registerButton;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        usernameRegister = findViewById(R.id.username_register);
        ageRegister = findViewById(R.id.age_register);
        maleBtn = findViewById(R.id.gender_male);
        femaleBtn = findViewById(R.id.gender_female);
        emailRegister = findViewById(R.id.email_register);
        passwordRegister = findViewById(R.id.pw_register);
        registerButton = findViewById(R.id.register_button);



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameRegister.getText().toString();
                String age = ageRegister.getText().toString();
                String male = maleBtn.getText().toString();
                String female = femaleBtn.getText().toString();
                String email = emailRegister.getText().toString();
                String password = passwordRegister.getText().toString();

                if (username.isEmpty()) {
                    Toast.makeText(Register.this, "Please enter username", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()) {
                    Toast.makeText(Register.this, "Please enter your e-mail address", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(Register.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                } else if (!isPasswordValid(password)) {
                    Toast.makeText(Register.this, "Password should contain more than 5 characters", Toast.LENGTH_SHORT).show();
                } else {
                    if (maleBtn.isChecked()) {
                        register(username, age, male, email, password);

                    } else if (femaleBtn.isChecked()) {
                        register(username, age, female, email, password);

                    }
                }
            }
        });


        // move to login form
        backToLogin = findViewById(R.id.to_login);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

    }

    private void register(final String username, String age, String gender, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            User user = new User(username, age, email, gender);

                            DatabaseReference reference = FirebaseDatabase.getInstance("https://health-bestie-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users").child(firebaseUser.getUid());
                            reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        firebaseUser.sendEmailVerification();
                                        new SweetAlertDialog(Register.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Successfully registered")
                                                .setContentText("Please verify your email")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        Intent i = new Intent(Register.this, Login.class);
                                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                })
                                                .show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Failed to register. Please try again",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        } else {
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
    }

    private boolean isPasswordValid (String password) {
        return password.length() >= 5;
    }

}