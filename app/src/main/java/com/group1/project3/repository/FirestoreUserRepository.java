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

/**
 * The user repository that connects to the Cloud Firestore.
 */
public class FirestoreUserRepository implements UserRepository {

    /**
     * The user collection.
     */
    private final CollectionReference userCollection;

    /**
     * Creates the repository.
     */
    public FirestoreUserRepository() {
        userCollection = FirebaseUtil.getFirestore()
                .collection("users");
    }

    /**
     * Registers an account and returns the new account's user id.
     *
     * @param email    The user email address.
     * @param password The user password.
     * @return the register request.
     */
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

    /**
     * Creates the user profile.
     *
     * @param user the user to create.
     * @return the create user profile request.
     */
    @Override
    public Task<Void> createUserProfile(User user) {
        return userCollection.document(user.getId())
                .set(user);
    }

    /**
     * Gets the currently signed in user.
     *
     * @return the get user request.
     */
    @Override
    public Task<User> getCurrentUser() {
        if (!FirebaseUtil.isSignedIn()) {
            return Tasks.forResult(null);
        }
        return getUser(FirebaseUtil.getAuth().getUid());
    }

    /**
     * Gets the user with the id
     *
     * @param userId the user id.
     * @return the get user request.
     */
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

    /**
     * Gets all the users with a matching id.
     *
     * @param userIds The user ids to match with.
     * @return the get users request.
     */
    @Override
    public Task<QuerySnapshot> getUsers(List<String> userIds) {
        return userCollection.whereIn("id", userIds)
                .get();
    }

    /**
     * Updates the user.
     *
     * @param user the user to update.
     * @return the update user request.
     */
    @Override
    public Task<Void> updateUser(User user) {
        return userCollection.document(user.getId())
                .set(user);
    }

    /**
     * Updates the current user's password.
     *
     * @param email       the user email address.
     * @param password    the user password
     * @param newPassword the user's new password.
     * @return the update password request.
     */
    @Override
    public Task<Void> updatePassword(String email, String password, String newPassword) {
        return FirebaseUtil.reauthenticate(email, password)
                .onSuccessTask(unused -> FirebaseUtil.updatePassword(password));
    }

    /**
     * Updates the user's profile.
     *
     * @param user          the user.
     * @param email         the user email address.
     * @param username      the username.
     * @param first         the user first name.
     * @param last          the user last name.
     * @param profilePicUri the user profile picture {@link Uri}.
     * @return the update user request.
     */
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
