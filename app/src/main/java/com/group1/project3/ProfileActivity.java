package com.group1.project3;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group1.project3.model.User;
import com.group1.project3.util.FirebaseUtil;

public class ProfileActivity extends AppCompatActivity {

    private EditText text_userName;
    private EditText text_firstName;
    private EditText text_lastName;
    private EditText text_emailAddress;
    private EditText text_oldPassword;
    private EditText text_newPassword;
    private EditText text_confirmPassword;
    private Button button_updateProfile;
    private Button button_updatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        text_userName = findViewById(R.id.profile_text_userName);
        text_firstName = findViewById(R.id.profile_text_firstName);
        text_lastName = findViewById(R.id.profile_text_lastName);
        text_emailAddress = findViewById(R.id.profile_text_emailAddress);
        text_oldPassword = findViewById(R.id.profile_text_oldPassword);
        text_newPassword = findViewById(R.id.profile_text_password);
        text_confirmPassword = findViewById(R.id.profile_text_confirmPassword);
        button_updateProfile = findViewById(R.id.profile_save_button);
        button_updatePassword = findViewById(R.id.profile_changePassword_button);

        TextWatcher profileWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean error = false;
                if (text_userName.getText().toString().trim().isEmpty()) {
                    error = true;
                }

                if (text_firstName.getText().toString().trim().isEmpty()) {
                    error = true;
                }

                if (text_lastName.getText().toString().trim().isEmpty()) {
                    error = true;
                }

                String email = text_emailAddress.getText().toString().trim();
                if (email.isEmpty()) {
                    error = true;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    error = true;
                }

                button_updateProfile.setEnabled(!error);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        text_userName.addTextChangedListener(profileWatcher);
        text_firstName.addTextChangedListener(profileWatcher);
        text_lastName.addTextChangedListener(profileWatcher);
        text_emailAddress.addTextChangedListener(profileWatcher);

        TextWatcher passwordWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean error = false;

                if (text_oldPassword.getText().toString().isEmpty()) {
                    error = true;
                }

                String newPassword = text_newPassword.getText().toString();
                if (newPassword.isEmpty()) {
                    error = true;
                }

                String confirmPassword = text_confirmPassword.getText().toString();
                if (confirmPassword.isEmpty()) {
                    error = true;
                }

                if (!newPassword.equals(confirmPassword)) {
                    error = true;
                }

                findViewById(R.id.profile_changePassword_button).setEnabled(!error);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        text_oldPassword.addTextChangedListener(passwordWatcher);
        text_newPassword.addTextChangedListener(passwordWatcher);
        text_confirmPassword.addTextChangedListener(passwordWatcher);

        bind(getUserFromAuth(FirebaseUtil.getAuth().getCurrentUser()));
    }

    private User getUserFromAuth(FirebaseUser currentUser) {
        final User[] user = {null};
        FirebaseUtil.getFirestore()
                .collection("users")
                .document(currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot result = task.getResult();
                            user[0] = result.toObject(User.class);
                        } else {
                            Toast.makeText(ProfileActivity.this, "Could not get user", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
        return user[0];

        /*DocumentReference userDocRef = FirebaseFirestore.getInstance().document("users/" + currentUser.getUid());
        final User[] user = {null};
        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user[0] = documentSnapshot.toObject(User.class);
            }
        });

        System.out.println(user);

        return user[0];*/
    }

    private void bind(User user) {
        if (user == null) {
            return;
        }
        text_userName.setText(user.getUsername());
        text_emailAddress.setText(user.getEmail());
        text_userName.setText(user.getUsername());
        text_firstName.setText(user.getFirstName());
        text_lastName.setText((user.getLastName()));
    }

    public void toProjectView(View view) {
        Intent intent = new Intent(this, ProjectMenuActivity.class);
        startActivity(intent);
        startActivity(intent);
    }

    public void updatePassword(View view) {
        FirebaseUser user = FirebaseUtil.getAuth().getCurrentUser();
        String email = user.getEmail();
        String oldPassword = text_oldPassword.getText().toString();
        String newPassword = text_newPassword.getText().toString();

        AuthCredential credentials = EmailAuthProvider.getCredential(email, oldPassword);
        user.reauthenticate(credentials)
                .addOnCompleteListener(authTask -> {
                    if (authTask.isSuccessful()) {
                        user.updatePassword(newPassword)
                                .addOnSuccessListener(changePasswordTask -> {
                                    Toast.makeText(ProfileActivity.this, "Updated password.", Toast.LENGTH_SHORT)
                                            .show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT)
                                            .show();
                                });
                    } else {
                        Toast.makeText(ProfileActivity.this, "Incorrect password", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    public void updateProfile(View view) {
        FirebaseUser user = FirebaseUtil.getAuth().getCurrentUser();

        String email = text_emailAddress.getText().toString().trim();
        if (!user.getEmail().equals(email)) {
            user.updateEmail(email);
        }

        String username = text_userName.getText().toString().trim();
        if (!user.getDisplayName().equals(username)) {
            UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username);
            user.updateProfile(builder.build());
        }

        FirebaseUtil.getFirestore().collection("users")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Updated Profile", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }
}
