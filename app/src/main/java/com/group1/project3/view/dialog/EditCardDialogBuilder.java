package com.group1.project3.view.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.group1.project3.R;
import com.group1.project3.model.Card;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditCardDialogBuilder extends MaterialAlertDialogBuilder {

    private Project project;
    private Card card;
    private boolean isPreview;

    private String cardContent;
    private Date date;
    private String userId;
    private Map<Tag, Boolean> changedTags;

    private ViewGroup contentContainer;
    private EditText cardEditContent;
    private TextView cardPreviewContent;

    private TextView text_date;
    private DialogInterface.OnClickListener onClickRemoveListener;

    public interface OnClickListener {
        void onClick(DialogInterface dialogInterface, int i, Card card);
    }

    public EditCardDialogBuilder(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    public EditCardDialogBuilder setTitle(@Nullable CharSequence title) {
        return (EditCardDialogBuilder) super.setTitle(title);
    }

    @NonNull
    @Override
    public EditCardDialogBuilder setView(int layoutResId) {
        return (EditCardDialogBuilder) super.setView(layoutResId);
    }

    public EditCardDialogBuilder setProject(Project project) {
        this.project = project;
        return this;
    }

    public EditCardDialogBuilder setCard(Card card) {
        this.card = card;
        cardContent = card.getContent();
        userId = card.getAssignedUser();
        date = card.getAssignedDate();
        changedTags = new HashMap<>();
        for (Tag tag : project.getTags()) {
            changedTags.put(tag, card.getTags().contains(tag));
        }
        return this;
    }

    public EditCardDialogBuilder setRemoveButton(DialogInterface.OnClickListener listener) {
        onClickRemoveListener = listener;
        return this;
    }

    @NonNull
    @Override
    public EditCardDialogBuilder setNegativeButton(@Nullable CharSequence text, @Nullable DialogInterface.OnClickListener listener) {
        return (EditCardDialogBuilder) super.setNegativeButton(text, listener);
    }

    @NonNull
    @Override
    public EditCardDialogBuilder setNeutralButton(@Nullable CharSequence text, @Nullable DialogInterface.OnClickListener listener) {
        return (EditCardDialogBuilder) super.setNeutralButton(text, listener);
    }

    public EditCardDialogBuilder setPositiveButton(CharSequence text, OnClickListener onClickListener) {
        super.setPositiveButton(text, ((dialogInterface, i) -> {
            card.setContent(cardContent);
            card.setAssignedDate(date);
            card.setAssignedUser(userId);
            onClickListener.onClick(dialogInterface, i, card);
        }));
        return this;
    }

    @NonNull
    @Override
    public AlertDialog create() {
        setNeutralButton("Preview", null);

        AlertDialog alertDialog = super.create();
        alertDialog.setOnShowListener(dialogInterface -> {
            Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            contentContainer = alertDialog.findViewById(R.id.dialog_editCard_card_content_container);

            text_date = alertDialog.findViewById(R.id.dialog_editCard_date);
            text_date.setText(date == null ? "" : DateFormat.format("MM/dd/yyyy", date));

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

            setCardContent(alertDialog, neutralButton);
            neutralButton.setOnClickListener(view -> setCardContent(alertDialog, neutralButton));
        });
        return alertDialog;
    }

    private void setCardContent(AlertDialog alertDialog, Button neutralButton) {

        contentContainer.removeAllViews();

        if (isPreview) {
            neutralButton.setText("Preview");
            View _view = alertDialog.getLayoutInflater().inflate(R.layout.item_edit_card, contentContainer, false);
            contentContainer.addView(_view);
            cardEditContent = _view.findViewById(R.id.card_edit_content);
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
            neutralButton.setText("Edit");
            View _view = alertDialog.getLayoutInflater().inflate(R.layout.item_preview_card, contentContainer, false);
            contentContainer.addView(_view);
            cardPreviewContent = _view.findViewById(R.id.card_preview_content);

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
                            .setSingleChoiceItems(usernames, checked, (dialogInterface, i) -> {
                                tempUser[0] = userIds.get(i);
                            })
                            .setPositiveButton("Assign User", (dialogInterface, i) -> {
                                userId = tempUser[0];
                            })
                            .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                            .setNeutralButton("Unassign User", ((dialogInterface, i) -> {
                                userId = null;
                                dialogInterface.dismiss();
                            }))
                            .show();

                    this.setOnDismissListener(dialogInterface -> {
                        dialog.dismiss();
                    });
                });
    }

    private void openTagsDialog() {
        List<Tag> tags = project.getTags();
        String[] tagNames = new String[tags.size()];
        boolean[] checked = new boolean[tags.size()];

        for (int i = 0; i < tags.size(); i++) {
            tagNames[i] = tags.get(i).getName();
            checked[i] = changedTags.get(tags.get(i));
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Edit tags")
                .setMultiChoiceItems(tagNames, checked, (dialogInterface, i, b) -> changedTags.put(tags.get(i), b))
                .setPositiveButton("Set tags", (dialogInterface, i) -> {
                    for (Map.Entry<Tag, Boolean> entry : changedTags.entrySet()) {
                        if (entry.getValue()) {
                            card.addTag(entry.getKey());
                        } else {
                            card.removeTag(entry.getKey());
                        }
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

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
            Date date = _calendar.getTime();
            this.date = date;

            text_date.setText(DateFormat.format("MM/dd/yyyy", _calendar));
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(minDate);
        datePickerDialog.show();
    }
}

