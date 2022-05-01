package com.group1.project3.view;

import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.group1.project3.R;
import com.group1.project3.controller.UserController;
import com.group1.project3.model.User;
import com.group1.project3.repository.FirestoreUserRepository;
import com.group1.project3.view.validator.RegisterFormValidator;

public class RegisterActivity extends AppCompatActivity {

    private EditText input_firstName;
    private EditText input_lastName;
    private EditText input_username;
    private EditText input_email;
    private EditText input_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        input_firstName = findViewById(R.id.register_input_firstName);
        input_lastName = findViewById(R.id.register_input_lastName);
        input_username = findViewById(R.id.register_input_username);
        input_email = findViewById(R.id.register_input_email);
        input_password = findViewById(R.id.register_input_password);
        EditText input_confirmPassword = findViewById(R.id.register_input_confirm_password);

        Button button_signUp = findViewById(R.id.register_button_signUp);
        button_signUp.setOnClickListener(this::onClickSignUpButton);
        button_signUp.setEnabled(false);

        TextWatcher textWatcher = new RegisterFormValidator(input_firstName, input_lastName, input_username, input_email, input_password, input_confirmPassword, button_signUp);
        input_firstName.addTextChangedListener(textWatcher);
        input_lastName.addTextChangedListener(textWatcher);
        input_username.addTextChangedListener(textWatcher);
        input_email.addTextChangedListener(textWatcher);
        input_password.addTextChangedListener(textWatcher);
        input_confirmPassword.addTextChangedListener(textWatcher);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.register_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * OnClick callback for the sign up button.
     * Signs the user up.
     *
     * @param view the button that was clicked on.
     */
    private void onClickSignUpButton(View view) {
        String email = input_email.getText().toString().trim();
        String password = input_password.getText().toString();
        String firstName = input_firstName.getText().toString().trim();
        String lastName = input_lastName.getText().toString().trim();
        String username = input_username.getText().toString().trim();

        User user = new User("", firstName, lastName, username, email);
        UserController profileController = new UserController(new FirestoreUserRepository());
        profileController.register(user, password)
                .addOnSuccessListener(unused -> {
                    Log.d("Register", "Registered Account");
                    finish();
                })
                .addOnFailureListener(exception -> {
                    Log.e("Register", exception.getMessage());
                    Toast.makeText(RegisterActivity.this, exception.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                });
    }
}