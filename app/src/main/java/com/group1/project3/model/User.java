package com.group1.project3.model;

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
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    /**
     * reference to the current user in Firebase
     */
    FirebaseUser user;

    /**
     * reference to the user's document in the Users collection
     */
    private DocumentReference userDocumentRef;

    /**
     * user's id
     */
    private String id;

    /**
     * user's display name
     */
    private String username;

    /**
     * user's email address
     */
    private String email;

    /**
     * uri to the user's profile picture
     */
    private Uri profilePic;

    /**
     * String : Object map containing user's first name/last name/array of projectIds the user has access to
     */
    private Map<String, Object> firstLastPid = new HashMap<>();

    /**
     * key for firstLastPid
     */
    private final String FIRST_NAME = "first_name";

    /**
     * key for firstLastPid
     */
    private final String LAST_NAME = "last_name";

    /**
     * key for firstLastPid
     */
    private final String PROJECT_IDS = "project_ids";

    /**
     * user's password
     */
    private String password;

    /**
     * Log tag
     */
    private final String TAG = "User Model";

    /**
     * Constructor for User class
     */
    public User() {

        user = FirebaseAuth.getInstance().getCurrentUser();

        //if user is logged in
        if (user != null) {

            //grab FireBase Auth info
            this.id = user.getUid();
            this.username = user.getDisplayName();
            this.email = user.getEmail();
            this.profilePic = null; //profile pic is not part of registration process

            //generate document reference for this user
            userDocumentRef = FirebaseFirestore.getInstance().document("users/" + id);

            //update user's document to contain missing fields
            firstLastPid.put(FIRST_NAME, null);
            firstLastPid.put(LAST_NAME, null);

            /*String[] test = {"test2", "test1"};
            firstLastPid.put(PROJECT_IDS, Arrays.asList(test));*/

            firstLastPid.put(PROJECT_IDS, null);
            userDocumentRef.set(firstLastPid, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "firstLastPid created on database.");
                    } else {
                        Log.d(TAG, "firstLastPid creation failed.");
                    }
                }
            });
        } else {
            Log.d(TAG, "User is not logged in");
        }

    }

    /**
     * returns the user's id
     * @return user id
     */
    public String getId() {
        return id;
    }

    /**
     * returns the user's username
     * @return user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * sets the username of user
     * @param username the user's updated username
     */
    public void setUsername(String username) {
        //update locally
        this.username = username;

        //update in FireBase Auth
        UserProfileChangeRequest usernameChange = new UserProfileChangeRequest.Builder().setDisplayName(username).build();

        user.updateProfile(usernameChange).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Username updated.");
                } else {
                    Log.d(TAG, "Username failed to update.");
                }
            }
        });
    }

    /**
     * returns the user's email address
     * @return user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * sets the user's email address
     * @param email the user's updated email address
     */
    public void setEmail(String email) {
        //update locally
        this.email = email;

        //update in FIreBase Auth
        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Email updated.");
                } else {
                    Log.d(TAG, "Email failed to update.");
                }
            }
        });
    }

    /**
     * returns uri to user's profile picture
     * @return uri of profile picture
     */
    public Uri getProfilePic() {
        return profilePic;
    }

    /**
     * sets the user's profile picture
     * @param profilePic string of url pointing to image
     */
    public void setProfilePic(String profilePic) {
        //update locally
        this.profilePic = Uri.parse(profilePic);

        //update in FireBase Auth
        UserProfileChangeRequest profilePicChange = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(profilePic)).build();

        user.updateProfile(profilePicChange).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Profile picture updated.");
                } else {
                    Log.d(TAG, "Profile picture failed to update.");
                }
            }
        });
    }

    /**
     * returns the user's first name
     * @return the user's first name
     */
    public String getFirstName() {
        return (String) firstLastPid.get(FIRST_NAME);
    }

    /**
     * returns the user's last name
     * @return the user's last name
     */
    public String getLastName() {
        return (String) firstLastPid.get(LAST_NAME);
    }

    /**
     * returns a string array of project ids if it exists, otherwise returns null
     * @return an array of project ids
     */
    public String[] getProjectIds() {
        if (this.firstLastPid.get(PROJECT_IDS) != null) {
            List<Object> old = Arrays.asList(this.firstLastPid.get(PROJECT_IDS));
            String temp = old.toString();
            temp = temp.replaceAll("[\\[\\](){}]", "");
            temp = temp.replaceAll("[^a-zA-Z0-9]", " ");
            //System.out.println(temp);
            String[] splited = temp.split("\\s+");
            for (int i = 0; i < splited.length; i++) {
                System.out.println(splited[i]);

                return splited;
            }
        }
        return null;
    }

    /**
     * sets the user's first name
     * @param firstName first name of the user
     */
    public void setFirstName(String firstName) {
        //updates locally
        this.firstLastPid.put(FIRST_NAME, firstName);

        //updates on the Firestore document
        userDocumentRef.set(firstLastPid, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "first_name updated.");
                } else {
                    Log.d(TAG, "last_name failed to update.");
                }
            }
        });
    }

    /**
     * sets the user's last name
     * @param lastName last name of the user
     */
    public void setLastName(String lastName) {
        //updates locally
        this.firstLastPid.put(LAST_NAME, lastName);

        //updates on the Firestore document
        userDocumentRef.set(firstLastPid, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "last_name updated.");
                } else {
                    Log.d(TAG, "last_name failed to update.");
                }
            }
        });
    }

    /**
     * adds a project id to the list that the user can access
     * @param id a project id
     */
    public void addProjectId(String id) {

        List<Object> old = Arrays.asList(this.firstLastPid.get(PROJECT_IDS));
        String temp = old.toString();
        temp = temp.replaceAll("[\\[\\](){}]","");
        temp = temp.replaceAll("[^a-zA-Z0-9]", " ");
        //System.out.println(temp);
        String[] splited = temp.split("\\s+");
        for (int i = 0; i < splited.length; i++) {
            System.out.println(splited[i]);
        }

        //update locally
        List<String> addTo = new ArrayList<>(Arrays.asList(splited));
        addTo.add(id);
        firstLastPid.put(PROJECT_IDS, addTo.toArray());

        //update Firestore document
        userDocumentRef.set(firstLastPid, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "last_name updated.");
                } else {
                    Log.d(TAG, "last_name failed to update.");
                }
            }
        });
    }

    /**
     * returns true if successfully re-authenticated and false otherwise
     * @param email the user's email address
     * @param password the user's password
     * @return true if successfully re-authenticated
     */
    public Boolean reauthenticate(String email, String password) {
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        Task<Void> auth = user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Successfully re-authenticated.");
                } else {
                    Log.d(TAG, "Failed to re-authenticate.");
                }
            }
        });

        return auth.isSuccessful();
    }

    /**
     * sets the user's password
     * @param password the yser's updated password
     */
    public void setPassword(String password) {
        this.password = password;

        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Password updated.");
                } else {
                    Log.d(TAG, "Password failed to update.");
                }
            }
        });
    }
}
