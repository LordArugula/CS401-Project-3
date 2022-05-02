package com.group1.project3.controller;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.group1.project3.model.Project;
import com.group1.project3.model.User;
import com.group1.project3.repository.UserRepository;

public class UserController {

    private final UserRepository userRepository;

    private User currentUser;

    public UserController(UserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalArgumentException("userRepository cannot be null.");
        }

        this.userRepository = userRepository;
        loadCurrentUser().addOnSuccessListener(user -> currentUser = user);
    }

    public Task<Void> register(@NonNull User user, String password) {
        return userRepository.registerUser(user.getEmail(), password)
                .onSuccessTask(userId -> {
                    currentUser = user;
                    user.setId(userId);
                    return userRepository.createUserProfile(user);
                });
    }

    public Task<User> loadCurrentUser() {
        return userRepository.getCurrentUser();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Task<Void> updatePassword(String oldPassword, String newPassword) {
        return userRepository.updatePassword(currentUser.getEmail(), oldPassword, newPassword);
    }

    public Task<Void> updateProfile(String email, String username, String first, String last, Uri profilePicUri) {
        return userRepository.updateProfile(currentUser, email, username, first, last, profilePicUri);
    }

    public Task<Void> updateUser(User user) {
        return userRepository.updateUser(user);
    }

    public Task<Void> addProject(Project project) {
        currentUser.addProject(project.getId());
        return userRepository.updateUser(currentUser);
    }
}
