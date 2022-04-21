package com.group1.project3.repository;

import com.google.android.gms.tasks.Task;
import com.group1.project3.model.User;

public interface UserRepository {
    Task<Void> createUser(User user);

    Task<User> getUser(String userId);
}

