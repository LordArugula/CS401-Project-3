package com.group1.project3.repository;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.group1.project3.model.User;
import com.group1.project3.util.FirebaseUtil;

import java.util.List;

public class FirestoreUserRepository implements UserRepository {

    private final CollectionReference userCollection;

    public FirestoreUserRepository() {
        userCollection = FirebaseUtil.getFirestore()
                .collection("users");
    }

    @Override
    public Task<String> registerUser(String email, String password) {
        FirebaseAuth auth = FirebaseUtil.getAuth();

        return auth.createUserWithEmailAndPassword(email, password)
                .onSuccessTask(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    assert user != null;
                    return Tasks.forResult(user.getUid());
                });
    }

    @Override
    public Task<Void> createUserProfile(User user) {
        return userCollection.document(user.getId())
                .set(user);
    }

    @Override
    public Task<User> getCurrentUser() {
        if (!FirebaseUtil.isSignedIn()) {
            return Tasks.forResult(null);
        }
        return getUser(FirebaseUtil.getAuth().getUid());
    }

    @Override
    public Task<User> getUser(String userId) {
        return userCollection.document(userId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        return task.getResult().toObject(User.class);
                    }
                    throw new Exception("No user with " + userId + "exists.");
                });
    }

    @Override
    public Task<QuerySnapshot> getUsers(List<String> userIds) {
        return userCollection.whereIn("id", userIds)
                .get();
    }

    @Override
    public Task<Void> updateUser(User user) {
        return userCollection.document(user.getId())
                .set(user);
    }

    @Override
    public Task<Void> updatePassword(String email, String password, String newPassword) {
        return FirebaseUtil.reauthenticate(email, password)
                .onSuccessTask(unused -> FirebaseUtil.updatePassword(password));
    }

    @Override
    public Task<Void> updateProfile(User user, String email, String username, String first, String last, Uri profilePicUri) {
        return FirebaseUtil.updateEmail(email)
                .onSuccessTask(unused -> FirebaseUtil.updateProfile(username, profilePicUri))
                .onSuccessTask(unused -> {
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setFirstName(first);
                    user.setLastName(last);
                    user.setProfilePicUri(profilePicUri == null ? "" : profilePicUri.toString());
                    return updateUser(user);
                });
    }
}
