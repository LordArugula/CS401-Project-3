package com.group1.project3.view.validator;

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
    private User user;

    public ChangeProfileFormValidator(EditText usernameText, EditText firstNameText, EditText lastNameText, EditText emailText, Button submitButton) {
        this.usernameText = usernameText;
        this.firstNameText = firstNameText;
        this.lastNameText = lastNameText;
        this.emailText = emailText;
        this.submitButton = submitButton;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
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
    }

    private boolean hasChanges(String username, String firstName, String lastName, String email) {
        return !(user.getUsername().equals(username) && user.getFirstName().equals(firstName)
                && user.getLastName().equals(lastName) && user.getEmail().equals(email));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
