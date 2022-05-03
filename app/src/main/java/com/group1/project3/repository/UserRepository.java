package com.group1.project3.repository;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.group1.project3.model.User;

import java.util.List;

/**
 * Interface for the User repository.
 */
public interface UserRepository {

    /**
     * Registers the user account with email and password.
     *
     * @param email    The user email address.
     * @param password The user password.
     * @return a {@link Task<String>} representing the register request that
     * returns the registered user id.
     */
    Task<String> registerUser(String email, String password);

    /**
     * Updates the user's password.
     *
     * @param email       the user email address.
     * @param oldPassword the user's old password.
     * @param newPassword the user's new password.
     * @return a {@link Task<Void>} representing the change password request.
     */
    Task<Void> updatePassword(String email, String oldPassword, String newPassword);

    /**
     * Creates the user.
     *
     * @param user the user to create.
     * @return a {@link Task<Void>} representing the create user request.
     */
    Task<Void> createUserProfile(User user);

    /**
     * Gets the current user.
     *
     * @return the current user.
     */
    Task<User> getCurrentUser();

    /**
     * Gets the user with id userId.
     *
     * @param userId the user id.
     * @return a {@link Task<Void>} representing the get user request.
     */
    Task<User> getUser(String userId);

    /**
     * Updates the user.
     *
     * @param user the user to update.
     * @return a {@link Task<Void>} representing the update user request.
     */
    Task<Void> updateUser(User user);

    /**
     * Gets a query of all users with matching user ids.
     *
     * @param userIds The user ids to match with.
     * @return a query of users with matching user ids.
     */
    Task<QuerySnapshot> getUsers(List<String> userIds);

    /**
     * Updates the user profile.
     *
     * @param email         the user email address.
     * @param username      the username.
     * @param profilePicUri the user profile picture {@link Uri}.
     * @return a {@link Task<Void>} representing the change profile request.
     */
    Task<Void> updateProfile(User user, String email, String username, String first, String last, Uri profilePicUri);

    /**
     * Finds the user id who has the email address.
     *
     * @param email the email address.
     * @return the find user id request.
     */
    Task<String> findUserIdFromEmail(String email);
}

