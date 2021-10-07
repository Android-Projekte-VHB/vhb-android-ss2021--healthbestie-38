package de.ur.mi.android.demos.healthbestie.drawer_menu_fragments.profile;


import android.app.Activity;
import android.content.Context;
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import de.ur.mi.android.demos.healthbestie.R;
import de.ur.mi.android.demos.healthbestie.User;


public class EditProfileFragment extends Fragment implements View.OnClickListener {

    private CircularImageView etImg;
    private ImageView uploadImageBtn;
    private String imgUri, gender;
    private EditText etUsername, etAge;
    private RadioButton etMale, etFemale;
    private TextView etEmail;
    private Button save, cancel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);


        etImg = (CircularImageView) v.findViewById(R.id.edit_profile_img);
        uploadImageBtn = (ImageView) v.findViewById(R.id.upload_img);
        etUsername = (EditText) v.findViewById(R.id.edit_username);
        etAge = (EditText) v.findViewById(R.id.edit_age);
        etEmail = (TextView) v.findViewById(R.id.edit_email);

        etMale = (RadioButton) v.findViewById(R.id.edit_male);
        etFemale = (RadioButton) v.findViewById(R.id.edit_female);

        Bundle bundle = this.getArguments();
        String username = bundle.getString("username");
        String age = bundle.getString("age");
        String email = bundle.getString("email");

        if (bundle.getString("gender").equals("Male")) {
            etMale.setChecked(true);
        } else {
            etFemale.setChecked(true);
        }
        imgUri = bundle.getString("imgUri");

        etUsername.setText(username);
        etAge.setText(age);
        etEmail.setText(email);
        Glide.with(getContext()).load(imgUri).into(etImg);


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
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult.launch(intent);
            break;


            case R.id.btn_save:

                String username = etUsername.getText().toString();
                String age = etAge.getText().toString();
                String email = etEmail.getText().toString();
                String gender = "";
                if (etMale.isChecked()) {
                    gender = etMale.getText().toString();
                } else {
                    gender = etFemale.getText().toString();
                }

                updateData(username, age ,email, gender, imgUri);
                Toast.makeText(getActivity(), "User profile successfully updated", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_profile, new ProfileFragment()).addToBackStack(null).commit();

                break;

            case R.id.btn_cancel:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_profile, new ProfileFragment()).addToBackStack(null).commit();
                break;
        }
    }


    private void updateData(String username, String age, String email, String gender, String img) {
        DatabaseReference reference =  FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        User etUser = new User(username,age, email,gender);
        reference.setValue(etUser);
        reference.child("profileImg").setValue(img);


    }


    ActivityResultLauncher<Intent> startActivityForResult  = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data.getData() != null) {
                            Uri profileUri = data.getData();
                            imgUri = profileUri.toString();
                            etImg.setImageURI(profileUri);
                            FirebaseStorage firebaseStorage  = FirebaseStorage.getInstance();
                            StorageReference storageReference = firebaseStorage.getReference().child("Profile Images").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            storageReference.putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getContext(), "Photo uploaded", Toast.LENGTH_SHORT).show();
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profileImg")
                                                    .setValue(uri.toString());
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            });
}