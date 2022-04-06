package com.group1.project3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
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

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private EditText firstNameText;
    private EditText lastNameText;
    private EditText emailText;
    private EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameText = findViewById(R.id.text_firstName);
        lastNameText = findViewById(R.id.text_lastName);
        emailText = findViewById(R.id.text_emailAddress);
        passwordText = findViewById(R.id.text_password);
        EditText confirmPasswordText = findViewById(R.id.text_confirm_password);

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setEnabled(false);

        TextWatcher textWatcher = new RegisterFormValidator(firstNameText, lastNameText, emailText, passwordText, confirmPasswordText, registerButton);
        firstNameText.addTextChangedListener(textWatcher);
        lastNameText.addTextChangedListener(textWatcher);
        emailText.addTextChangedListener(textWatcher);
        passwordText.addTextChangedListener(textWatcher);
        confirmPasswordText.addTextChangedListener(textWatcher);

        auth = FirebaseAuth.getInstance();
    }

    public void toMainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void registerAccount(View view) {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;

                            String firstName = firstNameText.getText().toString();
                            String lastName = lastNameText.getText().toString();

                            UserProfileChangeRequest.Builder changeRequestBuilder = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(firstName + " " + lastName);
                            user.updateProfile(changeRequestBuilder.build());

                            updateUI(user);
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to register account.", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        Toast.makeText(RegisterActivity.this, "TODO Register successful", Toast.LENGTH_SHORT)
                .show();
    }
}