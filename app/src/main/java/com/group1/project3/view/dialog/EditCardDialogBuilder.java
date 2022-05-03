package com.group1.project3.view.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.group1.project3.R;
import com.group1.project3.adapter.TagIconAdapter;
import com.group1.project3.model.Card;
import com.group1.project3.model.Pipeline;
import com.group1.project3.model.Project;
import com.group1.project3.model.Tag;
import com.group1.project3.model.User;
import com.group1.project3.repository.FirestoreUserRepository;
import com.group1.project3.repository.UserRepository;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Builds the EditCardDialog.
 */
public class EditCardDialogBuilder extends MaterialAlertDialogBuilder {

    /**
     * The project the card belongs to.
     */
    private Project project;
    /**
     * The pipeline the card belongs to.
     */
    private Pipeline pipeline;
    /**
     * The card to edit.
     */
    private Card card;
    /**
     * Whether to preview the card content or not.
     */
    private boolean isPreview = true;

    /**
     * The card content.
     */
    private String cardContent;
    /**
     * The card date.
     */
    private Date date;
    /**
     * The card assigned user id.
     */
    private String userId;
    /**
     * The card tags.
     */
    private List<Tag> _tags;
    /**
     * The tag icon adapter.
     */
    private TagIconAdapter tagIconAdapter;

    /**
     * The card content container ViewGroup.
     */
    private ViewGroup contentContainer;

    /**
     * The card date TextView.
     */
    private TextView text_date;
    /**
     * The onClickListener for the remove button.
     */
    private DialogInterface.OnClickListener onClickRemoveListener;

    private boolean readonly;

    public EditCardDialogBuilder setReadOnly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    /**
     * The onClickListener interface.
     */
    public interface OnClickListener {
        /**
         * The onClick callback.
         *
         * @param dialogInterface the dialogInterface.
         * @param i               the button index.
         * @param card            the card.
         */
        void onClick(DialogInterface dialogInterface, int i, Card card);
    }

    /**
     * Creates the builder.
     *
     * @param context the context.
     */
    public EditCardDialogBuilder(@NonNull Context context) {
        super(context);
    }

    /**
     * Sets the dialog title
     *
     * @param title the title.
     * @return the builder.
     */
    @NonNull
    @Override
    public EditCardDialogBuilder setTitle(@Nullable CharSequence title) {
        return (EditCardDialogBuilder) super.setTitle(title);
    }

    /**
     * Sets the dialog view.
     *
     * @param layoutResId the id for the view.
     * @return the builder.
     */
    @NonNull
    @Override
    public EditCardDialogBuilder setView(int layoutResId) {
        return (EditCardDialogBuilder) super.setView(layoutResId);
    }

    /**
     * Sets the card to edit.
     *
     * @param card     the card.
     * @param pipeline the pipeline the card belongs to.
     * @param project  the project the card belongs to.
     * @return the builder.
     */
    public EditCardDialogBuilder setCard(Card card, Pipeline pipeline, Project project) {
        this.project = project;
        this.pipeline = pipeline;
        this.card = card;
        cardContent = card.getContent();
        userId = card.getAssignedUser();
        date = card.getAssignedDate();
        _tags = new ArrayList<>(card.getTags());
        return this;
    }

