package com.group1.project3.view.validator;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

/**
 * Validates the password form in the ProfileActivity.
 */
public class ChangePasswordFormValidator implements TextWatcher {

    /**
     * The old password EditText.
     */
    private final EditText oldPasswordText;

    /**
     * The new password EditText.
     */
    private final EditText newPasswordText;

    /**
     * The confirm password EditText.
     */
    private final EditText confirmPasswordText;

    /**
     * The submit button.
     */
    private final Button submitButton;

    /**
     * Creates the form validator.
     *
     * @param oldPasswordText     the old password EditText.
     * @param newPasswordText     the new password EditText.
     * @param confirmPasswordText the confirm password EditText.
     * @param submitButton        the submit button.
     */
    public ChangePasswordFormValidator(EditText oldPasswordText, EditText newPasswordText, EditText confirmPasswordText, Button submitButton) {
        this.oldPasswordText = oldPasswordText;
        this.newPasswordText = newPasswordText;
        this.confirmPasswordText = confirmPasswordText;
        this.submitButton = submitButton;
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

    /**
     * Unused
     *
     * @param editable
     */
    @Override
    public void afterTextChanged(Editable editable) {

    }
}
