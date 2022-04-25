package com.group1.project3.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.group1.project3.model.Project;

import java.util.List;

public interface ProjectRepository {
    Task<Void> createProject(Project project);

    Task<Project> getProject(String projectId);

    Task<QuerySnapshot> getProjects(List<String> projectIds);
}

