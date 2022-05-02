package com.group1.project3.view.validator;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

/**
 * Validates the Login form.
 */
public class LoginFormValidator implements TextWatcher {

    /**
     * The email EditText.
     */
    private final EditText emailText;
    /**
     * The password EditText.
     */
    private final EditText passwordText;
    /**
     * The submit button.
     */
    private final Button submitButton;

    /**
     * Create the validator.
     * @param emailText the email EditText.
     * @param passwordText the password EditText.
     * @param submitButton the submit button.
     */
    public LoginFormValidator(EditText emailText, EditText passwordText, Button submitButton) {
        this.emailText = emailText;
        this.passwordText = passwordText;
        this.submitButton = submitButton;
    }

    /**
     * Unused.
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

    /**
     * Unused.
     * @param editable
     */
    @Override
    public void afterTextChanged(Editable editable) {
        // unused
    }
}