    /**
     * Sets the remove button listener.
     *
     * @param listener the onClick listener.
     * @return the builder.
     */
    public EditCardDialogBuilder setRemoveButton(DialogInterface.OnClickListener listener) {
        onClickRemoveListener = listener;
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
    public EditCardDialogBuilder setNegativeButton(@Nullable CharSequence text, @Nullable DialogInterface.OnClickListener listener) {
        return (EditCardDialogBuilder) super.setNegativeButton(text, listener);
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
    public EditCardDialogBuilder setNeutralButton(@Nullable CharSequence text, @Nullable DialogInterface.OnClickListener listener) {
        return (EditCardDialogBuilder) super.setNeutralButton(text, listener);
    }

    /**
     * Sets the positive button text and listener.
     *
     * @param text            the text.
     * @param onClickListener the listener.
     * @return the builder.
     */
    public EditCardDialogBuilder setPositiveButton(CharSequence text, OnClickListener onClickListener) {
        super.setPositiveButton(text, ((dialogInterface, i) -> {
            if (targetPipeline != -1) {
                pipeline.moveCard(card, project.getPipelines().get(targetPipeline));
            }
            card.setContent(cardContent);
            card.setAssignedDate(date);
            card.setAssignedUser(userId);
            onClickListener.onClick(dialogInterface, i, card);
        }));
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
        setNeutralButton("Preview", null);

        AlertDialog alertDialog = super.create();
        alertDialog.setOnShowListener(dialogInterface -> {
            // Hooks up events and binding data.
            Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            contentContainer = alertDialog.findViewById(R.id.dialog_editCard_card_content_container);

            text_date = alertDialog.findViewById(R.id.dialog_editCard_date);
            text_date.setText(date == null ? "" : DateFormat.format("MM/dd/yyyy", date));

            tagIconAdapter = new TagIconAdapter(_tags);
            RecyclerView recyclerView_tags = alertDialog.findViewById(R.id.dialog_editCard_recyclerView_tags);
            recyclerView_tags.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView_tags.setAdapter(tagIconAdapter);

            Button datePicker = alertDialog.findViewById(R.id.dialog_editCard_button_date);
            datePicker.setOnClickListener(view -> openDatePicker());

            Button userButton = alertDialog.findViewById(R.id.dialog_editCard_button_user);
            userButton.setOnClickListener(view -> openUserDialog());

            Button tagButton = alertDialog.findViewById(R.id.dialog_editCard_button_tags);
            tagButton.setOnClickListener(view -> openTagsDialog());

            Button removeButton = alertDialog.findViewById(R.id.dialog_editCard_button_remove);
            if (onClickRemoveListener != null) {
                removeButton.setOnClickListener(view -> onClickRemoveListener.onClick(alertDialog, 1));
            } else {
                removeButton.setVisibility(View.INVISIBLE);
            }

            Button moveButton = alertDialog.findViewById(R.id.dialog_editCard_button_move);
            moveButton.setOnClickListener(view -> openPipelinePickerDialog());

            setCardContent(alertDialog, neutralButton);
            neutralButton.setOnClickListener(view -> setCardContent(alertDialog, neutralButton));

            if (readonly) {
                neutralButton.setVisibility(View.INVISIBLE);
                removeButton.setVisibility(View.INVISIBLE);
                moveButton.setVisibility(View.INVISIBLE);
                datePicker.setVisibility(View.INVISIBLE);
                userButton.setVisibility(View.INVISIBLE);
                tagButton.setVisibility(View.INVISIBLE);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.INVISIBLE);
                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.INVISIBLE);
            }
        });
        return alertDialog;
    }

    /**
     * The pipeline index to move to the card to.
     */
    private int targetPipeline = -1;

    /**
     * Opens the pipeline picker dialog.
     */
    private void openPipelinePickerDialog() {
        List<Pipeline> pipelines = project.getPipelines();
        String[] _pipelines = new String[pipelines.size()];
        for (int i = 0; i < pipelines.size(); i++) {
            _pipelines[i] = pipelines.get(i).getName();
        }
        int checked = pipelines.indexOf(pipeline);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        AlertDialog dialog = builder.setTitle("Move to")
                .setSingleChoiceItems(_pipelines, checked, (dialogInterface, i) -> {
                    targetPipeline = i;
                })
                .setPositiveButton("Move", (dialogInterface, i) -> {
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    targetPipeline = -1;
                })
                .show();

        this.setOnDismissListener(dialogInterface -> dialog.dismiss());

    }

