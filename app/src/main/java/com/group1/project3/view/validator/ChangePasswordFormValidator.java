package com.group1.project3.view.validator;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class ChangePasswordFormValidator implements TextWatcher {

    private final EditText oldPasswordText;
    private final EditText newPasswordText;
    private final EditText confirmPasswordText;

    private final Button submitButton;

    public ChangePasswordFormValidator(EditText oldPasswordText, EditText newPasswordText, EditText confirmPasswordText, Button submitButton) {

        this.oldPasswordText = oldPasswordText;
        this.newPasswordText = newPasswordText;
        this.confirmPasswordText = confirmPasswordText;
        this.submitButton = submitButton;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        boolean error = oldPasswordText.getText().toString().isEmpty();

        String newPassword = newPasswordText.getText().toString();
        if (newPassword.isEmpty()) {
            error = true;
        }

        String confirmPassword = confirmPasswordText.getText().toString();
        if (confirmPassword.isEmpty()) {
            error = true;
        }

        if (!newPassword.equals(confirmPassword)) {
            error = true;
        }

        submitButton.setEnabled(!error);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
