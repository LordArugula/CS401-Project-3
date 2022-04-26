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
import com.group1.project3.model.Tag;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

public class EditTagDialogBuilder extends MaterialAlertDialogBuilder {

    private EditText input_tagName;
    private View view_bgColor;
    private int color;

    private Tag tag;

    public interface OnClickListener {
        void onClick(DialogInterface dialogInterface, int i, Tag tag);
    }

    public EditTagDialogBuilder(@NonNull Context context) {
        super(context);
    }

    public EditTagDialogBuilder setTag(Tag tag) {
        this.tag = tag;
        this.color = tag.getColor();
        return this;
    }

    @NonNull
    @Override
    public EditTagDialogBuilder setTitle(int titleId) {
        return (EditTagDialogBuilder) super.setTitle(titleId);
    }

    @NonNull
    @Override
    public EditTagDialogBuilder setTitle(@Nullable CharSequence title) {
        return (EditTagDialogBuilder) super.setTitle(title);
    }

    @NonNull
    @Override
    public EditTagDialogBuilder setView(@Nullable View view) {
        return (EditTagDialogBuilder) super.setView(view);
    }

    @NonNull
    @Override
    public EditTagDialogBuilder setView(int layoutResId) {
        return (EditTagDialogBuilder) super.setView(layoutResId);
    }

    @NonNull
    @Override
    public EditTagDialogBuilder setNegativeButton(int textId, @Nullable DialogInterface.OnClickListener listener) {
        return (EditTagDialogBuilder) super.setNegativeButton(textId, listener);
    }

    @NonNull
    @Override
    public EditTagDialogBuilder setNegativeButton(@Nullable CharSequence text, @Nullable DialogInterface.OnClickListener listener) {
        return (EditTagDialogBuilder) super.setNegativeButton(text, listener);
    }

    @NonNull
    public EditTagDialogBuilder setPositiveButton(int textId, @Nullable OnClickListener onClickListener) {
        return (EditTagDialogBuilder) super.setPositiveButton(textId, (dialogInterface, i) -> {
            tag.setName(input_tagName.getText().toString().trim());
            tag.setColor(color);
            onClickListener.onClick(dialogInterface, i, tag);
        });
    }

    @NonNull
    public EditTagDialogBuilder setPositiveButton(CharSequence text, @Nullable OnClickListener onClickListener) {
        return (EditTagDialogBuilder) super.setPositiveButton(text, (dialogInterface, i) -> {
            tag.setName(input_tagName.getText().toString().trim());
            tag.setColor(color);
            onClickListener.onClick(dialogInterface, i, tag);
        });
    }

    private boolean useDeleteButton = false;
    private OnClickListener onDeleteTagListener;

    public EditTagDialogBuilder setDeleteButton(boolean enabled, OnClickListener onClickListener) {
        useDeleteButton = enabled;
        onDeleteTagListener = onClickListener;
        return this;
    }

    @NonNull
    @Override
    public AlertDialog create() {
        AlertDialog alertDialog = super.create();

        alertDialog.setOnShowListener(dialogInterface -> {
            input_tagName = alertDialog.findViewById(R.id.dialog_editTag_input_name);
            input_tagName.setText(tag.getName());

            Button button_colorPicker = alertDialog.findViewById(R.id.dialog_editTag_button_colorPicker);
            button_colorPicker.setOnClickListener(this::onClickColorPickerButton);
            view_bgColor = alertDialog.findViewById(R.id.dialog_editTag_preview_colorPicker);
            view_bgColor.setBackgroundColor(tag.getColor());

            Button button_delete = alertDialog.findViewById(R.id.dialog_editTag_button_delete);
            if (useDeleteButton) {
                button_delete.setVisibility(View.VISIBLE);
                button_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onDeleteTagListener.onClick(dialogInterface, 0, tag);
                    }
                });
            } else {
                button_delete.setVisibility(View.INVISIBLE);
            }
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
}
