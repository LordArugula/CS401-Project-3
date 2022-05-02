package com.group1.project3.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.group1.project3.R;
import com.group1.project3.adapter.PipelineAdapter;
import com.group1.project3.model.Card;
import com.group1.project3.model.Pipeline;
import com.group1.project3.model.Project;
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

        if (getIntent().hasExtra("projectId")) {
            String projectId = getIntent().getStringExtra("projectId");
            projectRepository = new FirestoreProjectRepository();
            projectRepository.getProject(projectId)
                    .addOnSuccessListener(this::onLoadProject);
        }
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
                openEditProjectDialog();
                return true;
            case R.id.menu_project_addPipeline:
                addPipeline();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Adds a pipeline to the project.
     */
    private void addPipeline() {
        Pipeline pipeline = new Pipeline();
        new EditPipelineDialogBuilder(this)
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

        pipelineAdapter = new PipelineAdapter(project, this::onClickCreateCardButton, this::onClickPipelineMenu);

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