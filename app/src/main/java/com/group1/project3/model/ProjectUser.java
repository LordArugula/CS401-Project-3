package com.group1.project3.model;

public class ProjectUser {
    /**
     * The user id.
     */
    private String userId;
    /**
     * The user email.
     */
    private String email;
    /**
     * The role.
     */
    private String role;

    /**
     * Creates a project user from the user email and role.
     *
     * @param userId
     * @param email the user email.
     * @param role   the user role.
     */
    public ProjectUser(String userId, String email, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    /**
     * Creates a project user from the user email and role.
     *
     * @param userId
     * @param email the user email.
     * @param role   the user role.
     */
    public ProjectUser(String userId, String email, Role role) {
        this.userId = userId;
        this.email = email;
        this.role = role.name();
    }

    /**
     * Gets the user id.
     *
     * @return the user id.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId the user id.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the user email.
     *
     * @return the user email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user email.
     *
     * @param email the user email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user role.
     *
     * @return the user role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the user role.
     *
     * @param role the user role.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Sets the user role.
     *
     * @param role the user role.
     */
    public void setRole(Role role) {
        this.role = role.name();
    }
}
