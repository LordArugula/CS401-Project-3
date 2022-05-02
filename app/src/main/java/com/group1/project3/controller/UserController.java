package com.group1.project3.controller;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.group1.project3.model.Project;
import com.group1.project3.model.User;
import com.group1.project3.repository.UserRepository;

/**
 * The controller for the User.
 */
public class UserController {

    /**
     * The User repository.
     */
    private final UserRepository userRepository;

    /**
     * The current user.
     */
    private User currentUser;

    /**
     * Creates the controller from a repository.
     *
     * @param userRepository the user repository.
     */
    public UserController(UserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalArgumentException("userRepository cannot be null.");
        }

        this.userRepository = userRepository;
        loadCurrentUser().addOnSuccessListener(user -> currentUser = user);
    }

    /**
     * Registers the user account.
     *
     * @param user     the user.
     * @param password the password.
     * @return a {@link Task<Void>} representing the register request.
     */
    public Task<Void> register(@NonNull User user, String password) {
        return userRepository.registerUser(user.getEmail(), password)
                .onSuccessTask(userId -> {
                    currentUser = user;
                    user.setId(userId);
                    return userRepository.createUserProfile(user);
                });
    }

    /**
     * Loads the current user.
     *
     * @return the task representing the get user request.
     */
    public Task<User> loadCurrentUser() {
        return userRepository.getCurrentUser();
    }

    /**
     * Updates the user's password.
     *
     * @param oldPassword the old password.
     * @param newPassword the new password.
     * @return a update password request.
     */
    public Task<Void> updatePassword(String oldPassword, String newPassword) {
        return userRepository.updatePassword(currentUser.getEmail(), oldPassword, newPassword);
    }

    /**
     * Updates the user's profile.
     *
     * @param email         the email address.
     * @param username      the username.
     * @param first         the first name.
     * @param last          the last name.
     * @param profilePicUri the Uri for the profile picture.
     * @return the update profile request.
     */
    public Task<Void> updateProfile(String email, String username, String first, String last, Uri profilePicUri) {
        return userRepository.updateProfile(currentUser, email, username, first, last, profilePicUri);
    }
}
