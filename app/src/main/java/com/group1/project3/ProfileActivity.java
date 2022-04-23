package com.group1.project3;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.group1.project3.model.User;
import com.group1.project3.repository.FirestoreUserRepository;
import com.group1.project3.repository.UserRepository;
import com.group1.project3.util.FirebaseUtil;

public class ProfileActivity extends AppCompatActivity {

    private EditText input_username;
    private EditText text_profilePicUri;
    private EditText input_firstName;
    private EditText input_lastName;
    private EditText input_emailAddress;
    private EditText text_oldPassword;
    private EditText text_newPassword;
    private EditText text_confirmPassword;

    private UserRepository userRepository;
    private User user;

    private ChangeProfileWatcher profileWatcher;
    private Button button_updateProfile;
    private Button button_updatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        input_username = findViewById(R.id.profile_input_username);
        text_profilePicUri = findViewById(R.id.profile_input_profilePicUri);
        input_firstName = findViewById(R.id.profile_input_firstName);
        input_lastName = findViewById(R.id.profile_input_lastName);
        input_emailAddress = findViewById(R.id.profile_input_email);
        text_oldPassword = findViewById(R.id.profile_input_oldPassword);
        text_newPassword = findViewById(R.id.profile_input_newPassword);
        text_confirmPassword = findViewById(R.id.profile_input_confirmPassword);

        button_updateProfile = findViewById(R.id.profile_button_updateProfile);
        button_updateProfile.setOnClickListener(this::onClickUpdateProfileButton);

        Button button_revertProfile = findViewById(R.id.profile_button_revertProfile);
        button_revertProfile.setOnClickListener(this::onClickRevertProfileButton);

        button_updatePassword = findViewById(R.id.profile_button_updatePassword);
        button_updatePassword.setOnClickListener(this::onClickUpdatePasswordButton);

        profileWatcher = new ChangeProfileWatcher(input_username, input_firstName, input_lastName, input_emailAddress, button_updateProfile);
        input_username.addTextChangedListener(profileWatcher);
        input_firstName.addTextChangedListener(profileWatcher);
        input_lastName.addTextChangedListener(profileWatcher);
        input_emailAddress.addTextChangedListener(profileWatcher);

        TextWatcher passwordWatcher = new ChangePasswordWatcher(text_oldPassword, text_newPassword, text_confirmPassword, button_updatePassword);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
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
        FirebaseUser firebaseUser = FirebaseUtil.getAuth().getCurrentUser();

        assert firebaseUser != null;

        String email = input_emailAddress.getText().toString().trim();
        String username = input_username.getText().toString().trim();
        String profilePic = text_profilePicUri.getText().toString().trim();
        String first = input_firstName.getText().toString().trim();
        String last = input_lastName.getText().toString().trim();

        Task<Void> updateEmailTask = updateAuthEmail(firebaseUser, email);
        Task<Void> updateUsernameTask = updateAuthProfile(firebaseUser, username, Uri.parse(profilePic));

        user.setEmail(email);
        user.setUsername(username);
        user.setFirstName(first);
        user.setLastName(last);
        Task<Void> updateUserTask = userRepository.updateUser(user);

        Tasks.whenAll(updateEmailTask, updateUsernameTask, updateUserTask)
                .addOnFailureListener(exception -> Log.e("Profile", exception.getMessage()))
                .addOnSuccessListener((unused) -> Log.d("Profile", "Updated user profile"));
    }

    /**
     * Updates the user's username and profile picture.
     *
     * @param user          the Firebase user.
     * @param username      the username.
     * @param profilePicUri the profile picture {@link Uri}.
     * @return a {@link Task<Void>} representing the user change request.
     */
    private Task<Void> updateAuthProfile(FirebaseUser user, String username, Uri profilePicUri) {
        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(profilePicUri)
                .build();
        return user.updateProfile(changeRequest);
    }

    /**
     * Updates the user's email address.
     *
     * @param user  the Firebase user.
     * @param email the email address.
     * @return a {@link Task<Void>} representing the email change request.
     */
    private Task<Void> updateAuthEmail(FirebaseUser user, String email) {
        return user.updateEmail(email);
    }
}
