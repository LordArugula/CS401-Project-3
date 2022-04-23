package com.group1.project3.view.validator;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

public class LoginFormValidator implements TextWatcher {

    private final EditText emailText;
    private final EditText passwordText;
    private final Button submitButton;

    public LoginFormValidator(EditText emailText, EditText passwordText, Button submitButton) {
        this.emailText = emailText;
        this.passwordText = passwordText;
        this.submitButton = submitButton;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // unused
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        boolean error = false;

        String email = emailText.getText().toString().trim();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error = true;
        }

        String password = passwordText.getText().toString();
        if (password.isEmpty()) {
            error = true;
        }

        submitButton.setEnabled(!error);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // unused
    }
}
