package com.group1.project3.model;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * test class for {@link Card} class
 */
public class CardTest extends TestCase {

    /**
     * tests the setters for id, content, user, and data and the getters for id, title, content, user, date, and tags
     */
    public void testSettersGetters() {
        String id = "123";
        String title = "title";
        String content = "title\ncontent";
        String empty = "";
        List<Tag> tags = new ArrayList<>();
        String assignedUser = "assignedUser";
        Date assignedDate = new Date(1 / 12);
        Card card = new Card();

        card.setId(id);
        assertEquals(card.getId(), id);

        card.setContent(empty);
        assertEquals(card.getTitle(), empty);
        card.setContent(content);
        assertEquals(card.getTitle(), title);
        assertEquals(card.getContent(), content);

        card.setAssignedUser(assignedUser);
        assertEquals(card.getAssignedUser(), assignedUser);

        card.setAssignedDate(assignedDate);
        assertEquals(card.getAssignedDate(), assignedDate);

        assertEquals(card.getTags(), tags);
    }

    /**
     * tests addTag(), hasTag(), and removeTag()
     */
    public void testTagFunctions() {
        String id = "123";
        String name = "name";
        String name2 = "name2";
        List<Tag> tags = new ArrayList<>();
        Tag tag = new Tag(name);
        Tag tag2 = new Tag(name2);
        Card card = new Card(id);

        assertTrue(card.addTag(tag));
        assertTrue(card.addTag(tag2));
        tags.add(tag);
        tags.add(tag2);
        assertEquals(card.getTags(), tags);

        assertTrue(card.hasTag(tag));
        assertFalse(card.hasTag(new Tag("asdf")));

        assertTrue(card.removeTag(tag));
        tags.remove(tag);
        assertEquals(card.getTags(), tags);
    }
}