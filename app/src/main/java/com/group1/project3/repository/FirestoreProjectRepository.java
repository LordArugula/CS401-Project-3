package com.group1.project3.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.group1.project3.model.Project;
import com.group1.project3.util.FirebaseUtil;

import java.util.List;

/**
 * The project repository that connects to the Cloud Firestore.
 */
public class FirestoreProjectRepository implements ProjectRepository {

    /**
     * The project collection.
     */
    private final CollectionReference projectCollection;

    /**
     * Creates the repository.
     */
    public FirestoreProjectRepository() {
        projectCollection = FirebaseUtil.getFirestore()
                .collection("projects");
    }

    /**
     * Creates the project.
     *
     * @param project the project.
     * @return the create project request.
     */
    @Override
    public Task<Void> createProject(@NonNull Project project) {
        return projectCollection.add(project)
                .onSuccessTask(documentReference -> {
                    project.setId(documentReference.getId());
                    return documentReference.set(project);
                });
    }

    /**
     * Gets the project with the id.
     *
     * @param projectId the id.
     * @return the get project request.
     */
    @Override
    public Task<Project> getProject(String projectId) {
        return projectCollection.document(projectId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return task.getResult().toObject(Project.class);
                    }
                    return null;
                });

    }

    /**
     * Gets all the projects with matching ids.
     *
     * @param projectIds the ids.
     * @return the get projects request.
     */
    @Override
    public Task<QuerySnapshot> getProjects(@NonNull List<String> projectIds) {
        if (projectIds.isEmpty()) {
            return Tasks.forException(new IllegalArgumentException("projectIds was empty."));
        }
        return projectCollection.whereIn("id", projectIds)
                .get();
    }

    /**
     * Updates the project.
     *
     * @param project the project.
     * @return the update project request.
     */
    @Override
    public Task<Void> updateProject(Project project) {
        return projectCollection.document(project.getId())
                .set(project);
    }

    /**
     * Deletes the project.
     *
     * @param project the project.
     * @return the delete project request.
     */
    @Override
    public Task<Void> deleteProject(Project project) {
        return projectCollection.document(project.getId())
                .delete();
    }
}