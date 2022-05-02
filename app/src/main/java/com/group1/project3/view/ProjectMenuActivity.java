package com.group1.project3.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.group1.project3.R;
import com.group1.project3.adapter.ProjectAdapter;
import com.group1.project3.model.Pipeline;
import com.group1.project3.model.Project;
import com.group1.project3.model.Tag;
import com.group1.project3.model.User;
import com.group1.project3.repository.FirestoreProjectRepository;
import com.group1.project3.repository.FirestoreUserRepository;
import com.group1.project3.repository.ProjectRepository;
import com.group1.project3.repository.UserRepository;
import com.group1.project3.util.FirebaseUtil;
import com.group1.project3.view.dialog.EditProjectDialogBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * The Project Menu Activity.
 */
public class ProjectMenuActivity extends AppCompatActivity {

    /**
     * The project repository.
     */
    private ProjectRepository projectRepository;
    /**
     * The projects.
     */
    private List<Project> projects;

    /**
     * The project adapter.
     */
    private ProjectAdapter projectAdapter;

    /**
     * The user repository.
     */
    private UserRepository userRepository;
    /**
     * The user.
     */
    private User user;

    /**
     * Called when the activity starts.
     */
    @Override
    protected void onStart() {
        super.onStart();
        loadCurrentUser()
                .addOnSuccessListener(this::loadProjectsForUser)
                .addOnFailureListener(exception -> {
                    Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    signOut();
                });
    }

    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState the saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.ic_baseline_person_24);

        actionBar.setTitle("Projects");

        projectRepository = new FirestoreProjectRepository();
        projects = new ArrayList<>();

        userRepository = new FirestoreUserRepository();
        loadCurrentUser()
                .addOnSuccessListener(this::loadProjectsForUser)
                .addOnFailureListener(exception -> {
                    Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    signOut();
                });

        projectAdapter = new ProjectAdapter(projects, this::launchProjectActivity);

        RecyclerView projectRecyclerView = findViewById(R.id.project_menu_recyclerView_projects);
        projectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        projectRecyclerView.setAdapter(projectAdapter);

        FloatingActionButton fab_createProject = findViewById(R.id.project_menu_fab_createProject);
        fab_createProject.setOnClickListener(view -> openCreateProjectDialog());
    }

    /**
     * Loads the current user.
     *
     * @return the get user request.
     */
    private Task<User> loadCurrentUser() {
        return userRepository.getCurrentUser();
    }

    /**
     * Loads the projects for the user.
     *
     * @param user the user.
     */
    private void loadProjectsForUser(User user) {
        if (user == null) {
            signOut();
        }

        this.user = user;
        projects.clear();
        if (user.getProjectIds().isEmpty()) {
            projectAdapter.notifyDataSetChanged();
        } else {
            projectRepository.getProjects(user.getProjectIds())
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            projects.add(snapshot.toObject(Project.class));
                        }
                        projectAdapter.notifyDataSetChanged();
                    });
        }
    }

    /**
     * Launches the project activity for the project.
     *
     * @param project the project.
     */
    private void launchProjectActivity(Project project) {
        Intent intent = new Intent(this, ProjectActivity.class);
        intent.putExtra("projectId", project.getId());
        startActivity(intent);
    }

    /**
     * Creates the options menu.
     *
     * @param menu the menu.
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_project_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_open_account_profile:
                launchProfileActivity();
                break;
            case R.id.menu_sign_out:
                signOut();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Signs the user out.
     */
    private void signOut() {
        FirebaseUtil.signOut();
        launchMainActivity();
    }

    /**
     * Launches the Main Activity.
     */
    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Launches the profile activity.
     */
    private void launchProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    /**
     * Opens the CreateProjectDialog.
     */
    private void openCreateProjectDialog() {
        new EditProjectDialogBuilder(this)
                .setTitle("New Project")
                .setProject(getDefaultProject())
                .setPositiveButton("Create Project", (dialogInterface, i, project) -> createProject(project))
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    /**
     * Creates the project.
     *
     * @param project the project.
     */
    private void createProject(Project project) {
        project.setOwnerId(user.getId());
        projectRepository.createProject(project)
                .addOnSuccessListener(unused -> {
                    projects.add(project);
                    user.addProject(project.getId());
                    userRepository.updateUser(user);
                    projectAdapter.notifyDataSetChanged();
                });
    }

    /**
     * Creates a default project.
     *
     * @return the created project.
     */
    private Project getDefaultProject() {
        Project project = new Project();
        project.setColor(getColor(R.color.project_defaultColor));
        project.addPipeline(new Pipeline("In Progress"));
        project.addPipeline(new Pipeline("Review"));
        project.addPipeline(new Pipeline("Backlog"));
        project.addPipeline(new Pipeline("Done"));

        project.addTag(new Tag("Bugs", getColor(R.color.bugs)));
        project.addTag(new Tag("Sprint", getColor(R.color.sprint)));
        project.addTag(new Tag("Documentation", getColor(R.color.documentation)));

        return project;
    }
}