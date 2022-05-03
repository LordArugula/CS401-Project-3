package com.group1.project3.view;

import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.group1.project3.R;
import com.group1.project3.adapter.PipelineAdapter;
import com.group1.project3.adapter.ProjectUserAdapter;
import com.group1.project3.model.Card;
import com.group1.project3.model.Pipeline;
import com.group1.project3.model.Project;
import com.group1.project3.model.ProjectUser;
import com.group1.project3.model.Role;
import com.group1.project3.model.User;
import com.group1.project3.repository.FirestoreProjectRepository;
import com.group1.project3.repository.FirestoreUserRepository;
import com.group1.project3.repository.ProjectRepository;
import com.group1.project3.repository.UserRepository;
import com.group1.project3.view.dialog.EditCardDialogBuilder;
import com.group1.project3.view.dialog.EditPipelineDialogBuilder;
import com.group1.project3.view.dialog.EditProjectDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The Project Activity.
 */
public class ProjectActivity extends AppCompatActivity {

    /**
     * The pipeline adapter.
     */
    private PipelineAdapter pipelineAdapter;

    /**
     * The project.
     */
    private Project project;
    /**
     * The project repository.
     */
    private ProjectRepository projectRepository;

    /**
     * The user repository.
     */
    private UserRepository userRepository;

