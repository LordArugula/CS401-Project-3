package com.group1.project3.controller;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.group1.project3.model.User;
import com.group1.project3.repository.UserRepository;

public class ProfileController {

    private final UserRepository userRepository;

    private User currentUser;

    public ProfileController(UserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalArgumentException("userRepository cannot be null.");
        }

        this.userRepository = userRepository;
        getCurrentUser()
                .addOnSuccessListener(user -> currentUser = user);
    }

    public Task<Void> register(@NonNull User user, String password) {
        return userRepository.registerUser(user.getEmail(), password)
                .onSuccessTask(authResult -> {
                    assert authResult.getUser() != null;

                    currentUser = user;
                    user.setId(authResult.getUser().getUid());
                    return userRepository.createUserProfile(user);
                });
    }

    public Task<User> getCurrentUser() {
        if (currentUser != null) {
            return Tasks.forResult(currentUser);
        }

        return userRepository.getCurrentUser();
    }

    public Task<Void> updatePassword(String oldPassword, String newPassword) {
        return userRepository.updatePassword(currentUser.getEmail(), oldPassword, newPassword);
    }

    public Task<Void> updateProfile(String email, String username, String first, String last, Uri profilePicUri) {
        return userRepository.updateProfile(email, username, profilePicUri);
    }

    public Task<Void> updateUser(User user) {
        return userRepository.updateUser(user);
    }
}
