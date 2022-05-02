package com.group1.project3.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.group1.project3.R;
import com.group1.project3.util.FirebaseUtil;

/**
 * The Main activity is the title screen for the app.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState the saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the activity starts.
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseUtil.isSignedIn()) {
            launchProjectMenuActivity();
        }

        getSupportActionBar().hide();
    }

    /**
     * Launches the {@link LoginActivity}.
     *
     * @param view The view.
     */
    public void launchLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the {@link RegisterActivity}.
     *
     * @param view The view.
     */
    public void launchRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Launches the {@link ProjectMenuActivity}.
     */
    private void launchProjectMenuActivity() {
        Intent intent = new Intent(this, ProjectMenuActivity.class);
        startActivity(intent);
        finish();
    }
}