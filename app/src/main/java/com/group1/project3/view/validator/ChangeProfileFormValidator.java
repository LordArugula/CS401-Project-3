package com.group1.project3.view.validator;

import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.group1.project3.model.User;

/**
 * Validates the profile form in the ProfileActivity.
 */
public class ChangeProfileFormValidator implements TextWatcher {

    /**
     * The username EditText.
     */
    private final EditText usernameText;

    /**
     * The first name EditText.
     */
    private final EditText firstNameText;

    /**
     * The last name EditText.
     */
    private final EditText lastNameText;

    /**
     * The email address EditText.
     */
    private final EditText emailText;

    /**
     * The submit button.
     */
    private final Button submitButton;

    /**
     * The revert button
     */
    private final Button revertButton;

    /**
     * The profile picture Uri.
     */
    private Uri profilePicUri;

    /**
     * The user.
     */
    private User user;

    /**
     * Creates the validator.
     *
     * @param usernameText  the username EditText.
     * @param firstNameText the first name EditText.
     * @param lastNameText  the last name EditText.
     * @param emailText     the email EditText.
     * @param profilePicUri the profile picture Uri.
     * @param submitButton  the submit button.
     * @param revertButton  the revert button.
     */
    public ChangeProfileFormValidator(EditText usernameText, EditText firstNameText, EditText lastNameText, EditText emailText, Uri profilePicUri, Button submitButton, Button revertButton) {
        this.usernameText = usernameText;
        this.firstNameText = firstNameText;
        this.lastNameText = lastNameText;
        this.emailText = emailText;
        this.profilePicUri = profilePicUri;
        this.submitButton = submitButton;
        this.revertButton = revertButton;
    }

    /**
     * Sets the user to validate against.
     *
     * @param user the user.
     */
    public void setUser(User user) {
        this.user = user;
        profilePicUri = user.getProfilePicUri() == null ? null : Uri.parse(user.getProfilePicUri());
    }

    /**
     * Unused
     *
     * @param charSequence
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // unused
    }

    /**
     * Validates the form.
     *
     * @param charSequence The text as a sequence of characters. Unused.
     * @param i            The starting index of the change within {@code s}. Unused.
     * @param i1           The length of the old text. Unused.
     * @param i2           The length of the new text. Unused.
     */
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

    /**
     * Checks if the form has changes.
     *
     * @param username  the username.
     * @param firstName the first name.
     * @param lastName  the last name.
     * @param email     the email address.
     * @return true if the form has changes.
     */
    private boolean hasChanges(String username, String firstName, String lastName, String email) {
        return user != null && (!user.getUsername().equals(username)
                || !user.getFirstName().equals(firstName)
                || !user.getLastName().equals(lastName)
                || !user.getEmail().equals(email)
                || !areProfileUrisEqual());
    }

    /**
     * Checks if the profile Uris are the same.
     *
     * @return true if the profile Uris are the same.
     */
    private boolean areProfileUrisEqual() {
        String currentProfilePicUriString = user.getProfilePicUri();
        if (currentProfilePicUriString == null) {
            return profilePicUri == null || profilePicUri.equals(Uri.EMPTY);
        }

        Uri currentProfilePicUri = Uri.parse(currentProfilePicUriString);
        return currentProfilePicUri.equals(profilePicUri);
    }

    /**
     * Unused
     *
     * @param editable
     */
    @Override
    public void afterTextChanged(Editable editable) {
        // unused
    }

    /**
     * Sets the profile image Uri.
     *
     * @param imageUri the image Uri.
     */
    public void setImageUri(Uri imageUri) {
        this.profilePicUri = imageUri;
    }
}
