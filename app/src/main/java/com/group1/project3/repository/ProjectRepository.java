package com.group1.project3.repository;

import com.google.android.gms.tasks.Task;
import com.group1.project3.model.Project;

public interface ProjectRepository {
    Task<Void> createProject(Project project);

    Task<Project> getProject(String projectId);
}

