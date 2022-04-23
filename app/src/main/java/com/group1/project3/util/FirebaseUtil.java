package com.group1.project3.util;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group1.project3.BuildConfig;

/**
 * Utility class for initializing Firebase services and connecting them to
 * the Firebase Emulator Suite if necessary.
 */
public final class FirebaseUtil {

    /**
     * Use emulators only in debug builds
     **/
    private static final boolean sUseEmulators = BuildConfig.DEBUG;

    /**
     * The FirebaseFirestore instance.
     */
    private static FirebaseFirestore FIRESTORE;

    /**
     * The FirebaseAuth instance.
     */
    private static FirebaseAuth AUTH;

    private static String TAG = "FirebaseUtil";

    /**
     * Private constructor to prevent instantiating this util class.
     */
    private FirebaseUtil() {
        // Empty
    }

    /**
     * Gets the Firestore instance.
     *
     * @return the Firestore instance.
     */
    public static FirebaseFirestore getFirestore() {
        if (FIRESTORE == null) {
            FIRESTORE = FirebaseFirestore.getInstance();

            // Connect to the Cloud Firestore emulator when appropriate. The host '10.0.2.2' is a
            // special IP address to let the Android emulator connect to 'localhost'.
            if (sUseEmulators) {
                FIRESTORE.useEmulator("10.0.2.2", 8080);
            }
        }

        return FIRESTORE;
    }

    /**
     * Gets the FirebaseAuth instance.
     *
     * @return the FirebaseAuth instance.
     */
    public static FirebaseAuth getAuth() {
        if (AUTH == null) {
            AUTH = FirebaseAuth.getInstance();

            // Connect to the Firebase Auth emulator when appropriate. The host '10.0.2.2' is a
            // special IP address to let the Android emulator connect to 'localhost'.
            if (sUseEmulators) {
                AUTH.useEmulator("10.0.2.2", 9099);
            }
        }

        return AUTH;
    }

    /**
     * Checks if the user is signed in.
     *
     * @return True if the user is signed in.
     */
    public static boolean isSignedIn() {
        return getAuth().getCurrentUser() != null;
    }

    /**
     * Signs the user out.
     */
    public static void signOut() {
        getAuth().signOut();
    }

    /**
     * Signs the user in.
     *
     * @param email    the user's email address.
     * @param password the user's password.
     * @return A {@link Task<AuthResult>} representing the sign in request.
     */
    public static Task<AuthResult> signIn(String email, String password) {
        return getAuth().signInWithEmailAndPassword(email, password);
    }

    /**
     * Updates the user's profile.
     *
     * @param username the username.
     * @param photoUri the photo {@link Uri}.
     * @return a {@link Task<Void>} representing the profile change request.
     */
    public static Task<Void> updateProfile(String username, Uri photoUri) {
        UserProfileChangeRequest.Builder changeRequestBuilder = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(photoUri);

        return getAuth().getCurrentUser()
                .updateProfile(changeRequestBuilder.build());
    }

    /**
     * Updates the user's password.
     *
     * @param password the password.
     * @return a {@link Task<Void>} that represents the update password request.
     */
    public static Task<Void> updatePassword(String password) {
        return getAuth().getCurrentUser()
                .updatePassword(password);
    }

    /**
     * Reauthenticates the user using their email and password.
     *
     * @param email    the email.
     * @param password the password.
     * @return a {@link Task<Void>} that represents the reauthenticate request.
     */
    public static Task<Void> reauthenticate(String email, String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        return getAuth().getCurrentUser().reauthenticate(credential);
    }
}
