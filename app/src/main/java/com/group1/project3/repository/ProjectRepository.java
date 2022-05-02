package com.group1.project3.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.group1.project3.model.Project;

import java.util.List;

/**
 * The interface for the project repository.
 */
public interface ProjectRepository {
    /**
     * Creates a project.
     *
     * @param project the project.
     * @return the create project request.
     */
    Task<Void> createProject(Project project);

    /**
     * Gets the project with the id.
     *
     * @param projectId the id.
     * @return the get project request.
     */
    Task<Project> getProject(String projectId);

    /**
     * Gets all the projects with matching ids.
     *
     * @param projectIds the ids.
     * @return the get projects request.
     */
    Task<QuerySnapshot> getProjects(List<String> projectIds);

    /**
     * Updates the project.
     *
     * @param project the project.
     * @return the update project request.
     */
    Task<Void> updateProject(Project project);

    /**
     * Deletes the project.
     *
     * @param project the project.
     * @return the delete project request.
     */
    Task<Void> deleteProject(Project project);
}

