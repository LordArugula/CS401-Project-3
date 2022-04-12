package com.group1.project3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CardActivity extends AppCompatActivity {

    private final String TAG_CARD_PREVIEW_FRAGMENT = "CARD_PREVIEW_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, PreviewCardFragment.class, null)
                    .commit();
//
//            Fragment cardPreviewFragment = getSupportFragmentManager()
//                    .findFragmentByTag(TAG_CARD_PREVIEW_FRAGMENT);
        }
    }

    public void editCard(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, EditCardFragment.class, null)
                .commit();
        ;
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
}