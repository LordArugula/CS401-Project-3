package com.group1.project3.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.group1.project3.R;
import com.group1.project3.model.Pipeline;

/**
 * The EditPipelineDialog builder.
 */
public class EditPipelineDialogBuilder extends MaterialAlertDialogBuilder {

    /**
     * The pipeline name EditText.
     */
    private EditText input_pipelineName;
    /**
     * The pipeline to edit.
     */
    private Pipeline pipeline;

    /**
     * Sets the pipeline.
     *
     * @param pipeline the pipeline.
     * @return the builder.
     */
    public EditPipelineDialogBuilder setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
        return this;
    }

    /**
     * The OnClickListener interface.
     */
    public interface OnClickListener {
        /**
         * The onClick callback.
         *
         * @param dialogInterface the dialog interface.
         * @param i               the button index.
         * @param name            the name of the pipeline.
         */
        void onClick(DialogInterface dialogInterface, int i, String name);
    }

    /**
     * Sets the positive button text and listener.
     *
     * @param text     the text.
     * @param listener the listener.
     * @return the builder.
     */
    @NonNull
    public EditPipelineDialogBuilder setPositiveButton(CharSequence text, @Nullable OnClickListener listener) {
        return (EditPipelineDialogBuilder) super.setPositiveButton(text, (dialogInterface, i) -> {
            String name = input_pipelineName.getText().toString().trim();
            listener.onClick(dialogInterface, i, name);
        });
    }

    /**
     * Sets the view for the dialog.
     *
     * @param layoutResId the layout id.
     * @return the builder.
     */
    @NonNull
    @Override
    public EditPipelineDialogBuilder setView(int layoutResId) {
        return (EditPipelineDialogBuilder) super.setView(layoutResId);
    }

    /**
     * Sets the title of the dialog.
     *
     * @param title the title.
     * @return the builder.
     */
    @NonNull
    @Override
    public EditPipelineDialogBuilder setTitle(@Nullable CharSequence title) {
        return (EditPipelineDialogBuilder) super.setTitle(title);
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
    public EditPipelineDialogBuilder setNegativeButton(@Nullable CharSequence text, @Nullable DialogInterface.OnClickListener listener) {
        return (EditPipelineDialogBuilder) super.setNegativeButton(text, listener);
    }

    /**
     * Creates the dialog builder.
     *
     * @param context the context.
     */
    public EditPipelineDialogBuilder(@NonNull Context context) {
        super(context);
    }

    /**
     * Creates the dialog.
     *
     * @return the dialog.
     */
    @NonNull
    @Override
    public AlertDialog create() {
        AlertDialog alertDialog = super.create();
        alertDialog.setOnShowListener(dialogInterface -> {
            input_pipelineName = alertDialog.findViewById(R.id.dialog_editPipeline_name);
            input_pipelineName.setText(pipeline.getName());
        });

        return alertDialog;
    }
}
