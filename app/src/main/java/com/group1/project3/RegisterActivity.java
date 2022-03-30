package com.group1.project3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private EditText emailText;
    private EditText passwordText;
    private EditText confirmPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailText = findViewById(R.id.text_emailAddress);
        passwordText = findViewById(R.id.text_password);
        confirmPasswordText = findViewById(R.id.text_confirm_password);

        confirmPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String password = passwordText.getText().toString();
                String confirmPassword = confirmPasswordText.getText().toString();

                verifyPasswordsMatch(password, confirmPassword);
            }
        });

        auth = FirebaseAuth.getInstance();
    }

    private boolean verifyPasswordsMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Password does not match.", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }

    public void toMainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void registerAccount(View view) {

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        if (!verifyPasswordsMatch(password, confirmPassword)) {
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
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