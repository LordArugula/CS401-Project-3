package com.group1.project3;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.group1.project3.model.User;

public class ChangeProfileWatcher implements TextWatcher {

    private final EditText text_username;
    private final EditText text_first;
    private final EditText text_last;
    private final EditText text_email;
    private final Button submitButton;
    private User user;

    public ChangeProfileWatcher(EditText username, EditText first, EditText last, EditText email, Button submitButton) {
        this.text_username = username;
        this.text_first = first;
        this.text_last = last;
        this.text_email = email;
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
        String username = text_username.getText().toString().trim();
        if (username.isEmpty()) {
            canUpdateProfile = false;
        }

        String firstName = text_first.getText().toString().trim();
        if (firstName.isEmpty()) {
            canUpdateProfile = false;
        }

        String lastName = text_last.getText().toString().trim();
        if (lastName.isEmpty()) {
            canUpdateProfile = false;
        }

        String email = text_email.getText().toString().trim();
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
