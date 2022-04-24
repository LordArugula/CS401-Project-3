package com.group1.project3.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.group1.project3.R;
import com.group1.project3.model.Project;
import com.group1.project3.view.validator.CreateProjectFormValidator;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

public class CreateProjectDialogBuilder extends MaterialAlertDialogBuilder {

    private View view_bgColor;
    private EditText input_projectName;
    private int color;

    public CreateProjectDialogBuilder(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    public CreateProjectDialogBuilder setTitle(int titleId) {
        return (CreateProjectDialogBuilder) super.setTitle(titleId);
    }

    @NonNull
    @Override
    public CreateProjectDialogBuilder setTitle(@Nullable CharSequence title) {
        return (CreateProjectDialogBuilder) super.setTitle(title);
    }

    @NonNull
    public CreateProjectDialogBuilder setPositiveButton(@Nullable CharSequence text, @Nullable OnClickListener listener) {
        return (CreateProjectDialogBuilder) super.setPositiveButton(text, ((dialogInterface, i) -> listener.onClick(dialogInterface, i, getProject())));
    }

    @NonNull
    public CreateProjectDialogBuilder setPositiveButton(int textId, @Nullable OnClickListener listener) {
        return (CreateProjectDialogBuilder) super.setPositiveButton(textId, ((dialogInterface, i) -> listener.onClick(dialogInterface, i, getProject())));
    }

    @NonNull
    @Override
    public CreateProjectDialogBuilder setNegativeButton(@Nullable CharSequence text, @Nullable DialogInterface.OnClickListener listener) {
        return (CreateProjectDialogBuilder) super.setNegativeButton(text, listener);
    }

    @NonNull
    @Override
    public CreateProjectDialogBuilder setNegativeButton(int textId, @Nullable DialogInterface.OnClickListener listener) {
        return (CreateProjectDialogBuilder) super.setNegativeButton(textId, listener);
    }

    private Project getProject() {
        String projectName = input_projectName.getText().toString().trim();
        Project project = new Project();
        project.setName(projectName);
        project.setColor(color);
        return project;
    }

    @NonNull
    @Override
    public AlertDialog create() {
        setView(R.layout.dialog_create_project);

        color = getContext().getColor(R.color.dialog_createProject_defaultColor);
        AlertDialog alertDialog = super.create();
        alertDialog.setOnShowListener(dialogInterface -> {

            Button confirmButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

            input_projectName = alertDialog.findViewById(R.id.dialog_createProject_input_projectName);
            input_projectName.addTextChangedListener(new CreateProjectFormValidator(input_projectName, confirmButton));

            Button button_colorPicker = alertDialog.findViewById(R.id.dialog_createProject_button_colorPicker);
            button_colorPicker.setOnClickListener(this::onClickColorPickerButton);
            view_bgColor = alertDialog.findViewById(R.id.dialog_createProject_preview_colorPicker);
        });

        return alertDialog;
    }

    private void onClickColorPickerButton(View view) {

        ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(getContext())
                .setTitle("Project Color")
                .setPositiveButton("Select", (ColorEnvelopeListener) (envelope, fromUser) -> {
                    view_bgColor.setBackgroundColor(envelope.getColor());
                    color = envelope.getColor();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .attachAlphaSlideBar(false)
                .attachBrightnessSlideBar(true);

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialogInterface -> {
            try {
                builder.getColorPickerView().selectByHsvColor(color);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        alertDialog.show();
    }

    public interface OnClickListener {
        void onClick(DialogInterface dialogInterface, int i, Project project);
    }
}
