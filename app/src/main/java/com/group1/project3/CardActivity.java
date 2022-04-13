package com.group1.project3;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class CardActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        auth = FirebaseAuth.getInstance();

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
}