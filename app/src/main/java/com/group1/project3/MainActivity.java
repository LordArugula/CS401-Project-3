package com.group1.project3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  //remember to change back

        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            /*Toast.makeText(this, currentUser.getEmail() + ' ' + currentUser.getUid(), Toast.LENGTH_SHORT)
                    .show();*/
            reload();
        }
    }

    public void login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void reload() {
//        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);
    }
}