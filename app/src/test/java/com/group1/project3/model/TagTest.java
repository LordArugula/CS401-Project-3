package com.group1.project3.model;

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
        int color = 123;
        Tag test = new Tag(name);

        test.setColor(color);
        test.setName(newName);

        assertEquals(test.getColor(), color);
        assertEquals(test.getName(), newName);
    }
}