    /**
     * Sets the card content based on whether the user is in preview or edit mode.
     *
     * @param alertDialog   the dialog.
     * @param neutralButton the neutral button.
     */
    private void setCardContent(AlertDialog alertDialog, Button neutralButton) {

        contentContainer.removeAllViews();

        if (isPreview && !readonly) {
            // Sets the edit mode view
            neutralButton.setText("Preview");
            View _view = alertDialog.getLayoutInflater().inflate(R.layout.item_edit_card, contentContainer, false);
            contentContainer.addView(_view);
            EditText cardEditContent = _view.findViewById(R.id.card_edit_content);
            cardEditContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // unused
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    cardContent = cardEditContent.getText().toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // unused
                }
            });
            cardEditContent.setText(cardContent);
            isPreview = false;
        } else {
            // Sets the preview mode view.
            neutralButton.setText("Edit");
            View _view = alertDialog.getLayoutInflater().inflate(R.layout.item_preview_card, contentContainer, false);
            contentContainer.addView(_view);
            TextView cardPreviewContent = _view.findViewById(R.id.card_preview_content);

            if (!(cardContent == null || cardContent.isEmpty())) {
                String content = cardContent;
                if (!cardContent.startsWith("#")) {
                    content = "# " + cardContent;
                }
                Parser parser = Parser.builder().build();
                Node document = parser.parse(content);
                HtmlRenderer renderer = HtmlRenderer.builder().build();
                cardPreviewContent.setText(Html.fromHtml(renderer.render(document), Html.FROM_HTML_MODE_LEGACY));
            }
            isPreview = true;
        }
    }

    /**
     * Opens the assign user dialog.
     */
    private void openUserDialog() {
        UserRepository userRepository = new FirestoreUserRepository();
        List<String> userIds = new ArrayList<>(project.getEditors().keySet());

        userRepository.getUsers(userIds)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int checked = -1;
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                    String[] usernames = new String[userIds.size()];
                    for (int i = 0, documentsSize = documents.size(); i < documentsSize; i++) {
                        DocumentSnapshot snapshot = documents.get(i);
                        User user = snapshot.toObject(User.class);
                        usernames[i] = user.getUsername();
                        if (user.getId().equals(userId)) {
                            checked = i;
                        }
                    }

                    final String[] tempUser = new String[1];
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                    AlertDialog dialog = builder.setTitle("Edit tags")
                            .setSingleChoiceItems(usernames, checked, (dialogInterface, i) -> tempUser[0] = userIds.get(i))
                            .setPositiveButton("Assign User", (dialogInterface, i) -> userId = tempUser[0])
                            .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                            .setNeutralButton("Unassign User", ((dialogInterface, i) -> {
                                userId = null;
                                dialogInterface.dismiss();
                            }))
                            .show();

                    this.setOnDismissListener(dialogInterface -> dialog.dismiss());
                });
    }

    /**
     * Opens the assign tags dialog.
     */
    private void openTagsDialog() {
        List<Tag> tags = project.getTags();
        String[] tagNames = new String[tags.size()];
        boolean[] checked = new boolean[tags.size()];

        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            tagNames[i] = tag.getName();
            checked[i] = _tags.contains(tag);
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Edit tags")
                .setMultiChoiceItems(tagNames, checked, (dialogInterface, i, b) -> {
                    if (b) {
                        _tags.add(project.getTags().get(i));
                    } else {
                        _tags.remove(project.getTags().get(i));
                    }
                })
                .setPositiveButton("Set tags", (dialogInterface, i) -> {
                    card.clearTags();
                    for (Tag tag : _tags) {
                        card.addTag(tag);
                    }
                    tagIconAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    /**
     * Opens the assign date dialog.
     */
    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        long minDate = calendar.getTimeInMillis();
        int year, month, day;
        if (card.getAssignedDate() != null) {
            ZonedDateTime zonedDateTime = card.getAssignedDate().toInstant().atZone(ZoneId.systemDefault());
            year = zonedDateTime.getYear();
            month = zonedDateTime.getMonthValue();
            day = zonedDateTime.getDayOfMonth();
        } else {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, _year, _month, _day) -> {
            Calendar _calendar = Calendar.getInstance();
            _calendar.set(_year, _month, _day);
            this.date = _calendar.getTime();

            text_date.setText(DateFormat.format("MM/dd/yyyy", _calendar));
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(minDate);
        datePickerDialog.show();
    }
}

