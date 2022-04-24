package com.group1.project3.view.validator;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class CreateProjectFormValidator implements TextWatcher {

    private EditText input_projectName;
    private Button button_submit;

    public CreateProjectFormValidator(EditText projectName, Button submitButton) {
        input_projectName = projectName;
        button_submit = submitButton;
        button_submit.setEnabled(!getProjectName().isEmpty());
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // unused
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        boolean error = validateProjectName();

        button_submit.setEnabled(error);
    }

    private boolean validateProjectName() {
        boolean error = false;
        String projectName = getProjectName();
        if (projectName.isEmpty()) {
            error = true;
        }
        return !error;
    }

    @NonNull
    private String getProjectName() {
        return input_projectName.getText().toString().trim();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // unused
    }
}
