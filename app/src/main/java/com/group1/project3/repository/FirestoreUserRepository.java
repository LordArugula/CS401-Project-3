package com.group1.project3.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.group1.project3.model.User;
import com.group1.project3.util.FirebaseUtil;

import java.util.ArrayList;

public class FirestoreUserRepository implements UserRepository {

    private final CollectionReference userCollection;

    public FirestoreUserRepository() {
        userCollection = FirebaseUtil.getFirestore()
                .collection("users");
    }

    @Override
    public Task<Void> createUser(User user) {
        return userCollection.document(user.getId())
                .set(user);
    }

    @Override
    public Task<User> getUser(String userId) {
        return userCollection.document(userId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return task.getResult().toObject(User.class);
                    }
                    return null;
                });
    }

    @Override
    public Task<Void> updateUser(User user) {
        return userCollection.document(user.getId())
                .set(user);
    }

    @Override
    public Task<QuerySnapshot> getUsers(ArrayList<String> userIds) {
        return userCollection.whereIn("id", userIds)
                .get();
    }
}
