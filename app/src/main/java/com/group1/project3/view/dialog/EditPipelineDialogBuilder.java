package com.group1.project3.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.group1.project3.R;
import com.group1.project3.model.Pipeline;

public class EditPipelineDialogBuilder extends MaterialAlertDialogBuilder {

    private EditText input_pipelineName;
    private Pipeline pipeline;

    public EditPipelineDialogBuilder setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
        return this;
    }

    public interface OnClickListener {
        void onClick(DialogInterface dialogInterface, int i, String name);
    }

    @NonNull
    public EditPipelineDialogBuilder setPositiveButton(CharSequence text, @Nullable OnClickListener listener) {
        return (EditPipelineDialogBuilder) super.setPositiveButton(text, (dialogInterface, i) -> {
            String name = input_pipelineName.getText().toString().trim();
            listener.onClick(dialogInterface, i, name);
        });
    }

    @NonNull
    @Override
    public EditPipelineDialogBuilder setView(int layoutResId) {
        return (EditPipelineDialogBuilder) super.setView(layoutResId);
    }

    @NonNull
    @Override
    public EditPipelineDialogBuilder setTitle(@Nullable CharSequence title) {
        return (EditPipelineDialogBuilder) super.setTitle(title);
    }

    @NonNull
    @Override
    public EditPipelineDialogBuilder setNegativeButton(@Nullable CharSequence text, @Nullable DialogInterface.OnClickListener listener) {
        return (EditPipelineDialogBuilder) super.setNegativeButton(text, listener);
    }

    public EditPipelineDialogBuilder(@NonNull Context context) {
        super(context);
    }

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
