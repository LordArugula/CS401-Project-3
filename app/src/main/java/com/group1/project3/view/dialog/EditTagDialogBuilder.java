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

/**
 * The EditTagDialog builder.
 */
public class EditTagDialogBuilder extends MaterialAlertDialogBuilder {

    /**
     * The tag name EditText.
     */
    private EditText input_tagName;
    /**
     * The background color view.
     */
    private View view_bgColor;
    /**
     * The background color.
     */
    private int color;
    /**
     * The tag.
     */
    private Tag tag;

    /**
     * The OnClickListener interface.
     */
    public interface OnClickListener {
        /**
         * The onClick callback.
         *
         * @param dialogInterface the dialog interface.
         * @param i               the button index.
         * @param tag             the tag.
         */
        void onClick(DialogInterface dialogInterface, int i, Tag tag);
    }

    /**
     * Creates the dialog builder.
     *
     * @param context the context.
     */
    public EditTagDialogBuilder(@NonNull Context context) {
        super(context);
    }

    /**
     * Sets the tag to edit.
     *
     * @param tag the tag.
     * @return the builder.
     */
    public EditTagDialogBuilder setTag(Tag tag) {
        this.tag = tag;
        this.color = tag.getColor();
        return this;
    }

    /**
     * Sets the title of the dialog.
     *
     * @param title the title.
     * @return the builder.
     */
    @NonNull
    @Override
    public EditTagDialogBuilder setTitle(@Nullable CharSequence title) {
        return (EditTagDialogBuilder) super.setTitle(title);
    }

    /**
     * Sets the view of the dialog.
     *
     * @param layoutResId the view id.
     * @return the builder.
     */
    @NonNull
    @Override
    public EditTagDialogBuilder setView(int layoutResId) {
        return (EditTagDialogBuilder) super.setView(layoutResId);
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
    public EditTagDialogBuilder setNegativeButton(@Nullable CharSequence text, @Nullable DialogInterface.OnClickListener listener) {
        return (EditTagDialogBuilder) super.setNegativeButton(text, listener);
    }

    /**
     * Sets the positive button text and listener.
     *
     * @param text            the text.
     * @param onClickListener the listener.
     * @return the builder.
     */
    @NonNull
    public EditTagDialogBuilder setPositiveButton(CharSequence text, @Nullable OnClickListener onClickListener) {
        return (EditTagDialogBuilder) super.setPositiveButton(text, (dialogInterface, i) -> {
            tag.setName(input_tagName.getText().toString().trim());
            tag.setColor(color);
            onClickListener.onClick(dialogInterface, i, tag);
        });
    }

    /**
     * Whether to show the delete button or not.
     */
    private boolean useDeleteButton = false;
    /**
     * The delete button onClick listener.
     */
    private OnClickListener onDeleteTagListener;

    /**
     * Sets the delete button listener.
     *
     * @param enabled         whether to use the delete button.
     * @param onClickListener the delete button listener.
     * @return the builder.
     */
    public EditTagDialogBuilder setDeleteButton(boolean enabled, OnClickListener onClickListener) {
        useDeleteButton = enabled;
        onDeleteTagListener = onClickListener;
        return this;
    }

    /**
     * Creates the alert dialog.
     *
     * @return
     */
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
                builder.getColorPickerView().selectByHsvColor(color);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        alertDialog.show();
    }
}
