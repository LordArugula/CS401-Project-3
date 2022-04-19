package com.group1.project3.model;

/**
 * Roles determine what permissions a {@link User} has in a {@link Project}.
 */
public enum Role {
    /**
     * Has no permissions.
     */
    None,
    /**
     * Has read-only permissions.
     */
    Viewer,
    /**
     * Has read and write permissions.
     * Cannot add or remove users from the project.
     */
    Editor,
    /**
     * Has full permissions.
     */
    Owner,
}
