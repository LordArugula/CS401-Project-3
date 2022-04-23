package com.group1.project3;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.group1.project3.util.FirebaseUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText input_email;
    private EditText input_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input_email = findViewById(R.id.login_input_email);
        input_password = findViewById(R.id.login_input_password);

        Button button_signIn = findViewById(R.id.login_button_signUp);
        button_signIn.setOnClickListener(this::onClickSignInButton);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.login_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * OnClick callback for the sign in button.
     * Signs the user in.
     *
     * @param view the button that was clicked on.
     */
    private void onClickSignInButton(View view) {
        signIn()
                .addOnSuccessListener(authResult -> finish())
                .addOnFailureListener(exception -> Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Task<AuthResult> signIn() {
        String email = input_email.getText().toString().trim();
        String password = input_password.getText().toString();

        return FirebaseUtil.signIn(email, password);
    }
}