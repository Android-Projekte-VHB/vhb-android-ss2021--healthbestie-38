package de.ur.mi.android.demos.healthbestie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Register extends AppCompatActivity {

    TextView backToLogin;
    EditText usernameRegister, emailRegister, passwordRegister;
    Button registerButton;

    FirebaseAuth mAuth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        usernameRegister = findViewById(R.id.username_register);
        emailRegister = findViewById(R.id.email_register);
        passwordRegister = findViewById(R.id.pw_register);
        registerButton = findViewById(R.id.register_button);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameRegister.getText().toString();
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
                    register(username, email, password);
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

    private void register(final String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            User user = new User(username, "", email, "");

                            DatabaseReference reference = FirebaseDatabase.getInstance("https://health-bestie-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users").child(firebaseUser.getUid());
                            reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        firebaseUser.sendEmailVerification();
                                        new SweetAlertDialog(Register.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Message")
                                                .setContentText("Successfully registered. \n Please verify your email")
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