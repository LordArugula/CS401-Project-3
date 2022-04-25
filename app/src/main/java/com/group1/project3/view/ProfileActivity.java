package com.group1.project3.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.group1.project3.R;
import com.group1.project3.model.User;
import com.group1.project3.repository.FirestoreUserRepository;
import com.group1.project3.repository.UserRepository;
import com.group1.project3.util.FirebaseUtil;
import com.group1.project3.view.dialog.UploadImageDialogBuilder;
import com.group1.project3.view.validator.ChangePasswordFormValidator;
import com.group1.project3.view.validator.ChangeProfileFormValidator;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private ImageView image_profilePic;
    private EditText input_username;
    private EditText input_firstName;
    private EditText input_lastName;
    private EditText input_emailAddress;
    private EditText text_oldPassword;
    private EditText text_newPassword;
    private EditText text_confirmPassword;

    private Uri profilePicUri;

    private UserRepository userRepository;
    private User user;

    private Button button_updateProfile;
    private Button button_updatePassword;
    private ChangeProfileFormValidator profileWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        input_username = findViewById(R.id.profile_input_username);
        input_firstName = findViewById(R.id.profile_input_firstName);
        input_lastName = findViewById(R.id.profile_input_lastName);
        input_emailAddress = findViewById(R.id.profile_input_email);
        text_oldPassword = findViewById(R.id.profile_input_oldPassword);
        text_newPassword = findViewById(R.id.profile_input_newPassword);
        text_confirmPassword = findViewById(R.id.profile_input_confirmPassword);

        image_profilePic = findViewById(R.id.profile_image_profilePic);
        Button button_updateImage = findViewById(R.id.profile_button_updateImage);
        button_updateImage.setOnClickListener(this::onClickUpdateImageButton);

        button_updateProfile = findViewById(R.id.profile_button_updateProfile);
        button_updateProfile.setOnClickListener(this::onClickUpdateProfileButton);

        Button button_revertProfile = findViewById(R.id.profile_button_revertProfile);
        button_revertProfile.setOnClickListener(this::onClickRevertProfileButton);

        button_updatePassword = findViewById(R.id.profile_button_updatePassword);
        button_updatePassword.setOnClickListener(this::onClickUpdatePasswordButton);

        profileWatcher = new ChangeProfileFormValidator(input_username, input_firstName, input_lastName, input_emailAddress, profilePicUri, button_updateProfile);
        input_username.addTextChangedListener(profileWatcher);
        input_firstName.addTextChangedListener(profileWatcher);
        input_lastName.addTextChangedListener(profileWatcher);
        input_emailAddress.addTextChangedListener(profileWatcher);

        TextWatcher passwordWatcher = new ChangePasswordFormValidator(text_oldPassword, text_newPassword, text_confirmPassword, button_updatePassword);
        text_oldPassword.addTextChangedListener(passwordWatcher);
        text_newPassword.addTextChangedListener(passwordWatcher);
        text_confirmPassword.addTextChangedListener(passwordWatcher);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.profile_title);
        actionBar.setDisplayHomeAsUpEnabled(true);

        userRepository = new FirestoreUserRepository();
        FirebaseUser currentUser = FirebaseUtil.getAuth().getCurrentUser();
        userRepository.getUser(currentUser.getUid())
                .addOnSuccessListener(user -> {
                    profileWatcher.setUser(user);
                    bind(user);
                });
    }

    private void onClickUpdateImageButton(View view) {

        AlertDialog dialogBuilder = new UploadImageDialogBuilder(ProfileActivity.this)
                .setTitle("Upload Image from Url")
                .setView(R.layout.dialog_image_url)
                .setImageUri(profilePicUri)
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setNeutralButton("Remove", (dialogInterface, i) -> {
                    loadImage();
                })
                .setPositiveButton("Confirm", ((dialogInterface, i, imageUri) -> {
                    Picasso.get()
                            .load(imageUri)
                            .resize(64, 64)
                            .config(Bitmap.Config.ARGB_8888)
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .into(image_profilePic);
                    image_profilePic.setClipToOutline(true);

                    profilePicUri = imageUri;
                    profileWatcher.setImageUri(profilePicUri);
                    profileWatcher.validateForm();
                }))
                .setPlaceholderImage(R.drawable.ic_baseline_person_24)
                .show();
    }

    private void loadImage() {
        Picasso.get()
                .load(R.drawable.ic_baseline_person_24)
                .resize(64, 64)
                .config(Bitmap.Config.ARGB_8888)
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(image_profilePic);
        image_profilePic.setClipToOutline(true);

        profilePicUri = null;
        profileWatcher.setImageUri(profilePicUri);
        profileWatcher.validateForm();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(ProfileActivity.this, ProjectMenuActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * OnClick callback for the update profile button.
     * Updates the user profile.
     *
     * @param view the button that was clicked
     */
    private void onClickUpdateProfileButton(View view) {
        updateProfile();
        button_updateProfile.setEnabled(false);
    }

    /**
     * OnClick callback for the revert profile button.
     *
     * @param view the button that was clicked.
     */
    private void onClickRevertProfileButton(View view) {
        bind(user);
    }

    /**
     * OnClick callback for the update password button.
     * Updates the user's password.
     *
     * @param view the button that was clicked
     */
    private void onClickUpdatePasswordButton(View view) {
        updatePassword().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                text_oldPassword.getText().clear();
                text_newPassword.getText().clear();
                text_confirmPassword.getText().clear();
                button_updatePassword.setEnabled(false);
            } else {
                if (task.getException() != null) {
                    Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Error updating password", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    /**
     * Populates the text fields with the user fields.
     *
     * @param user the user.
     */
    private void bind(User user) {
        if (user == null) {
            return;
        }
        this.user = user;
        input_username.setText(user.getUsername());
        input_emailAddress.setText(user.getEmail());
        input_username.setText(user.getUsername());
        input_firstName.setText(user.getFirstName());
        input_lastName.setText((user.getLastName()));
        profilePicUri = user.getProfilePicUri() == null ? Uri.EMPTY : Uri.parse(user.getProfilePicUri());
        Picasso.get().load(profilePicUri)
                .resize(64, 64)
                .config(Bitmap.Config.ARGB_8888)
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(image_profilePic);
        image_profilePic.setClipToOutline(true);
    }

    /**
     * Updates the user's password.
     */
    private Task<Void> updatePassword() {
        String email = user.getEmail();
        String oldPassword = text_oldPassword.getText().toString();
        String newPassword = text_newPassword.getText().toString();

        return FirebaseUtil.reauthenticate(email, oldPassword)
                .onSuccessTask(unused -> FirebaseUtil.updatePassword(newPassword))
                .addOnFailureListener(exception -> Toast.makeText(ProfileActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show());
    }

    /**
     * Updates the user's profile.
     */
    private void updateProfile() {
        String email = input_emailAddress.getText().toString().trim();
        String username = input_username.getText().toString().trim();
        String first = input_firstName.getText().toString().trim();
        String last = input_lastName.getText().toString().trim();

        Task<Void> updateEmailTask = FirebaseUtil.updateEmail(email);
        Task<Void> updateUsernameTask = FirebaseUtil.updateProfile(username, profilePicUri);

        user.setEmail(email);
        user.setUsername(username);
        user.setFirstName(first);
        user.setLastName(last);
        user.setProfilePicUri(profilePicUri == null ? null : profilePicUri.toString());

        Task<Void> updateUserTask = userRepository.updateUser(user);

        Tasks.whenAll(updateEmailTask, updateUsernameTask, updateUserTask)
                .addOnFailureListener(exception -> Log.e("Profile", exception.getMessage()))
                .addOnSuccessListener((unused) -> Log.d("Profile", "Updated user profile"));
    }

}