package com.group1.project3;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class ChangePasswordWatcher implements TextWatcher {

    private final EditText text_oldPassword;
    private final EditText text_newPassword;
    private final EditText text_confirmPassword;

    private final Button submitButton;

    public ChangePasswordWatcher(EditText oldPassword, EditText newPassword, EditText confirmNewPassword, Button submitButton) {

        this.text_oldPassword = oldPassword;
        this.text_newPassword = newPassword;
        this.text_confirmPassword = confirmNewPassword;
        this.submitButton = submitButton;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        boolean error = text_oldPassword.getText().toString().isEmpty();

        String newPassword = text_newPassword.getText().toString();
        if (newPassword.isEmpty()) {
            error = true;
        }

        String confirmPassword = text_confirmPassword.getText().toString();
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
