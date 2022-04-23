package com.group1.project3.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.group1.project3.R;
import com.group1.project3.model.Card;
import com.group1.project3.model.Tag;
import com.group1.project3.repository.TagsRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardActivity extends AppCompatActivity {

    private Card card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.card_fragment_container_view, PreviewCardFragment.class, getIntent().getExtras())
                    .commit();
        }

        String cardAsJson = getIntent().getStringExtra("card");
        card = new Gson().fromJson(cardAsJson, Card.class);
    }

    public void editCardContent(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.card_fragment_container_view, EditCardFragment.class, getIntent().getExtras())
                .commit();
    }

    public void previewCardContent(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.card_fragment_container_view, PreviewCardFragment.class, getIntent().getExtras())
                .commit();
    }

    public void deleteCard(View view) {
        Toast.makeText(this, "TODO: Delete Card", Toast.LENGTH_SHORT).show();
    }

    public void cancelContentChange(View view) {
        Toast.makeText(this, "TODO: Cancel Changes to Content", Toast.LENGTH_SHORT).show();
    }

    public void saveContentChange(View view) {
        Toast.makeText(this, "TODO: Save Changes to Content", Toast.LENGTH_SHORT).show();
    }

    public void openDatePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                Date date = calendar.getTime();
                card.setAssignedDate(date);

                TextView dateText = findViewById(R.id.card_preview_date);
                dateText.setText(DateFormat.format("MM/dd/yyyy", calendar));
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    public void assignUser(View view) {

    }

    public void editTags(View view) {
        // TODO get project tags
        List<Tag> tags = TagsRepository.ITEMS;
        String[] tagNames = new String[tags.size()];
        boolean[] checked = new boolean[tags.size()];

        for (int i = 0; i < tags.size(); i++) {
            tagNames[i] = tags.get(i).getName();
            checked[i] = card.hasTag(tags.get(i));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Map<Tag, Boolean> changeTags = new HashMap<>();
        builder.setTitle("Edit tags")
                .setMultiChoiceItems(tagNames, checked, (dialogInterface, i, b) -> {
                    changeTags.put(tags.get(i), b);
                })
                .setPositiveButton("Set tags", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (Map.Entry<Tag, Boolean> entry : changeTags.entrySet()) {
                            if (entry.getValue()) {
                                card.addTag(entry.getKey());
                            } else {
                                card.removeTag(entry.getKey());
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void updateTags() {
    }
}