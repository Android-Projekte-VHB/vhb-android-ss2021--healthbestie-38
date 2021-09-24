package de.ur.mi.android.demos.healthbestie.drawer_menu_fragments.profile;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.ur.mi.android.demos.healthbestie.DashBoard;
import de.ur.mi.android.demos.healthbestie.Login;
import de.ur.mi.android.demos.healthbestie.R;



public class ProfileFragment extends Fragment implements View.OnClickListener {

    TextView usernameProfile, fullNameProfile, emailProfile, phoneProfile;

    EditText oldPw, newPw, confirmPw;

    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;
    DatabaseReference reference;

    TextView editProfile, changePassword, deleteAccount ;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameProfile =  (TextView) v.findViewById(R.id.username_profile);
        emailProfile = (TextView) v.findViewById(R.id.email_profile);
        fullNameProfile = (TextView) v.findViewById(R.id.full_name_profile);
        phoneProfile = (TextView) v.findViewById(R.id.phone_number_profile);



        editProfile = (TextView) v.findViewById(R.id.edit_profile);
        changePassword = (TextView) v.findViewById(R.id.change_pw);
        deleteAccount = (TextView) v.findViewById(R.id.delete_acct);
        editProfile.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        deleteAccount.setOnClickListener(this);




        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://health-bestie-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = mFirebaseDatabase.getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("username").getValue(String.class);
                String email = dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("email").getValue(String.class);
                String fullName = dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("fullName").getValue(String.class);
                String phone = dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("phone").getValue(String.class);

                usernameProfile.setText(username);
                emailProfile.setText(email);
                fullNameProfile.setText(fullName);
                phoneProfile.setText(phone);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(v.getContext(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_profile:
                Bundle bundle = new Bundle();
                bundle.putString("username", usernameProfile.getText().toString());
                bundle.putString("fullName", fullNameProfile.getText().toString());
                bundle.putString("email", emailProfile.getText().toString());
                bundle.putString("phone", phoneProfile.getText().toString());

                EditProfileFragment etFragment = new EditProfileFragment();
                etFragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.fragment_profile, etFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                break;

            case R.id.change_pw:
                if (AccessToken.getCurrentAccessToken() != null || GoogleSignIn.getLastSignedInAccount(getActivity()) != null) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("If you are using Facebook or Google to log into this app, please go back to your Facebook or Google account page to change your password.")
                            .show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View view = getLayoutInflater().inflate(R.layout.change_password, null);
                    builder.setView(view);
                    oldPw = (EditText) view.findViewById(R.id.current_pw);
                    newPw = (EditText) view.findViewById(R.id.new_pw);
                    confirmPw = (EditText) view.findViewById(R.id.confirm_pw);

                    builder.setTitle("Change password")
                            .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            })
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String oldPassword = oldPw.getText().toString();
                                    String newPassword = newPw.getText().toString();
                                    String confirmPassword = confirmPw.getText().toString();

                                    if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                                        Toast.makeText(getContext(), "All the fields are required", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else if (newPassword.length() <= 5) {
                                        Toast.makeText(getActivity(), "Password should contain more than 5 characters", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else if (oldPassword.equals(newPassword)) {
                                        Toast.makeText(getActivity(), " New password cannot be same as your current password.Please choose a different password", Toast.LENGTH_SHORT).show();
                                        return;
                                    } else if (!newPassword.equals(confirmPassword)) {
                                        Toast.makeText(getActivity(), "Confirm password does not match", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    updatePassword(oldPassword, newPassword);
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

                break;


            case R.id.delete_acct:
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Delete Confirmation")
                        .setContentText("Are you sure you want to delete your account permanently?")
                        .setConfirmText("Delete my account")
                        .setCancelText("Cancel")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    revokeAccess();
                                                } else {
                                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                        })
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();
                break;

        }
    }

    private void updatePassword(String oldPassword, String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), oldPassword);

        user.reauthenticate(credential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        user.updatePassword(newPassword)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getActivity(), "Password changed successfully. \n Please log in again", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent backToLogin = new Intent(getActivity(), Login.class);
                                        backToLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(backToLogin);                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Old password is incorrect", Toast.LENGTH_SHORT).show();

                    }
                });

    }






    private void revokeAccess() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity() , gso);
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent backToLogin = new Intent(getActivity(), Login.class);
                        backToLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getActivity().startActivity(backToLogin);
                        Toast.makeText(getActivity(), "Account deleted", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}