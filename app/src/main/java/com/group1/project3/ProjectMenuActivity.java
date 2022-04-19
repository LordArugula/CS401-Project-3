package com.group1.project3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.group1.project3.adapter.ProjectAdapter;
import com.group1.project3.model.Project;
import com.group1.project3.model.User;
import com.group1.project3.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class ProjectMenuActivity extends AppCompatActivity {

    private RecyclerView projectRecyclerView;
    private ProjectAdapter projectAdapter;

    private List<Project> projects;

    public ProjectMenuActivity() {
        projects = new ArrayList<>();
        Project projectA = new Project("id", "test", "123");
        projectA.setColor(Color.valueOf(Color.parseColor("#FF0000")));
        projects.add(projectA);
        Project projectB = new Project("id", "Hello", "123");
        projectB.setColor(Color.valueOf(Color.parseColor("#00FF00")));
        projects.add(projectB);
        Project projectC = new Project("id", "World", "123");
        projectC.setColor(Color.valueOf(Color.parseColor("#0000FF")));
        projects.add(projectC);
        Project projectD = new Project("id", "This is a Project", "123");
        projectD.setColor(Color.valueOf(Color.parseColor("#00FFFF")));
        projects.add(projectD);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_menu);

        projectAdapter = new ProjectAdapter(projects, (Project project) -> {
            launchProjectActivity(project);
        });

        projectRecyclerView = findViewById(R.id.project_menu_project_recyclerView);
        projectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        projectRecyclerView.setAdapter(projectAdapter);
    }

    private void launchProjectActivity(Project project) {
        Intent intent = new Intent(this, ProjectActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_open_account_profile:
                launchProfileActivity();
                break;
            case R.id.menu_sign_out:
                FirebaseUtil.signOut();
                launchMainActivity();
                break;
            case R.id.menu_card:
                Intent intent = new Intent(this, CardActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void launchProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void openCreateProjectDialog(View view) {
        Intent intent = new Intent(this, CreateProjectActivity.class);
        startActivity(intent);
    }
}