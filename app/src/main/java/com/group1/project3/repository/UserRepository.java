package com.group1.project3.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.group1.project3.model.User;

import java.util.ArrayList;

/**
 * Interface for the User repository.
 */
public interface UserRepository {
    /**
     * Creates the user.
     *
     * @param user the user to create.
     * @return a {@link Task<Void>} representing the create user request.
     */
    Task<Void> createUser(User user);

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

    Task<QuerySnapshot> getUsers(ArrayList<String> userIds);
}

