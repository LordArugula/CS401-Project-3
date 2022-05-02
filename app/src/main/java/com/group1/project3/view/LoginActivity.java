package com.group1.project3.view;

import android.os.Bundle;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.group1.project3.R;
import com.group1.project3.util.FirebaseUtil;
import com.group1.project3.view.validator.LoginFormValidator;

/**
 * The Login Activity.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * The email EditText.
     */
    private EditText input_email;
    /**
     * The password EditText.
     */
    private EditText input_password;

    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState the saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input_email = findViewById(R.id.login_input_email);
        input_password = findViewById(R.id.login_input_password);

        Button button_signIn = findViewById(R.id.login_button_signUp);
        button_signIn.setOnClickListener(this::onClickSignInButton);
        button_signIn.setEnabled(false);

        TextWatcher textWatcher = new LoginFormValidator(input_email, input_password, button_signIn);
        input_email.addTextChangedListener(textWatcher);
        input_password.addTextChangedListener(textWatcher);

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
        String email = input_email.getText().toString().trim();
        String password = input_password.getText().toString();

        FirebaseUtil.signIn(email, password)
                .addOnSuccessListener(authResult -> finish())
                .addOnFailureListener(exception -> Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show());
    }

    /**
     * The options menu handler
     *
     * @param item the menu item.
     * @return based on the selected option.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}