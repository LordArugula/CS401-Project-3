package com.group1.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.group1.project3.util.FirebaseUtil;

/**
 * The Main activity is the title screen for the app.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseUtil.isSignedIn()) {
            launchProjectMenuActivity();
        }
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