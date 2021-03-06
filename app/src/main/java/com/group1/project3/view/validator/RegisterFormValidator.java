package com.group1.project3.view.validator;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.group1.project3.view.RegisterActivity;

/**
 * Validates changes to the form in the {@link RegisterActivity ).
 */
public class RegisterFormValidator implements TextWatcher {
    /**
     * The first name text field.
     */
    private final EditText firstNameText;

    /**
     * The last name text field.
     */
    private final EditText lastNameText;

    /**
     * The username text field.
     */
    private final EditText usernameText;

    /**
     * The email address text field.
     */
    private final EditText emailText;

    /**
     * The password text field.
     */
    private final EditText passwordText;

    /**
     * The confirm password text field.
     */
    private final EditText confirmPasswordText;

    /**
     * The submit button.
     */
    private final Button submitButton;

    /**
     * @param firstNameText       The text field for the user's first name.
     * @param lastNameText        The text field for the user's last name.
     * @param usernameText        The text field for the user's username.
     * @param emailText           The text field for the user's email address.
     * @param passwordText        The text field for the password.
     * @param confirmPasswordText The text field to confirm the password.
     * @param submitButton        The button to submit the form.
     */
    public RegisterFormValidator(EditText firstNameText,
                                 EditText lastNameText,
                                 EditText usernameText,
                                 EditText emailText,
                                 EditText passwordText,
                                 EditText confirmPasswordText,
                                 Button submitButton) {
        this.firstNameText = firstNameText;
        this.lastNameText = lastNameText;
        this.usernameText = usernameText;
        this.emailText = emailText;
        this.passwordText = passwordText;
        this.confirmPasswordText = confirmPasswordText;
        this.submitButton = submitButton;
    }

    /**
     * Validates the register form on change.
     *
     * @param s      The text as a sequence of characters. Unused.
     * @param start  The starting index of the change within {@code s}. Unused.
     * @param before The length of the old text. Unused.
     * @param count  The length of the new text. Unused.
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        submitButton.setEnabled(validate());
    }

    /**
     * Validates the register form.
     *
     * @return true if the form is valid and false otherwise.
     */
    private boolean validate() {
        return validateName()
                && validateUsername()
                && validateEmail()
                && validatePassword();
    }

    /**
     * Validates the name fields.
     *
     * @return True if the name fields are not empty.
     */
    private boolean validateName() {
        String firstName = firstNameText.getText().toString().trim();
        String lastName = lastNameText.getText().toString().trim();
        return !firstName.isEmpty() && !lastName.isEmpty();
    }

    /**
     * Validates the username field.
     *
     * @return True if the username is not empty.
     */
    private boolean validateUsername() {
        String username = usernameText.getText().toString().trim();
        return !username.isEmpty();
    }

    /**
     * Validates the email address.
     *
     * @return True if the email is in the valid format.
     */
    private boolean validateEmail() {
        String email = emailText.getText().toString().trim();
        return !email.isEmpty()
                && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Validates the password fields.
     *
     * @return True if the password fields are not empty
     * and match each other.
     */
    private boolean validatePassword() {
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();
        return !password.isEmpty()
                && !confirmPassword.isEmpty()
                && password.equals(confirmPassword);
    }

    /**
     * Called before the text is changed. Unused.
     *
     * @param s     The text as a sequence of characters.
     * @param start The starting index of the change within {@code s}.
     * @param count The length of the characters of the previous text.
     * @param after The length of the characters to replace the previous characters with.
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /**
     * Called after the text has changed. Unused.
     *
     * @param s The interface for editable text content.
     */
    @Override
    public void afterTextChanged(Editable s) {
    }
}
