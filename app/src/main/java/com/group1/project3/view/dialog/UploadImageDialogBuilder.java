package com.group1.project3.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.group1.project3.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * The UploadImageDialog builder.
 */
public class UploadImageDialogBuilder extends MaterialAlertDialogBuilder {

    /**
     * The Uri for the image.
     */
    private Uri imageUri;
    /**
     * The placeholder resource id.
     */
    private int placeholderResId;

    /**
     * The image url EditText.
     */
    private EditText input_imageUrl;
    /**
     * The preview ImageView.
     */
    private ImageView image_preview;
    /**
     * The upload button.
     */
    private Button button_upload;

    /**
     * Creates the builder.
     *
     * @param context the context.
     */
    public UploadImageDialogBuilder(@NonNull Context context) {
        super(context);
    }

    /**
     * Sets teh title of the dialog.
     *
     * @param title the title
     * @return the builder
     */
    @NonNull
    @Override
    public UploadImageDialogBuilder setTitle(@Nullable CharSequence title) {
        return (UploadImageDialogBuilder) super.setTitle(title);
    }

    /**
     * Sets the view of the dialog.
     *
     * @param layoutResId the view id.
     * @return the builder.
     */
    @NonNull
    @Override
    public UploadImageDialogBuilder setView(int layoutResId) {
        return (UploadImageDialogBuilder) super.setView(layoutResId);
    }

    /**
     * Sets the default image Uri.
     *
     * @param imageUri the image Uri
     * @return the builder.
     */
    @NonNull
    public UploadImageDialogBuilder setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
        return this;
    }

    /**
     * Sets the placeholder image resource id.
     *
     * @param placeholderResId the resource id.
     * @return the builder.
     */
    @NonNull
    public UploadImageDialogBuilder setPlaceholderImage(int placeholderResId) {
        this.placeholderResId = placeholderResId;
        return this;
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
    public UploadImageDialogBuilder setNegativeButton(@Nullable CharSequence text, @Nullable DialogInterface.OnClickListener listener) {
        return (UploadImageDialogBuilder) super.setNegativeButton(text, listener);
    }

    /**
     * Sets the neutral button text and listener.
     *
     * @param text     the text.
     * @param listener the listener.
     * @return the builder.
     */
    @NonNull
    @Override
    public UploadImageDialogBuilder setNeutralButton(@Nullable CharSequence text, @Nullable DialogInterface.OnClickListener listener) {
        return (UploadImageDialogBuilder) super.setNeutralButton(text, listener);
    }

    /**
     * Sets the positive button text and listener.
     *
     * @param text            the text.
     * @param onClickListener the listener.
     * @return the builder.
     */
    @NonNull
    public UploadImageDialogBuilder setPositiveButton(@Nullable CharSequence text, @Nullable OnClickListener onClickListener) {
        setPositiveButton(text, (dialogInterface, i) -> onClickListener.onClick(dialogInterface, i, imageUri));
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
        setView(R.layout.dialog_image_url);

        setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

        AlertDialog alertDialog = super.create();
        alertDialog.setOnShowListener(dialogInterface -> {
            input_imageUrl = alertDialog.findViewById(R.id.imageURLDialog_input_imageUrl);
            image_preview = alertDialog.findViewById(R.id.imageURLDialog_image_preview);

            Picasso.get()
                    .load(imageUri)
                    .config(Bitmap.Config.ARGB_8888)
                    .resize(64, 64)
                    .placeholder(placeholderResId)
                    .into(image_preview);
            image_preview.setClipToOutline(true);

            button_upload = alertDialog.findViewById(R.id.imageURLDialog_button_upload);
            button_upload.setEnabled(false);
            button_upload.setOnClickListener(view -> {
                String imageUrlString = input_imageUrl.getText().toString().trim();
                Uri _imageUri = Uri.parse(imageUrlString);

                Picasso.get()
                        .load(_imageUri)
                        .resize(64, 64)
                        .config(Bitmap.Config.ARGB_8888)
                        .placeholder(placeholderResId)
                        .into(image_preview, new Callback() {
                            @Override
                            public void onSuccess() {
                                imageUri = _imageUri;
                                image_preview.setClipToOutline(true);
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            });

            input_imageUrl.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String url = input_imageUrl.getText().toString().trim();
                    button_upload.setEnabled(!url.isEmpty() && Patterns.WEB_URL.matcher(url).matches());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        });
        return alertDialog;
    }

    /**
     * The OnClickListener interface.
     */
    public interface OnClickListener {
        /**
         * The onClick callback.
         *
         * @param dialogInterface the dialog interface.
         * @param i               the dialog button index.
         * @param imageUri        the image Uri.
         */
        void onClick(DialogInterface dialogInterface, int i, Uri imageUri);
    }
}
