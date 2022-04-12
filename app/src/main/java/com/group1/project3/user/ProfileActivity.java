package com.group1.project3.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.group1.project3.R;

public class ProfileActivity extends AppCompatActivity {

    EditText text_userName;
    EditText text_firstName;
    EditText text_lastName;
    EditText text_emailAddress;
    EditText text_oldPassword;
    EditText text_newPassword;
    EditText text_confirmPassword;

    String username, firstName, lastName, email;
    String oldPassword, newPassword, confirmPassword;
    String uid;


    Boolean nameEmailChanges = false;

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

        //makes username field uneditable
        text_userName.setKeyListener(null);
        text_firstName.addTextChangedListener(textWatcherUsername);
        text_lastName.addTextChangedListener(textWatcherUsername);

        text_userName.addTextChangedListener(textWatcherNameEmail);
        text_firstName.addTextChangedListener(textWatcherNameEmail);
        text_lastName.addTextChangedListener(textWatcherNameEmail);
        text_emailAddress.addTextChangedListener(textWatcherNameEmail);


        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            username = user.getDisplayName();
            email = user.getEmail();
            uid = user.getUid();

            text_userName.setText(username);
            text_emailAddress.setText(email);
        }
        else {
            Toast.makeText(this, "An error occurred when retrieving your profile information.", Toast.LENGTH_SHORT).show();
        }

    }

    public void toProjectView(View view) {
    }

    public void changePassword(View view) {
        //update email in case it changed
        email = user.getEmail();
        oldPassword = text_oldPassword.getText().toString();
        newPassword = text_newPassword.getText().toString();
        confirmPassword = text_confirmPassword.getText().toString();

        assert email != null;

        if (!oldPassword.matches("") && !newPassword.matches("") && !confirmPassword.matches("")) {
            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("updatepassword", "Password updated");
                                } else {
                                    Log.d("updatepassword", "Password not updated");
                                }
                            }
                        });
                    } else {
                        Log.d("Reauth", "Error auth failed");
                        Toast.makeText(ProfileActivity.this, "Password is incorrect", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(ProfileActivity.this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
        }

    }

    public void saveProfileChanges(View view) {
        username = text_userName.getText().toString();
        firstName = text_firstName.getText().toString();
        lastName = text_lastName.getText().toString();
        email = text_emailAddress.getText().toString();

        if (nameEmailChanges) {

        }
    }

    private TextWatcher textWatcherUsername = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            firstName = text_firstName.getText().toString();
            lastName = text_lastName.getText().toString();
            username = firstName + " " + lastName;

            text_userName.setText(username);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher textWatcherNameEmail = new TextWatcher() {
        String oldUsername, oldFirst, oldLast, oldEmail;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            oldUsername = text_userName.getText().toString();
            oldFirst = text_firstName.getText().toString();
            oldLast = text_lastName.getText().toString();
            oldEmail = text_emailAddress.getText().toString();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if () {
                nameEmailChanges = true;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}