    /**
     * The project user.
     */
    private ProjectUser user;

    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState the saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        userRepository = new FirestoreUserRepository();
        userRepository.getCurrentUser()
                .addOnSuccessListener(user -> {
                    this.user = new ProjectUser(user.getId(), user.getEmail(), Role.Viewer);

                    if (getIntent().hasExtra("projectId")) {
                        String projectId = getIntent().getStringExtra("projectId");
                        projectRepository = new FirestoreProjectRepository();
                        projectRepository.getProject(projectId)
                                .addOnSuccessListener(this::onLoadProject);
                    }
                });
    }

    /**
     * Creates the options menu.
     *
     * @param menu the menu.
     * @return true.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_project, menu);
        return true;
    }

    /**
     * Handles the options menu.
     *
     * @param item the menu item.
     * @return based on the option selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_project_editProject:
                // Handles the edit project menu option
                switch (Role.valueOf(user.getRole())) {
                    case None:
                    case Viewer:
                        Toast.makeText(this, "You do not have permission to edit this project.", Toast.LENGTH_SHORT).show();
                        break;
                    case Editor:
                    case Admin:
                        openEditProjectDialog();
                        break;
                }
                return true;
            case R.id.menu_project_addPipeline:
                // Handles the add pipeline menu option
                switch (Role.valueOf(user.getRole())) {
                    case None:
                    case Viewer:
                        Toast.makeText(this, "You do not have permission to edit this project.", Toast.LENGTH_SHORT).show();
                        break;
                    case Editor:
                    case Admin:
                        addPipeline();
                        break;
                }
                return true;
            case R.id.menu_project_editUsers:
                // Handles the edit users menu option.
                switch (Role.valueOf(user.getRole())) {
                    case None:
                    case Viewer:
                        Toast.makeText(this, "You do not have permission to edit this project.", Toast.LENGTH_SHORT).show();
                        break;
                    case Editor:
                    case Admin:
                        openEditUsersDialog();
                        break;
                }
                return true;
            case android.R.id.home:
                // handles the home button.
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Opens the EditUsersDialog.
     */
    private void openEditUsersDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle("Edit Project Users")
                .setView(R.layout.dialog_edit_project_users)
                .setNegativeButton("Close", (dialogInterface, i) -> dialogInterface.dismiss());

        // Gets the project user ids, emails, and roles.
        List<ProjectUser> projectUsers = new ArrayList<>();
        List<String> editorIds = new ArrayList<>(project.getEditors().keySet());
        ProjectUserAdapter projectUserAdapter = new ProjectUserAdapter(project.getId(), projectUsers);
        userRepository.getUsers(editorIds)
                .addOnSuccessListener(query -> {
                    for (DocumentSnapshot snapshot : query.getDocuments()) {
                        snapshot.getReference().update("projectIds", FieldValue.arrayUnion(project.getId()));
                        String email = snapshot.getString("email");
                        String id = snapshot.getString("id");
                        projectUsers.add(new ProjectUser(id, email, project.getUserRole(id)));

                    }
                });
        // Hook up UI and events.
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialogInterface -> {
            List<String> roles = new ArrayList<>();
            roles.add(Role.Editor.name());
            roles.add(Role.Viewer.name());

            EditText input_email = alertDialog.findViewById(R.id.dialog_edit_project_users_input_email);

            Spinner spinner_role = alertDialog.findViewById(R.id.dialog_edit_project_users_spinner_role);
            spinner_role.setAdapter(new ArrayAdapter(ProjectActivity.this, android.R.layout.simple_spinner_item, roles));
            spinner_role.setPrompt("Editor");

            RecyclerView recyclerView_users = alertDialog.findViewById(R.id.dialog_edit_project_users_recyclerView_Users);
            recyclerView_users.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            recyclerView_users.setAdapter(projectUserAdapter);

            Button button_add = alertDialog.findViewById(R.id.dialog_edit_project_users_button_add);
            button_add.setOnClickListener(view -> {
                // Update project and users
                String email = input_email.getText().toString().trim();
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    userRepository.findUserIdFromEmail(email)
                            .addOnSuccessListener(id -> {
                                Role role = Role.valueOf((String) spinner_role.getSelectedItem());
                                projectUsers.add(new ProjectUser(id, email, role));
                                projectUserAdapter.notifyItemInserted(projectUsers.size() - 1);
                                project.updateOrAddEditor(id, role);
                                projectRepository.updateProject(project);
                            })
                            .addOnFailureListener(exception -> {
                                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
                input_email.getText().clear();
            });
        });
        alertDialog.show();
    }

    /**
     * Adds a pipeline to the project.
     */
    private void addPipeline() {
        Pipeline pipeline = new Pipeline();
        new EditPipelineDialogBuilder(this)
                .setPipeline(pipeline)
                .setPositiveButton("Confirm", (dialogInterface, i, name) -> {
                    pipeline.setName(name);
                    project.addPipeline(pipeline);
                    pipelineAdapter.notifyDataSetChanged();
                    projectRepository.updateProject(project);
                })
                .setTitle("Rename Pipeline")
                .setView(R.layout.dialog_edit_pipeline)
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    /**
     * Opens the EditProjectDialog.
     */
    private void openEditProjectDialog() {
        new EditProjectDialogBuilder(this)
                .setTitle("Edit Project")
                .setProject(project)
                .setDeleteButton(true, (dialogInterface, i, project) -> {
                    // Updates the project and user
                    dialogInterface.dismiss();
                    Map<String, Role> editors = project.getEditors();
                    userRepository.getUsers(new ArrayList<>(editors.keySet()))
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                    User user = snapshot.toObject(User.class);
                                    user.removeProject(project.getId());
                                    userRepository.updateUser(user);
                                }
                            });

                    projectRepository.deleteProject(project);
                    finish();
                })
                .setPositiveButton("Save Project", (dialogInterface, i, project) -> onSaveProject(project))
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    /**
     * Saves the project.
     *
     * @param project the project.
     */
    private void onSaveProject(Project project) {
        getSupportActionBar().setTitle(project.getName());

        projectRepository.updateProject(project)
                .addOnFailureListener(exception -> {
                    Toast.makeText(this, "Failed to save project\n" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Opens the CreateCardDialog.
     *
     * @param pipeline the pipeline to add the card to.
     */
    private void openCreateCardDialog(Pipeline pipeline) {
        new EditCardDialogBuilder(this)
                .setTitle("Create card")
                .setView(R.layout.dialog_edit_card)
                .setCard(new Card(UUID.randomUUID().toString()), pipeline, project)
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("Save", (dialogInterface, i, card) -> {
                    pipeline.addCard(card);
                    pipelineAdapter.notifyDataSetChanged();
                    projectRepository.updateProject(project);
                })
                .show();
    }

    /**
     * Loads the project
     *
     * @param project the project.
     */
    private void onLoadProject(Project project) {
        this.project = project;

        getSupportActionBar().setTitle(project.getName());
        user.setRole(project.getUserRole(user.getUserId()));
        pipelineAdapter = new PipelineAdapter(project, user, this::onClickCreateCardButton, this::onClickPipelineMenu);

        RecyclerView pipelineRecyclerView = findViewById(R.id.project_recyclerView_cards);
        pipelineRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        pipelineRecyclerView.setAdapter(pipelineAdapter);
    }

    /**
     * Opens the CreateCardDialog.
     *
     * @param view     the create card button.
     * @param position the pipeline index.
     */
    private void onClickCreateCardButton(View view, int position) {
        openCreateCardDialog(project.getPipelines().get(position));
    }

    /**
     * Opens the pipeline options menu.
     *
     * @param view     the pipeline menu button.
     * @param position the pipeline index.
     */
    private void onClickPipelineMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);

        Pipeline pipeline = project.getPipelines().get(position);
        popupMenu.getMenuInflater().inflate(R.menu.menu_pipeline_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_pipeline_rename:
                    AlertDialog alertDialog = new EditPipelineDialogBuilder(this)
                            .setPositiveButton("Confirm", (dialogInterface, i, name) -> {
                                pipeline.setName(name);
                                pipelineAdapter.notifyDataSetChanged();
                                projectRepository.updateProject(project);
                            })
                            .setTitle("Rename Pipeline")
                            .setPipeline(pipeline)
                            .setView(R.layout.dialog_edit_pipeline)
                            .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                            .create();
                    alertDialog.show();
                    return true;
                case R.id.menu_pipeline_delete:
                    deletePipeline(position);
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    /**
     * Deletes the pipeline.
     *
     * @param position the index of the pipeline.
     */
    private void deletePipeline(int position) {
        project.removePipeline(project.getPipelines().get(position));
        pipelineAdapter.notifyDataSetChanged();
        projectRepository.updateProject(project);
    }
}