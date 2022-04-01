package com.group1.project3;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class RegisterTextWatcher implements TextWatcher {
    private final EditText firstnameText;
    private final EditText lastNameText;
    private final EditText emailText;
    private final EditText passwordText;
    private final EditText confirmPasswordText;
    private final Button submitButton;

    public RegisterTextWatcher(EditText firstnameText, EditText lastName, EditText emailText, EditText passwordText, EditText confirmPasswordText, Button submitButton) {
        this.firstnameText = firstnameText;
        this.lastNameText = lastName;
        this.emailText = emailText;
        this.passwordText = passwordText;
        this.confirmPasswordText = confirmPasswordText;
        this.submitButton = submitButton;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        boolean error = false;

        String firstname = firstnameText.getText().toString();
        if (firstname.isEmpty()) {
            error = true;
        }

        String lastName = lastNameText.getText().toString();
        if (lastName.isEmpty()) {
            error = true;
        }

        String email = emailText.getText().toString();
        if (email.isEmpty()) {
            error = true;
        }

        String password = passwordText.getText().toString();
        if (password.isEmpty()) {
            error = true;
        }

        String confirmPassword = confirmPasswordText.getText().toString();
        if (confirmPassword.isEmpty()) {
            error = true;
        }

        if (!password.equals(confirmPassword)) {
            error = true;
        }

        submitButton.setEnabled(!error);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
