package com.group1.project3.view.validator;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

/**
 * Validates the Project form.
 */
public class CreateProjectFormValidator implements TextWatcher {

    /**
     * The project name EditText.
     */
    private EditText input_projectName;
    /**
     * The submit button.
     */
    private Button button_submit;

    /**
     * Creates the validator.
     *
     * @param projectName  the project name EditText.
     * @param submitButton the submit button.
     */
    public CreateProjectFormValidator(EditText projectName, Button submitButton) {
        input_projectName = projectName;
        button_submit = submitButton;
        button_submit.setEnabled(!getProjectName().isEmpty());
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
        boolean error = validateProjectName();

        button_submit.setEnabled(error);
    }

    /**
     * Validates the project name
     *
     * @return true if the name is not empty.
     */
    private boolean validateProjectName() {
        boolean error = false;
        String projectName = getProjectName();
        if (projectName.isEmpty()) {
            error = true;
        }
        return !error;
    }

    /**
     * Returns the name of the project.
     *
     * @return the name of the project.
     */
    @NonNull
    private String getProjectName() {
        return input_projectName.getText().toString().trim();
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
}
