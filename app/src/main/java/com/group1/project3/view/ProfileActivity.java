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
import androidx.appcompat.app.AppCompatActivity;

import com.group1.project3.R;
import com.group1.project3.controller.UserController;
import com.group1.project3.model.User;
import com.group1.project3.repository.FirestoreUserRepository;
import com.group1.project3.view.dialog.UploadImageDialogBuilder;
import com.group1.project3.view.validator.ChangePasswordFormValidator;
import com.group1.project3.view.validator.ChangeProfileFormValidator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * The user profile activity.
 */
public class ProfileActivity extends AppCompatActivity {

    /**
     * The profile pic ImageView.
     */
    private ImageView image_profilePic;
    /**
     * The username EditText.
     */
    private EditText input_username;
    /**
     * The first name EditText.
     */
    private EditText input_firstName;
    /**
     * The last name EditText.
     */
    private EditText input_lastName;
    /**
     * The email address EditText.
     */
    private EditText input_emailAddress;
    /**
     * The old password EditText.
     */
    private EditText text_oldPassword;
    /**
     * The new password EditText.
     */
    private EditText text_newPassword;
    /**
     * The confirm password EditText.
     */
    private EditText text_confirmPassword;

    /**
     * The profile picture Uri.
     */
    private Uri profilePicUri;

    /**
     * The update password button.
     */
    private Button button_updatePassword;
    /**
     * The profile form validator.
     */
    private ChangeProfileFormValidator profileWatcher;

    /**
     * The user controller.
     */
    private UserController profileController;
    /**
     * The user.
     */
    private User user;

    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState the saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Binds the user profile
        input_username = findViewById(R.id.profile_input_username);
        input_firstName = findViewById(R.id.profile_input_firstName);
        input_lastName = findViewById(R.id.profile_input_lastName);
        input_emailAddress = findViewById(R.id.profile_input_email);

        image_profilePic = findViewById(R.id.profile_image_profilePic);
        Button button_updateImage = findViewById(R.id.profile_button_updateImage);
        button_updateImage.setOnClickListener(this::onClickUpdateImageButton);

        Button button_updateProfile = findViewById(R.id.profile_button_updateProfile);
        button_updateProfile.setOnClickListener(this::onClickUpdateProfileButton);

        Button button_revertProfile = findViewById(R.id.profile_button_revertProfile);
        button_revertProfile.setOnClickListener(this::onClickRevertProfileButton);

        button_updatePassword = findViewById(R.id.profile_button_updatePassword);
        button_updatePassword.setOnClickListener(this::onClickUpdatePasswordButton);

        profileWatcher = new ChangeProfileFormValidator(input_username, input_firstName, input_lastName, input_emailAddress, profilePicUri, button_updateProfile, button_revertProfile);
        input_username.addTextChangedListener(profileWatcher);
        input_firstName.addTextChangedListener(profileWatcher);
        input_lastName.addTextChangedListener(profileWatcher);
        input_emailAddress.addTextChangedListener(profileWatcher);

        // Binds the password validator
        text_oldPassword = findViewById(R.id.profile_input_oldPassword);
        text_newPassword = findViewById(R.id.profile_input_newPassword);
        text_confirmPassword = findViewById(R.id.profile_input_confirmPassword);

        TextWatcher passwordWatcher = new ChangePasswordFormValidator(text_oldPassword, text_newPassword, text_confirmPassword, button_updatePassword);
        text_oldPassword.addTextChangedListener(passwordWatcher);
        text_newPassword.addTextChangedListener(passwordWatcher);
        text_confirmPassword.addTextChangedListener(passwordWatcher);

        // Sets the action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.profile_title);
        actionBar.setDisplayHomeAsUpEnabled(true);

        profileController = new UserController(new FirestoreUserRepository());
        profileController.loadCurrentUser()
                .addOnSuccessListener(user -> {
                    this.user = user;
                    bindUser(user);
                    profileWatcher.setUser(user);
                    profileWatcher.validateForm();
                });
    }

    /**
     * Opens the UploadImageDialog.
     *
     * @param view the button.
     */
    private void onClickUpdateImageButton(View view) {
        new UploadImageDialogBuilder(ProfileActivity.this)
                .setTitle("Upload Image from Url")
                .setView(R.layout.dialog_image_url)
                .setImageUri(profilePicUri)
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setNeutralButton("Remove", (dialogInterface, i) -> loadImage(Uri.EMPTY))
                .setPositiveButton("Confirm", ((dialogInterface, i, imageUri) -> loadImage(imageUri)))
                .setPlaceholderImage(R.drawable.ic_baseline_person_24)
                .show();
    }

    /**
     * Loads the image with the Uri.
     *
     * @param imageUri the image Uri.
     */
    private void loadImage(@NonNull Uri imageUri) {
        Picasso picasso = Picasso.get();

        RequestCreator requestCreator;
        if (imageUri == Uri.EMPTY) {
            requestCreator = picasso.load(R.drawable.ic_baseline_person_24);
        } else {
            requestCreator = picasso.load(imageUri);
        }

        requestCreator.resize(64, 64)
                .config(Bitmap.Config.ARGB_8888)
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(image_profilePic);
        image_profilePic.setClipToOutline(true);

        profilePicUri = imageUri;
        profileWatcher.setImageUri(profilePicUri);
        profileWatcher.validateForm();
    }

    /**
     * The menu options handler.
     * @param item the menu item.
     * @return based on the option selected.
     */
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
        String email = input_emailAddress.getText().toString().trim();
        String username = input_username.getText().toString().trim();
        String first = input_firstName.getText().toString().trim();
        String last = input_lastName.getText().toString().trim();

        profileController.updateProfile(email, username, first, last, profilePicUri)
                .addOnSuccessListener(unused -> {
                    Log.d("Profile", "Updated user profile.");
                    bindUser(user);
                })
                .addOnFailureListener(exception -> {
                    Log.e("Profile", exception.getMessage());
                    Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * OnClick callback for the revert profile button.
     *
     * @param view the button that was clicked.
     */
    private void onClickRevertProfileButton(View view) {
        bindUser(user);
    }

    /**
     * OnClick callback for the update password button.
     * Updates the user's password.
     *
     * @param view the button that was clicked
     */
    private void onClickUpdatePasswordButton(View view) {
        String oldPassword = text_oldPassword.getText().toString();
        String newPassword = text_newPassword.getText().toString();
        profileController.updatePassword(oldPassword, newPassword)
                .addOnSuccessListener(task -> {
                    Log.d("Profile", "Updated password.");
                    text_oldPassword.getText().clear();
                    text_newPassword.getText().clear();
                    text_confirmPassword.getText().clear();
                    button_updatePassword.setEnabled(false);
                })
                .addOnFailureListener(exception -> {
                    Log.e("Profile", exception.getMessage());
                    Toast.makeText(ProfileActivity.this, exception.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                });
    }

    /**
     * Populates the text fields with the user fields.
     *
     * @param user the user.
     */
    private void bindUser(@NonNull User user) {
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
}
