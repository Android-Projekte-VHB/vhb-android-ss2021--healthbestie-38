package de.ur.mi.android.demos.healthbestie;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.common.SignInButton;

import com.google.android.gms.common.api.ApiException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.ur.mi.android.demos.healthbestie.drawer_menu_fragments.NavigationDrawer;


public class Login extends AppCompatActivity {

    private static final String FB_TAG = Login.class.getSimpleName();
    private CallbackManager callbackManager;
    private GraphRequest request;
    private String name, email;


    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String GG_TAG = "SignInActivity";


    private EditText emailLogin, passwordLogin;
    private Button loginButton;
    private CheckBox rememberMe;
    private TextView forgetPassword;

    private TextView moveToRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_HealthBestie);
        setContentView(R.layout.activity_login);

        checkNightModeState();
        checkLoginStatus();
        appLogin();
        keepLoggedIn();
        resetPassword();
        facebookSignIn();
        googleSignIn();
        moveToRegisterForm();
    }

    private void checkNightModeState() {
        SharedPreferences preferences = getSharedPreferences("nightModeState", Context.MODE_PRIVATE);
        if (preferences.getBoolean("nightModeState", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


    private void checkLoginStatus() {
        // Check if the user is already signed in
        if (AccessToken.getCurrentAccessToken() != null || GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(new Intent(Login.this, NavigationDrawer.class));
            finish();
        }
    }

    /** Remember Me Login Function **/
    private void keepLoggedIn() {
        rememberMe = findViewById(R.id.remember_me);
        SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = sharedPreferences.getString("remember", "");
        if (checkbox.equals("checked")) {
            startActivity(new Intent(Login.this, NavigationDrawer.class));
            finish();

        }
        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("remember", "checked");
                    editor.apply();
                }
                else if (!compoundButton.isChecked()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("remember", "unchecked");
                    editor.apply();
                }
            }
        });
    }

    /** Reset password Function **/
    private void resetPassword() {
        forgetPassword = findViewById(R.id.reset_pw);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                View view = getLayoutInflater().inflate(R.layout.reset_password, null, false);
                builder.setView(view);
                EditText resetPw = (EditText) view.findViewById(R.id.reset_pw_by_email);
                builder.setTitle("Forgot your password?")
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Reset Password", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String resetPassword = resetPw.getText().toString();
                                if (resetPassword.isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "Please enter your email address", Toast.LENGTH_SHORT).show();

                                } else {
                                    mAuth = FirebaseAuth.getInstance();
                                    mAuth.sendPasswordResetEmail(resetPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "A password reset email has been sent to your email address.Please check your email box", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }


    /** Use App Account to sign in **/

    private void appLogin() {
        mAuth = FirebaseAuth.getInstance();

        emailLogin = findViewById(R.id.email_login);
        passwordLogin = findViewById(R.id.pw_login);
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailLogin.getText().toString();
                String password = passwordLogin.getText().toString();
                
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Email and Password are required", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        new SweetAlertDialog(Login.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Login Successful")
                                                .setContentText("Welcome back!")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        startActivity(new Intent(Login.this, NavigationDrawer.class));
                                                        finish();
                                                    }
                                                })
                                                .show();
                                    } else {
                                        Toast.makeText(Login.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }

    /** Use Facebook Account to sign in **/

    private void facebookSignIn() {
        callbackManager = CallbackManager.Factory.create();

        LoginButton fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        fbLoginButton.setPermissions(Arrays.asList("email", "public_profile"));
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(FB_TAG, "Facebook login success");
                handleFacebookAccessToken(loginResult.getAccessToken());
                startActivity(new Intent(Login.this, NavigationDrawer.class));
                finish();
            }

            @Override
            public void onCancel() {
                Log.d(FB_TAG, "facebook:onCancel");
                Toast.makeText(Login.this, "Login Facebook cancelled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(FB_TAG, "Facebook login error");
                Toast.makeText(Login.this, "Login Facebook error.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(FB_TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(FB_TAG, "signInWithCredential:success");
                            request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        try {
                                            name = object.getString("name");
                                            email = object.getString("email");
                                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                            User user = new User(name, "",email, "");
                                            DatabaseReference reference = FirebaseDatabase.getInstance("https://health-bestie-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users").child(firebaseUser.getUid());
                                            reference.setValue(user);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "name,email");
                                request.setParameters(parameters);
                                request.executeAsync();
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(FB_TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    /** Use Google Account to sign in **/

    private void googleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult.launch(signInIntent);
            }
        });

    }


    ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleSignInResult(task);
                    }
                }
            });


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            if (acct != null) {
                String idToken = acct.getIdToken();
                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();

                AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(GG_TAG, "signInWithCredential:success");
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    User user = new User(personName, "", personEmail, "");
                                    DatabaseReference reference = FirebaseDatabase.getInstance("https://health-bestie-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(firebaseUser.getUid());
                                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent i = new Intent(Login.this, NavigationDrawer.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(i);
                                                finish();

                                            }
                                        }
                                    });


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(GG_TAG, "signInWithCredential:failure", task.getException());

                                }
                            }
                        });


            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(GG_TAG, e.getMessage());

        }
    }



    /** Create a new account **/
    private void moveToRegisterForm() {
        moveToRegister =findViewById(R.id.to_register);
        moveToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }





}