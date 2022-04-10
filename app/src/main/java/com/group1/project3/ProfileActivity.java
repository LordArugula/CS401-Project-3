package com.group1.project3;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends Activity {

    EditText text_userName;
    EditText text_firstName;
    EditText text_lastName;
    EditText text_emailAddress;
    EditText text_oldPassword;
    EditText text_newPassword;
    EditText text_confirmPassword;

    String name, email;
    Uri photoUrl;
    String uid;


    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        text_userName = findViewById(R.id.text_userName);
        text_firstName = findViewById(R.id.text_firstName);
        text_lastName = findViewById(R.id.text_lastName);
        text_emailAddress = findViewById(R.id.text_emailAddress);
        text_oldPassword = findViewById(R.id.text_oldPassword);
        text_newPassword = findViewById(R.id.text_password);
        text_confirmPassword = findViewById(R.id.text_confirmPassword);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            email = user.getEmail();
            photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            uid = user.getUid();
        }
    }

    @Override
    protected void onStart() {

        super.onStart();
        text_emailAddress.setText(email);

    }

    public void registerAccount(View view) {
    }

    public void toMainMenu(View view) {
    }

    public void changePassword(View view) {
    }

    public void saveProfileChanges(View view) {
    }
}
