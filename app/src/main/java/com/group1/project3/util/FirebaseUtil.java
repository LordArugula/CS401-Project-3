package com.group1.project3.util;

import com.google.firebase.auth.FirebaseAuth;
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
}
