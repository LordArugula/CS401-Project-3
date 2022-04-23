package com.group1.project3.util;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
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

    public static boolean sendPassResetEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null && user.getEmail() != null) {
            auth.sendPasswordResetEmail(user.getEmail())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Reset password email sent.");
                            } else {
                                Log.d(TAG, "Email failed to send.");
                            }
                        }
                    });

            /*would need to update firestore when they click the link but not sure how*/

            return true;
        } else {
            return false;
        }


    }

    public static boolean reauthAndSetPass(String email, String password, String newpassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Re-authenticated.");
                        setPassword(newpassword);
                    } else {
                        Log.d(TAG, "Failed to re-authenticate. Password is unchanged.");
                    }
                }
            });
            return true;
        } else {
            Log.d(TAG, "Failed to re-authenticate.");
            return false;
        }
    }

    private static void setPassword(String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User password updated.");
                            } else {
                                Log.d(TAG, "Failed to update password.");
                            }
                        }
                    });
        }

    }

    public static boolean setDisplayName(String name) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Username updated in Firebase Auth.");
                                DocumentReference userDocumentRef = FirebaseFirestore.getInstance().document("users/" + user.getUid());
                                userDocumentRef.update("username", name).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Username updated in Firestore.");
                                        } else {
                                            Log.d(TAG, "Username failed to update in Firestore.");
                                        }
                                    }
                                });
                            } else {
                                Log.d(TAG, "Failed to update username.");
                            }
                        }
                    });
            return true;
        } else {
            Log.d(TAG, "Failed to update username.");
            return false;
        }


    }

    public static boolean setProfilePicture(String url) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(url))
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Profile Picture updated in Firebase Auth.");
                                DocumentReference userDocumentRef = FirebaseFirestore.getInstance().document("users/" + user.getUid());
                                userDocumentRef.update("profilePic", url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Profile Picture updated in Firestore.");
                                        } else {
                                            Log.d(TAG, "Profile Picture failed to update in Firestore.");
                                        }
                                    }
                                });
                            } else {
                                Log.d(TAG, "Failed to update profile picture.");
                            }
                        }
                    });
            return true;
        } else {
            Log.d(TAG, "Failed to update profile picture.");
            return false;
        }


    }

    public static boolean reauthAndSetEmail(String email, String password, String newEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Re-authenticated.");
                        setEmail(newEmail);

                        DocumentReference userDocumentRef = FirebaseFirestore.getInstance().document("users/" + user.getUid());
                        userDocumentRef.update("email", newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email updated in Firestore.");
                                } else {
                                    Log.d(TAG, "Email failed to update in Firestore.");
                                }
                            }
                        });
                    } else {
                        Log.d(TAG, "Failed to re-authenticate. Email is unchanged.");
                    }
                }
            });
            return true;
        } else {
            Log.d(TAG, "Failed to re-authenticate.");
            return false;
        }


    }

    private static void setEmail(String email) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email updated.");
                            } else {
                                Log.d(TAG, "Failed to update email.");
                            }
                        }
                    });
        }


    }
}
