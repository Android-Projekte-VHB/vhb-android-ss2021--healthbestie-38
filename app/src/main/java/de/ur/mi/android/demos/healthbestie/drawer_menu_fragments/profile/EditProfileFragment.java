package de.ur.mi.android.demos.healthbestie.drawer_menu_fragments.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;

import de.ur.mi.android.demos.healthbestie.R;
import de.ur.mi.android.demos.healthbestie.User;


public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private CircularImageView etImg;
    private ImageView uploadImageBtn;
    private EditText etUsername, etFullName, etPhone;
    private TextView etEmail;
    Button save, cancel;


    public EditProfileFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);


        etImg = (CircularImageView) v.findViewById(R.id.edit_profile_img);
        uploadImageBtn = (ImageView) v.findViewById(R.id.upload_img);
        etUsername = (EditText) v.findViewById(R.id.edit_username);
        etFullName = (EditText) v.findViewById(R.id.edit_full_name);
        etEmail = (TextView) v.findViewById(R.id.edit_email);
        etPhone = (EditText) v.findViewById(R.id.edit_phone);

        Bundle bundle = this.getArguments();
        String username = bundle.getString("username");
        String fullName = bundle.getString("fullName");
        String email = bundle.getString("email");
        String phone = bundle.getString("phone");

        etUsername.setText(username);
        etFullName.setText(fullName);
        etEmail.setText(email);
        etPhone.setText(phone);

        save = (Button) v.findViewById(R.id.btn_save);
        cancel = (Button) v.findViewById(R.id.btn_cancel);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        uploadImageBtn.setOnClickListener(this);


        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_img:


            case R.id.btn_save:
                String username = etUsername.getText().toString();
                String fullName = etFullName.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();

                updateData(username, fullName,email, phone);
                Toast.makeText(getActivity(), "User profile successfully updated", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_profile, new ProfileFragment()).addToBackStack(null).commit();
            case R.id.btn_cancel:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_profile, new ProfileFragment()).addToBackStack(null).commit();
        }
    }



    private void updateData(String username, String fullName, String email, String phone) {
        DatabaseReference reference =  FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        User etUser = new User(username,fullName, email,phone);
        reference.setValue(etUser);

    }
}