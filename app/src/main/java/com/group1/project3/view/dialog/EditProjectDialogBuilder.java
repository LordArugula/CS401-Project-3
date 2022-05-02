package com.group1.project3.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.group1.project3.R;
import com.group1.project3.adapter.TagItemAdapter;
import com.group1.project3.model.Project;
import com.group1.project3.model.Tag;
import com.group1.project3.view.validator.CreateProjectFormValidator;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

/**
 * The EditProjectDialog builder.
 */
public class EditProjectDialogBuilder extends MaterialAlertDialogBuilder {

    /**
     * The background color view.
     */
    private View view_bgColor;
    /**
     * The project name EditText.
     */
    private EditText input_projectName;
    /**
     * The background color.
     */
    private int color;

    /**
     * The project.
     */
    private Project project;

    /**
     * Creates the builder.
     *
     * @param context the context.
     */
    public EditProjectDialogBuilder(@NonNull Context context) {
        super(context);
    }

    /**
     * Sets the title of the dialog.
     *
     * @param title the title.
     * @return the builder.
     */
    @NonNull
    @Override
    public EditProjectDialogBuilder setTitle(@Nullable CharSequence title) {
        return (EditProjectDialogBuilder) super.setTitle(title);
    }

    /**
     * Sets the positive button text and listener.
     *
     * @param text     the text.
     * @param listener the listener.
     * @return the builder.
     */
    @NonNull
    public EditProjectDialogBuilder setPositiveButton(@Nullable CharSequence text, @NonNull OnClickListener listener) {
        return (EditProjectDialogBuilder) super.setPositiveButton(text, ((dialogInterface, i) -> {
            String projectName = input_projectName.getText().toString().trim();
            project.setName(projectName);
            project.setColor(color);
            listener.onClick(dialogInterface, i, project);
        }));
    }

    /**
     * Sets the negative button text and listener.
     *
     * @param text     the text.
     * @param listener the listener.
     * @return the builder.
     */
    @NonNull
    @Override
    public EditProjectDialogBuilder setNegativeButton(@Nullable CharSequence text, @Nullable DialogInterface.OnClickListener listener) {
        return (EditProjectDialogBuilder) super.setNegativeButton(text, listener);
    }

    /**
     * Whether to show the delete button or not.
     */
    private boolean useDeleteButton = false;
    /**
     * The onDeleteButton click listener.
     */
    private OnClickListener onDeleteProjectListener;

    /**
     * Sets the delete button listener.
     *
     * @param enabled
     * @param onClickListener the listener.
     * @return the builder.
     */
    public EditProjectDialogBuilder setDeleteButton(boolean enabled, OnClickListener onClickListener) {
        useDeleteButton = enabled;
        this.onDeleteProjectListener = onClickListener;
        return this;
    }

    /**
     * Creates the dialog.
     *
     * @return the dialog.
     */
    @NonNull
    @Override
    public AlertDialog create() {
        setView(R.layout.dialog_edit_project);

        color = getContext().getColor(R.color.project_defaultColor);
        AlertDialog alertDialog = super.create();
        alertDialog.setOnShowListener(dialogInterface -> {
            Button confirmButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            input_projectName = alertDialog.findViewById(R.id.dialog_editProject_input_projectName);
            input_projectName.setText(project.getName());
            input_projectName.addTextChangedListener(new CreateProjectFormValidator(input_projectName, confirmButton));

            Button button_colorPicker = alertDialog.findViewById(R.id.dialog_editProject_button_colorPicker);
            button_colorPicker.setOnClickListener(this::onClickColorPickerButton);
            view_bgColor = alertDialog.findViewById(R.id.dialog_editProject_preview_colorPicker);
            view_bgColor.setBackgroundColor(project.getColor());

            ImageButton button_addTag = alertDialog.findViewById(R.id.dialog_editProject_button_addTag);
            button_addTag.setOnClickListener(view -> {
                EditTagDialogBuilder builder = new EditTagDialogBuilder(getContext())
                        .setView(R.layout.dialog_edit_tag)
                        .setDeleteButton(false, null)
                        .setTitle("Create Tag")
                        .setTag(new Tag("", getContext().getColor(R.color.tag_defaultColor)))
                        .setNegativeButton("Cancel", ((dialogInterface1, i) -> dialogInterface1.dismiss()))
                        .setPositiveButton("Save", ((dialogInterface1, i, tag) -> {
                            project.addTag(tag);
                        }));
                builder.show();
            });

            Button button_delete = alertDialog.findViewById(R.id.dialog_editProject_button_delete);
            if (useDeleteButton) {
                button_delete.setVisibility(View.VISIBLE);
                button_delete.setOnClickListener(view -> {
                    onDeleteProjectListener.onClick(dialogInterface, 0, project);
                });
            } else {
                button_delete.setVisibility(View.INVISIBLE);
            }

            RecyclerView recyclerView_tags = alertDialog.findViewById(R.id.dialog_editProject_recyclerView_tags);
            TagItemAdapter tagItemAdapter = new TagItemAdapter(project);
            recyclerView_tags.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView_tags.setAdapter(tagItemAdapter);
        });

        return alertDialog;
    }

    /**
     * Opens the color picker dialog.
     *
     * @param view the color picker button.
     */
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
                builder.getColorPickerView().selectByHsvColor(project.getColor());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        alertDialog.show();
    }

    /**
     * Sets the project to edit.
     *
     * @param project the project.
     * @return the builder.
     */
    public EditProjectDialogBuilder setProject(Project project) {
        this.project = project;
        return this;
    }

    /**
     * The onClickListener interface.
     */
    public interface OnClickListener {
        /**
         * The onClick callback.
         *
         * @param dialogInterface the dialog interface.
         * @param i               the button index.
         * @param project         the project.
         */
        void onClick(DialogInterface dialogInterface, int i, Project project);
    }
}
