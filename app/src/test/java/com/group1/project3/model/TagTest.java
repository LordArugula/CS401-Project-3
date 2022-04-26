package com.group1.project3.model;

import android.graphics.Color;

import junit.framework.TestCase;

/**
 * test for the {@link Card class}
 */
public class TagTest extends TestCase {

    /**
     * test for the setter, getter, and constructors
     */
    public void testSetterAndGetters() {
        String name = "name";
        String newName = "newName";
        Color color = Color.valueOf(0, 0, 0);
        Tag test = new Tag(name);

        test.setColor(color);
        test.setName(newName);

        assertEquals(test.getColor(), color);
        assertEquals(test.getName(), newName);
    }
}