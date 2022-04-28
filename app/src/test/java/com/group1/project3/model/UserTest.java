package com.group1.project3.model;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * test class for user class
 */
public class UserTest extends TestCase {

    /**
     * tests the setters
     */
    public void testSetters() {
        User test = new User();
        String id = "123";
        String first = "first";
        String last = "last";
        String user = "user";
        String email = "email";
        String profile = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png";

        test.setFirstName(first);
        test.setLastName(last);
        test.setUsername(user);
        test.setEmail(email);
        test.setProfilePicUri(profile);

        assertEquals(test.getFirstName(), first);
        assertEquals(test.getLastName(), last);
        assertEquals(test.getUsername(), user);
        assertEquals(test.getEmail(), email);
        assertEquals(test.getProfilePicUri(), profile);

    }

    /**
     * tests the alternative constructor
     */
    public void testSecondConstructor() {
        String id = "123";
        String first = "first";
        String last = "last";
        String user = "user";
        String email = "email";
        String profile = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_light_color_272x92dp.png";
        List<String> projectIds = new ArrayList<>();
        User test = new User(id, first, last, user, email);
        test.addProject("project1");
        test.addProject("project2");
        projectIds.add("project1");
        projectIds.add("project2");
        test.setProfilePicUri(profile);

        assertEquals(test.getId(), id);
        assertEquals(test.getFirstName(), first);
        assertEquals(test.getLastName(), last);
        assertEquals(test.getUsername(), user);
        assertEquals(test.getEmail(), email);
        assertEquals(test.getProfilePicUri(), profile);
        assertEquals(test.getProjectIds(), projectIds);
    }

    /**
     * tests the project related functions
     */
    public void testProjectFunctions() {
        User test = new User();
        List<String> projectIds = new ArrayList<>();
        projectIds.add("project1");
        projectIds.add("project2");

        test.addProject("project1");
        test.addProject("project2");
        assertEquals(test.getProjectIds(), projectIds);

        test.removeProject("project2");
        projectIds.remove("project2");
        assertEquals(test.getProjectIds(), projectIds);

        assertTrue(test.hasProject("project1"));
    }
}