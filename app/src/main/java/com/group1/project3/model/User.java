package com.group1.project3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * user model
 */
public class User {

    /**
     * The projects this user has access to.
     */
    private final List<String> projectIds;

    /**
     * user's id
     */
    private String id;

    /**
     * user's first name
     */
    private String firstName;

    /**
     * user's last name
     */
    private String lastName;

    /**
     * user's display name
     */
    private String username;

    /**
     * user's email address
     */
    private String email;

    /**
     * uri to the user's profile picture
     */
    private String profilePic;

    /**
     * Creates an empty user.
     */
    public User() {
        projectIds = new ArrayList<>();
    }

    /**
     * Creates a user with an id, first and last name, username, and email address.
     *
     * @param id       the user id
     * @param first    the user first name
     * @param last     the user last name
     * @param username the username
     * @param email    the email address
     */
    public User(String id, String first, String last, String username, String email) {
        this();
        this.id = id;
        this.firstName = first;
        this.lastName = last;
        this.username = username;
        this.email = email;
        this.profilePic = null;
    }

    /**
     * returns the user's id
     *
     * @return user id
     */
    public String getId() {
        return id;
    }

    /**
     * sets the user's id
     *
     * @param id the user id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * returns the user's first name
     *
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * sets the user's first name
     *
     * @param firstName first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * returns the user's last name
     *
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * sets the user's last name
     *
     * @param lastName last name of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * returns the user's username
     *
     * @return user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * sets the username of user
     *
     * @param username the user's updated username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * returns the user's email address
     *
     * @return user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * sets the user's email address
     *
     * @param email the user's updated email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * returns uri to user's profile picture
     *
     * @return uri of profile picture
     */
    public String getProfilePicUri() {
        return profilePic;
    }

    /**
     * sets the user's profile picture
     *
     * @param profilePic string of url pointing to image
     */
    public void setProfilePicUri(String profilePic) {
        this.profilePic = profilePic;
    }

    /**
     * returns a read-only set of this user's project ids.
     *
     * @return a read-only set of project ids
     */
    public List<String> getProjectIds() {
        return Collections.unmodifiableList(projectIds);
    }

    /**
     * adds a project to the list that the user can access
     *
     * @param projectId the project id
     */
    public boolean addProject(String projectId) {
        return projectIds.add(projectId);
    }

    /**
     * removes a project from the list that the user can access
     *
     * @param projectId the project id
     */
    public boolean removeProject(String projectId) {
        return projectIds.remove(projectId);
    }

    /**
     * Removes a project from the list that the user can access
     *
     * @param projectId the project id
     * @return true if this user has access to the project.
     */
    public boolean hasProject(String projectId) {
        return projectIds.contains(projectId);
    }
}
