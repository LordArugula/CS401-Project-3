package com.group1.project3.view.validator;

import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.group1.project3.model.User;

public class ChangeProfileFormValidator implements TextWatcher {

    private final EditText usernameText;
    private final EditText firstNameText;
    private final EditText lastNameText;
    private final EditText emailText;
    private final Button submitButton;
    private final Button revertButton;
    private Uri profilePicUri;
    private User user;

    public ChangeProfileFormValidator(EditText usernameText, EditText firstNameText, EditText lastNameText, EditText emailText, Uri profilePicUri, Button submitButton, Button revertButton) {
        this.usernameText = usernameText;
        this.firstNameText = firstNameText;
        this.lastNameText = lastNameText;
        this.emailText = emailText;
        this.profilePicUri = profilePicUri;
        this.submitButton = submitButton;
        this.revertButton = revertButton;
    }

    public void setUser(User user) {
        this.user = user;
        profilePicUri = user.getProfilePicUri() == null ? null : Uri.parse(user.getProfilePicUri());
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // unused
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        validateForm();
    }

    public void validateForm() {
        boolean canUpdateProfile = true;
        String username = usernameText.getText().toString().trim();
        if (username.isEmpty()) {
            canUpdateProfile = false;
        }

        String firstName = firstNameText.getText().toString().trim();
        if (firstName.isEmpty()) {
            canUpdateProfile = false;
        }

        String lastName = lastNameText.getText().toString().trim();
        if (lastName.isEmpty()) {
            canUpdateProfile = false;
        }

        String email = emailText.getText().toString().trim();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            canUpdateProfile = false;
        }

        if (!hasChanges(username, firstName, lastName, email)) {
            canUpdateProfile = false;
        }

        submitButton.setEnabled(canUpdateProfile);
        revertButton.setEnabled(canUpdateProfile);
    }

    private boolean hasChanges(String username, String firstName, String lastName, String email) {
        return user != null && (!user.getUsername().equals(username)
                || !user.getFirstName().equals(firstName)
                || !user.getLastName().equals(lastName)
                || !user.getEmail().equals(email)
                || !areProfileUrisEqual());
    }

    private boolean areProfileUrisEqual() {
        String currentProfilePicUriString = user.getProfilePicUri();
        if (currentProfilePicUriString == null) {
            return profilePicUri == null || profilePicUri.equals(Uri.EMPTY);
        }

        Uri currentProfilePicUri = Uri.parse(currentProfilePicUriString);
        return currentProfilePicUri.equals(profilePicUri);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // unused
    }

    public void setImageUri(Uri imageUri) {
        this.profilePicUri = imageUri;
    }
}
