package com.group1.project3;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.group1.project3.model.Tag;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//import com.google.firebase.auth.FirebaseAuth;

public class CardActivity extends AppCompatActivity {
    private List<Tag> projectTags;

    //    private FirebaseAuth auth;
    public static List<Tag> tags;

    public CardActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
//
//        auth = FirebaseAuth.getInstance();

        projectTags = new ArrayList<>();
        projectTags.add(new Tag("Hello"));
        projectTags.add(new Tag("World"));
        projectTags.add(new Tag("Test"));
        tags = new ArrayList<>();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, PreviewCardFragment.class, null)
                    .commit();
        }
    }

    public void editCard(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, EditCardFragment.class, null)
                .commit();
    }

    public void previewCard(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, PreviewCardFragment.class, null)
                .commit();
    }

    public void deleteCard(View view) {
        Toast.makeText(this, "Delete Card", Toast.LENGTH_SHORT).show();
    }

    public void cancelChanges(View view) {
        Toast.makeText(this, "Cancel Changes", Toast.LENGTH_SHORT).show();
    }

    public void saveChanges(View view) {
        Toast.makeText(this, "Save Changes", Toast.LENGTH_SHORT).show();
    }

    public void assignDate(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // TODO Update card
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                Date date = calendar.getTime();
                // card.setDate();

                TextView dateText = findViewById(R.id.card_date);
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
        String[] tagNames = new String[projectTags.size()];

        tagNames = projectTags.stream()
                .map(Tag::getName)
                .toArray(String[]::new);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(tagNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tags.add(projectTags.get(i));
                updateTags();
            }
        })
                .create().show();
    }

    private void updateTags() {
    }
}