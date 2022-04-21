package com.group1.project3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.group1.project3.model.User;
import com.group1.project3.util.FirebaseUtil;

public class RegisterActivity extends AppCompatActivity {

    private EditText text_firstName;
    private EditText text_lastName;
    private EditText text_username;
    private EditText text_email;
    private EditText text_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        text_firstName = findViewById(R.id.register_text_firstName);
        text_lastName = findViewById(R.id.register_text_lastName);
        text_username = findViewById(R.id.register_text_username);
        text_email = findViewById(R.id.register_text_emailAddress);
        text_password = findViewById(R.id.register_text_password);
        EditText confirmPasswordText = findViewById(R.id.register_text_confirmPassword);

        Button registerButton = findViewById(R.id.register_submit_button);
        registerButton.setEnabled(false);

        TextWatcher textWatcher = new RegisterFormValidator(text_firstName, text_lastName, text_username, text_email, text_password, confirmPasswordText, registerButton);
        text_firstName.addTextChangedListener(textWatcher);
        text_lastName.addTextChangedListener(textWatcher);
        text_username.addTextChangedListener(textWatcher);
        text_email.addTextChangedListener(textWatcher);
        text_password.addTextChangedListener(textWatcher);
        confirmPasswordText.addTextChangedListener(textWatcher);
    }

    public void toMainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void registerAccount(View view) {
        String email = text_email.getText().toString();
        String password = text_password.getText().toString();

        FirebaseAuth auth = FirebaseUtil.getAuth();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;

                            String firstName = text_firstName.getText().toString().trim();
                            String lastName = text_lastName.getText().toString().trim();
                            String username = text_username.getText().toString().trim();

                            User user = new User(firebaseUser.getUid(), firstName, lastName, username, email);

                            UserProfileChangeRequest.Builder changeRequestBuilder = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username);
                            firebaseUser.updateProfile(changeRequestBuilder.build());

                            FirebaseUtil.getFirestore().collection("users")
                                    .document(firebaseUser.getUid())
                                    .set(user.serializeAsMap())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("REGISTER", "Registered account");
                                            }
                                        }
                                    });

                            launchProjectMenuActivity(firebaseUser);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to register account.", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

    private void launchProjectMenuActivity(FirebaseUser user) {
        Intent intent = new Intent(this, ProjectMenuActivity.class);
        startActivity(intent);
    }
}