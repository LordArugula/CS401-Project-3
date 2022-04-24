package com.group1.project3.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.group1.project3.model.Project;
import com.group1.project3.util.FirebaseUtil;

import java.util.HashMap;
import java.util.Map;

public class FirestoreProjectRepository implements ProjectRepository {

    private final CollectionReference projectCollection;

    public FirestoreProjectRepository() {
        projectCollection = FirebaseUtil.getFirestore()
                .collection("projects");
    }

    @Override
    public Task<Void> createProject(@NonNull Project project) {
        return projectCollection.add(project)
                .onSuccessTask(documentReference -> {
                    project.setId(documentReference.getId());
                    return documentReference.set(project);
                });
    }

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
}